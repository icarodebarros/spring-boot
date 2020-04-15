package com.icarodebarros.cursomc.services.exceptions;

import java.util.ArrayList;
import java.util.List;

import com.icarodebarros.cursomc.resources.exceptions.FieldMessage;

/**
 * Exceção que simula erro de validação observado pela anotação @Valid.
 * Quando um atributo de uma classe, por algum motivo não puder ter a anotação @Valid, deve ter sua validação manualmente colocada nos métodos
 * validateInsert()/validateUpdate() e lançada FieldValidationException caso o objeto seja inválido.
 */
public class FieldValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private List<FieldMessage> erros = new ArrayList<>();
	
	public FieldValidationException(String msg) {
		super(msg);
	}
	
//	public FieldValidationException(String msg, Throwable cause) {
//		super(msg, cause);
//	}
	
	public FieldValidationException(String msg, List<FieldMessage> erros) {
		super(msg);
		this.setErros(erros);
	}

	public List<FieldMessage> getErros() {
		return erros;
	}

	public void setErros(List<FieldMessage> erros) {
		this.erros = erros;
	}

}
