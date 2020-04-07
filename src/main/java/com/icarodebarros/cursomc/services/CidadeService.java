package com.icarodebarros.cursomc.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.icarodebarros.cursomc.domain.Cidade;
import com.icarodebarros.cursomc.repositories.CidadeRepository;

@Service
public class CidadeService extends GenericService<Cidade, Integer> {
		
	public List<Cidade> findByEstado(Integer estado_id) {
		return getRepository().findCidades(estado_id);
	}
	
	@Override
	public CidadeRepository getRepository() {
		return (CidadeRepository) super.getRepository();
	}

	@Override
	protected String getMessageDataIntegrityViolation() {
		return "Não é possível apagar essa Cidade pois um ou mais Endereços estão associados a ela";
	}

}
