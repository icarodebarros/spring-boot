 package com.icarodebarros.cursomc.resources;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.icarodebarros.cursomc.domain.Produto;
import com.icarodebarros.cursomc.resources.utils.URL;
import com.icarodebarros.cursomc.services.ProdutoService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResource extends GenericResource<Produto, Integer> {
	
	@Autowired
	private ProdutoService service;
	
	@Override
	public ProdutoService getService() {
		return (ProdutoService) this.service;
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Void> insert(@Valid Produto obj) {
		// TODO Auto-generated method stub
		return super.insert(obj);
	}
	
	@Override
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Void> update(@Valid Produto obj, Integer id) {
		// TODO Auto-generated method stub
		return super.update(obj, id);
	}
	
	@Override
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Void> delete(Integer id) {
		// TODO Auto-generated method stub
		return super.delete(id);
	}
	
	@Override
	public ResponseEntity<List<Produto>> findAll() {
		// Método com acesso bloqueado
		return ResponseEntity.notFound().build();
	}
	
	@ApiOperation(value="Busca customizada de produtos com paginação")
	@RequestMapping(value="/search", method = RequestMethod.GET)
	public ResponseEntity<Page<Produto>> search(
			@RequestParam(value="nome", defaultValue = "") String nome,
			@RequestParam(value="categorias", defaultValue = "") String categorias,
		 	@RequestParam(value="page", defaultValue = "0") Integer page,
		 	@RequestParam(value="linesPerPage", defaultValue = "24") Integer linesPerPage,
		 	@RequestParam(value="orderBy", defaultValue = "nome") String orderBy,
		 	@RequestParam(value="direction", defaultValue = "ASC") String direction) {
		String nomeDecoded = URL.decodePram(nome);
		List<Integer> ids = new ArrayList<Integer>();
		try {
			ids = URL.decodeIntList(categorias);			
		} catch (Exception e) {}
		Page<Produto> list = this.service.search(nomeDecoded, ids, page, linesPerPage, orderBy, direction);
		return ResponseEntity.ok().body(list);
	}

}
