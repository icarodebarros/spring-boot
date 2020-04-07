package com.icarodebarros.cursomc.services;

import org.springframework.stereotype.Service;

import com.icarodebarros.cursomc.domain.Categoria;
import com.icarodebarros.cursomc.dto.CategoriaDTO;

@Service
public class CategoriaService extends GenericService<Categoria, Integer> {
		
	public Categoria fromDTO(CategoriaDTO objDto) {
		return new Categoria(objDto.getId(), objDto.getNome());
	}

	@Override
	protected Categoria updateData(Categoria objDB, Categoria obj) {
		objDB.setNome(obj.getNome());
		return objDB;
	}

	@Override
	protected String getMessageDataIntegrityViolation() {
		return "Não é possível excluir uma categoria que possui produtos";
	}

}
