package com.icarodebarros.cursomc.services;

import java.awt.image.BufferedImage;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.icarodebarros.cursomc.domain.Cidade;
import com.icarodebarros.cursomc.domain.Cliente;
import com.icarodebarros.cursomc.domain.Endereco;
import com.icarodebarros.cursomc.domain.enums.Perfil;
import com.icarodebarros.cursomc.domain.enums.TipoCliente;
import com.icarodebarros.cursomc.dto.ClienteDTO;
import com.icarodebarros.cursomc.dto.ClienteNewDTO;
import com.icarodebarros.cursomc.repositories.ClienteRepository;
import com.icarodebarros.cursomc.repositories.EnderecoRepository;
import com.icarodebarros.cursomc.security.UserSS;
import com.icarodebarros.cursomc.services.exceptions.AuthorizationException;
import com.icarodebarros.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService extends GenericService<Cliente, Integer> {
		
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private ImageService imageService;
	
	@Value("${img.prefix.client.profile}")
	private String prefix;
	
	@Value("${img.profile.size}")
	private Integer size;
	
	@Override
	public ClienteRepository getRepository() {
		return (ClienteRepository) super.getRepository();
	}
	
	@Override
	public Cliente find(Integer id) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso Negado");
		}
		
		return super.find(id);
	}
		
	@Override
	protected void postDependencies(Cliente obj) {
		super.postDependencies(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
	}
	
	@Override
	protected String getMessageDataIntegrityViolation() {
		return "Não é possível excluir porque há pedidos relacionados";
	}
	
	public Cliente findByEmail(String email) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.ADMIN) && !email.equals(user.getUsername())) {
			throw new AuthorizationException("Acesso Negado");
		}
		Cliente obj = getRepository().findByEmail(email);
		if (obj == null) {
			throw new ObjectNotFoundException("Objeto não encontrado! Id: " + user.getId() + ", Tipo: " + Cliente.class.getName());
		}
		return obj;
	}
	
	public Cliente fromDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO objDto) {
		Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), 
				TipoCliente.toEnum(objDto.getTipo()), pe.encode(objDto.getSenha()));
		Cidade cid = new Cidade(objDto.getCidadeId(), null, null);
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(),
				objDto.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDto.getTelefone1());
		if (objDto.getTelefone2() != null) {
			cli.getTelefones().add(objDto.getTelefone2());			
		}
		if (objDto.getTelefone3() != null) {
			cli.getTelefones().add(objDto.getTelefone3());			
		}
		return cli;
	}

	@Override
	protected Cliente updateData(Cliente objDB, Cliente obj) {
		objDB.setNome(obj.getNome());
		objDB.setEmail(obj.getEmail());
		return objDB;
	}
	
	public URI uploadProfilePicture(MultipartFile multipartFile) {
		UserSS user = UserService.authenticated();
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
		jpgImage = imageService.cropSquare(jpgImage);
		jpgImage = imageService.resize(jpgImage, size);
		
		String fileName = prefix + user.getId() + ".jpg";
		
		return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
	}

}
