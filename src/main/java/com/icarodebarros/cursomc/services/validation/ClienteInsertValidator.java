package com.icarodebarros.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.icarodebarros.cursomc.domain.Cliente;
import com.icarodebarros.cursomc.domain.enums.TipoCliente;
import com.icarodebarros.cursomc.repositories.ClienteRepository;
import com.icarodebarros.cursomc.resources.exceptions.FieldMessage;
import com.icarodebarros.cursomc.services.validation.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, Cliente> {
	
	// OBS. 1: Como existem dois validadores customizados para Cliente (@ClienteInsert e @ClienteUpdate) o ideal é usar um DTO para cada
	// fluxo, caso contrário os métodos isValid(...) ficarão mais verbosos desnecessariamente.
	// Projeto original com um DTO pra cada fluxo: https://github.com/icarodebarros/cursomc
	
	// OBS. 2: No perfil teste são carregados dados iniciais (DBService) que ao tentarem passar por validações que usam os atributos @Autowired
	// abaixo lançam erros já que no momento da inicialização do app esses atributos ainda estão nulos! Por isso o ifs de verificação.
	
	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private HttpServletRequest request;
		
	@Override
	public void initialize(ClienteInsert ann) {
		// Quando for necessário algum tipo de inicialização. (No nosso caso não é)
	}

	@Override
	public boolean isValid(Cliente obj, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();
		
		if (request != null) {
			
			boolean isInsert = "POST".equals(request.getMethod());
			
			if (isInsert) { // Valida apenas insersão
				if (obj.getCpfOuCnpj() == null) {
					list.add(new FieldMessage("cpfOuCnpj", "Preenchimento obrigatório"));
				} else {
					if (obj.getTipo().equals(TipoCliente.PESSOAFISICA) && !BR.isValidCPF(obj.getCpfOuCnpj())) {
						list.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
					}
					
					if (obj.getTipo().equals(TipoCliente.PESSOAJURIDICA) && !BR.isValidCNPJ(obj.getCpfOuCnpj())) {
						list.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));
					}					
				}				
				
				if (repo != null) {
					Cliente aux = repo.findByEmail(obj.getEmail());		
					if (aux != null) {
						list.add(new FieldMessage("email", "E-mail já existente"));
					}			
				}			
			}			
		}			
		
		// Código padrão que o framework usa para criar as excessões dele baseado na nossa lista de erros
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}