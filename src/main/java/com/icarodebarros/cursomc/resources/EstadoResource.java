package com.icarodebarros.cursomc.resources;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.icarodebarros.cursomc.domain.Cidade;
import com.icarodebarros.cursomc.domain.Estado;
import com.icarodebarros.cursomc.services.CidadeService;
import com.icarodebarros.cursomc.services.EstadoService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/estados")
public class EstadoResource extends GenericResource<Estado, Integer> {
	
	@Autowired
	private EstadoService service;
	
	@Autowired
	private CidadeService cidadeService;
	
	@Override
	public EstadoService getService() {
		return (EstadoService) this.service;
	}
	
	@Override
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody Estado obj) {
		return super.insert(obj);
	}
	
	@Override
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value="/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody Estado obj, @PathVariable Integer id) {
		return super.update(obj, id);
	}
	
	@Override
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		return super.delete(id);
	}
	
	@ApiOperation(value="Retorna todas as cidades de um determinado estado")
	@RequestMapping(value = "/{estadoId}/cidades", method = RequestMethod.GET)
	public ResponseEntity<List<Cidade>> findCidades(@PathVariable Integer estadoId) {
		List<Cidade> list = this.cidadeService.findByEstado(estadoId);
		return ResponseEntity.ok().body(list);
	}

}
