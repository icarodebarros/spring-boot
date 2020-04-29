package com.icarodebarros.cursomc.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.icarodebarros.cursomc.domain.Cliente;
import com.icarodebarros.cursomc.domain.Endereco;
import com.icarodebarros.cursomc.domain.enums.Perfil;
import com.icarodebarros.cursomc.repositories.ClienteRepository;
import com.icarodebarros.cursomc.repositories.EnderecoRepository;
import com.icarodebarros.cursomc.resources.exceptions.FieldMessage;
import com.icarodebarros.cursomc.security.UserSS;
import com.icarodebarros.cursomc.services.exceptions.AuthorizationException;
import com.icarodebarros.cursomc.services.exceptions.FieldValidationException;
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
		obj.setSenha(null);
		
		return obj;
	}
	
	@Override
	protected void validateInsert(Cliente obj) {
		super.validateInsert(obj);
		List<FieldMessage> erros = new ArrayList<>();
		if (obj.getSenha() == null) {
			erros.add(new FieldMessage("senha", "Preenchimento obrigatório"));
		}
		if (obj.getEnderecos() == null || obj.getEnderecos().size() == 0) {
			erros.add(new FieldMessage("enderecos", "Preenchimento obrigatório"));
		}
		if (obj.getTelefones() == null || obj.getTelefones().isEmpty()) {
			erros.add(new FieldMessage("telefones", "Preenchimento obrigatório"));
		}
		if (!erros.isEmpty()) {
			throw new FieldValidationException("Campo(s) obrigatório(s) está(ão) nulo ou contém lista(s) vazia(s)", erros);			
		}
	}
	
	@Override
	protected void preDependencies(Cliente obj) {
		super.preDependencies(obj);
		if (obj.getId() == null) { // Insersção...			
			obj.setSenha(pe.encode(obj.getSenha()));
			for (Endereco e: obj.getEnderecos()) {
				e.setCliente(obj);
			}
		}
	}
	
	@Override
	protected Cliente updateData(Cliente objDB, Cliente obj) {
		objDB.setNome(obj.getNome());
		objDB.setEmail(obj.getEmail());
		return objDB;
	}
	
	@Override
	public List<Cliente> findAll() {
		return super.shortFindAll();
	}
	
	@Override
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		return super.shortFindPage(page, linesPerPage, orderBy, direction);
	}
	
	@Override
	protected List<Object[]> repositoryShortFindAll() {
		return this.getRepository().shortFindAll();
	}
	
	@Override
	protected Page<Object[]> repositoryShortFindAll(PageRequest pageRequest) {
		return this.getRepository().shortFindAll(pageRequest);
	}
	
	@Override
	protected Cliente mapObjectToClass(Object[] obj) {
		Cliente cli = new Cliente((Integer) obj[0], (String) obj[1], (String) obj[2]);
		this.annulObjectLists(cli);		
		return cli;
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
