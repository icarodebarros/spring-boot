package com.icarodebarros.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.icarodebarros.cursomc.domain.Cliente;
import com.icarodebarros.cursomc.repositories.ClienteRepository;
import com.icarodebarros.cursomc.resources.exceptions.FieldMessage;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, Cliente> {
	
	// OBS. 1: Como existem dois validadores customizados para Cliente (@ClienteInsert e @ClienteUpdate) o ideal é usar um DTO para cada
	// fluxo, caso contrário os métodos isValid(...) ficarão mais verbosos desnecessariamente.
	// Projeto original com um DTO pra cada fluxo: https://github.com/icarodebarros/cursomc
	
	// OBS. 2: No perfil teste são carregados dados iniciais (DBService) que ao tentarem passar por validações que usam os atributos @Autowired
	// abaixo lançam erros já que no momento da inicialização do app esses atributos ainda estão nulos! Por isso o ifs de verificação.
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private ClienteRepository repo;
	
	@Override
	public void initialize(ClienteUpdate ann) {
		// Quando for necessário algum tipo de inicialização. (No nosso caso não é)
	}

	@Override
	public boolean isValid(Cliente obj, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();
		
		if (request != null) {
			
			boolean isUpdate = "PUT".equals(request.getMethod());
			
			if (isUpdate) {
				@SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
				Integer uriId = Integer.parseInt(map.get("id"));		
				
				Cliente aux = repo.findByEmail(obj.getEmail());		
				if (aux != null && !aux.getId().equals(uriId)) {
					list.add(new FieldMessage("email", "E-mail já existente"));
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