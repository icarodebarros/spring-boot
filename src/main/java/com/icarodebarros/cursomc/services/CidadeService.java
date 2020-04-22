package com.icarodebarros.cursomc.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.icarodebarros.cursomc.domain.Cidade;
import com.icarodebarros.cursomc.repositories.CidadeRepository;

@Service
public class CidadeService extends GenericService<Cidade, Integer> {
		
	public List<Cidade> findByEstado(Integer estado_id) {
//		return getRepository().findCidades(estado_id);
		List<Object[]> rawResult = this.getRepository().shortFindCidades(estado_id);
		List<Cidade> objects = rawResult.stream().map(obj -> this.mapObjectToClass(obj)).collect(Collectors.toList());		
		return objects;
	}
	
	@Override
	public CidadeRepository getRepository() {
		return (CidadeRepository) super.getRepository();
	}

	@Override
	protected String getMessageDataIntegrityViolation() {
		return "Não é possível apagar essa Cidade pois um ou mais Endereços estão associados a ela";
	}
	
	@Override
	protected Cidade mapObjectToClass(Object[] obj) {
		Cidade cid = new Cidade((Integer) obj[0], (String) obj[1]);		
		return cid;
	}

}
