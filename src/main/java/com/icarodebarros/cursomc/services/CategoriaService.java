package com.icarodebarros.cursomc.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.icarodebarros.cursomc.domain.Categoria;
import com.icarodebarros.cursomc.repositories.CategoriaRepository;

@Service
public class CategoriaService extends GenericService<Categoria, Integer> {
		
	@Override
	protected Categoria updateData(Categoria objDB, Categoria obj) {
		objDB.setNome(obj.getNome());
		return objDB;
	}

	@Override
	protected String getMessageDataIntegrityViolation() {
		return "Não é possível excluir uma categoria que possui produtos";
	}
	
	@Override
	public CategoriaRepository getRepository() {
		return (CategoriaRepository) super.getRepository();
	}
	
	@Override
	public List<Categoria> findAll() {
		return super.shortFindAll();
	}
	
	@Override
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
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
	protected Categoria mapObjectToCategoria(Object[] obj) {
		Categoria cat = new Categoria((Integer) obj[0], (String) obj[1]);
		cat.setProdutos(null);
		return cat;
	}

}
