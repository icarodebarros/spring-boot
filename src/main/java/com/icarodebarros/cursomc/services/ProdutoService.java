package com.icarodebarros.cursomc.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.icarodebarros.cursomc.domain.Categoria;
import com.icarodebarros.cursomc.domain.Produto;
import com.icarodebarros.cursomc.repositories.CategoriaRepository;
import com.icarodebarros.cursomc.repositories.ProdutoRepository;

@Service
public class ProdutoService extends GenericService<Produto, Integer> {
		
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Override
	public ProdutoRepository getRepository() {
		return (ProdutoRepository) super.getRepository();
	}
	
	public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy,
			String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		List<Categoria> categorias = new ArrayList<Categoria>();
		if (ids.size() == 0) {
			categorias = categoriaRepository.findAll();	
		} else {
			categorias = categoriaRepository.findAllById(ids);			
		}
//		return getRepository().search(nome, categorias, pageRequest);
		Page<Object[]> rawResult = getRepository().shortSearch(nome, categorias, pageRequest);
		Page<Produto> produtos = rawResult.map(obj -> this.mapObjectToClass(obj));		
		return produtos;
	}
	
	@Override
	protected Produto mapObjectToClass(Object[] obj) {
		Produto prod = new Produto((Integer) obj[0], (String) obj[1], (Double) obj[2]);
		this.annulObjectLists(prod);		
		return prod;
	}

}
