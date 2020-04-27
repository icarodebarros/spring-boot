 package com.icarodebarros.cursomc.resources;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.icarodebarros.cursomc.domain.Pedido;
import com.icarodebarros.cursomc.services.PedidoService;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResource extends GenericResource<Pedido, Integer> {
	
	@Autowired
	private PedidoService service;
	
	@Override
	public PedidoService getService() {
		return (PedidoService) this.service;
	}
	
	@Override
	public ResponseEntity<Void> delete(Integer id) {
		// Método com acesso bloqueado
		return ResponseEntity.notFound().build();
	}
	
	@Override
	public ResponseEntity<Void> update(@Valid Pedido obj, Integer id) {
		// Método com acesso bloqueado
		return ResponseEntity.notFound().build();
	}
	
	@Override
	public ResponseEntity<List<Pedido>> findAll() {
		// Método com acesso bloqueado
		return ResponseEntity.notFound().build();
	}
	
	@Override
	@RequestMapping(value="/page", method = RequestMethod.GET)
	public ResponseEntity<Page<Pedido>> findPage(
		 	@RequestParam(value="page", defaultValue = "0") Integer page,
		 	@RequestParam(value="linesPerPage", defaultValue = "24") Integer linesPerPage,
		 	@RequestParam(value="orderBy", defaultValue = "instante") String orderBy,
		 	@RequestParam(value="direction", defaultValue = "DESC") String direction) {
		return super.findPage(page, linesPerPage, orderBy, direction);
	}

}
