package com.icarodebarros.cursomc.resources;

import java.io.Serializable;
import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.icarodebarros.cursomc.domain.Pojo;
import com.icarodebarros.cursomc.services.GenericService;

public abstract class GenericResource<T extends Pojo<ID>, ID extends Serializable> {
		
	public abstract GenericService<T, ID> getService();

	/**
	 * Endpoint que busca objeto pelo id.
	 * @param id
	 * @return Objeto com id correspondente
	 */
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public ResponseEntity<T> find(@PathVariable ID id) {
		T obj = this.getService().find(id);
		return ResponseEntity.ok().body(obj);
	}
	
	/**
	 * Endpoint de inserção de objeto.
	 * @param obj Objeto a ser inserido
	 * @return Retorna a URI de acesso ao novo objeto recém inserido
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody T obj) {
		obj = getService().insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
					.path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	/**
	 * Endpoint de atualização de um objeto.
	 * @param obj Objeto a ser atualizado
	 * @param id Id do objeto a ser atualizado
	 * @return Não há retorno
	 */
	@RequestMapping(value="/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody T obj, @PathVariable ID id) {
		obj.setId(id);
		obj = getService().update(obj);
		return ResponseEntity.noContent().build();
	}
	
	/**
	 * Endpoint de deleção de um objeto da base.
	 * @param id Id do objeto a ser apagado
	 * @return Não há retorno
	 */
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable ID id) {
		this.getService().delete(id);
		return ResponseEntity.noContent().build();
	}
	
	/**
	 * Endpoint de listagem de todos os objetos da classe.
	 * @return Lista dos objetos da classe
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<T>> findAll() {
		List<T> list = this.getService().findAll();
		return ResponseEntity.ok().body(list);
	}
	
	/**
	 * Endpoint de listagem de objetos usando paginação.
	 * @param page Número da página que se deseja
	 * @param linesPerPage Número de objetos por página
	 * @param orderBy Campo pelo qual se deseja ordenar a busca
	 * @param direction Ordenação da busca (crescente ou decrescente)
	 * @return Lista de itens limitada pela paginação
	 */
	@RequestMapping(value="/page", method = RequestMethod.GET)
	public ResponseEntity<Page<T>> findPage(
		 	@RequestParam(value="page", defaultValue = "0") Integer page,
		 	@RequestParam(value="linesPerPage", defaultValue = "24") Integer linesPerPage,
		 	@RequestParam(value="orderBy", defaultValue = "nome") String orderBy,
		 	@RequestParam(value="direction", defaultValue = "ASC") String direction) {
		Page<T> list = this.getService().findPage(page, linesPerPage, orderBy, direction);
		return ResponseEntity.ok().body(list);
	}

}
