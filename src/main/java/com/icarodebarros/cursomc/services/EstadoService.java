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
//		return getRepository().findAllByOrderByNome();
		return super.shortFindAll();
	}
	
	@Override
	protected String getMessageDataIntegrityViolation() {
		return "Não é possível apagar esse Estado pois uma ou mais Cidades estão associadas a ele";
	}
	
	@Override
	protected List<Object[]> repositoryShortFindAll() {
		return this.getRepository().shortFindAll();
	}
	
	@Override
	protected Estado mapObjectToClass(Object[] obj) {
		Estado est = new Estado((Integer) obj[0], (String) obj[1]);
		this.annulObjectLists(est);
		return est;
	}
}
