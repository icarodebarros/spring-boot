package com.icarodebarros.cursomc.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.icarodebarros.cursomc.domain.Estado;
import com.icarodebarros.cursomc.repositories.EstadoRepository;

@Service
public class EstadoService extends GenericService<Estado, Integer> {

	@Override
	public EstadoRepository getRepository() {
		return (EstadoRepository) super.getRepository();
	}
	
	@Override
	public List<Estado> findAll() {
		return getRepository().findAllByOrderByNome();
	}
	
	@Override
	protected String getMessageDataIntegrityViolation() {
		return "Não é possível apagar esse Estado pois uma ou mais Cidades estão associadas a ele";
	}
}
