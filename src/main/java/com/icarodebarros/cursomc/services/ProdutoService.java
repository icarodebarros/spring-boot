package com.icarodebarros.cursomc.services;

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
		List<Categoria> categorias = categoriaRepository.findAllById(ids);
		return getRepository().search(nome, categorias, pageRequest);
	}

}
