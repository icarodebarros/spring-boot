 package com.icarodebarros.cursomc.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.icarodebarros.cursomc.domain.Cliente;
import com.icarodebarros.cursomc.services.ClienteService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource extends GenericResource<Cliente, Integer> {
	
	@Autowired
	private ClienteService service;
	
	@Override
	public ClienteService getService() {
		return (ClienteService) this.service;
	}
	
	@ApiOperation(value="Busca por e-mail")
	@RequestMapping(value="/email", method = RequestMethod.GET)
	public ResponseEntity<Cliente> find(@RequestParam(value = "value") String email) {
		Cliente obj = this.service.findByEmail(email);
		return ResponseEntity.ok().body(obj);
	}
	
	@Override
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public ResponseEntity<Cliente> find(@PathVariable Integer id) {
		Cliente obj = this.getService().find(id);
		obj.setSenha(null);
		return ResponseEntity.ok().body(obj);
	}
	
	@Override
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		return super.delete(id);
	}
	
	@Override
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Cliente>> findAll() {
		return super.findAll();
	}
	
	@Override
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value="/page", method = RequestMethod.GET)
	public ResponseEntity<Page<Cliente>> findPage(
		 	@RequestParam(value="page", defaultValue = "0") Integer page,
		 	@RequestParam(value="linesPerPage", defaultValue = "24") Integer linesPerPage,
		 	@RequestParam(value="orderBy", defaultValue = "nome") String orderBy,
		 	@RequestParam(value="direction", defaultValue = "ASC") String direction) {
		return super.findPage(page, linesPerPage, orderBy, direction);
	}
	
	@ApiOperation(value="Insere imagem de perfil do cliente")
	@RequestMapping(value = "/picture", method = RequestMethod.POST)
	public ResponseEntity<Void> uploadProfilePicture(@RequestParam(name = "file") MultipartFile file) {
		URI uri = service.uploadProfilePicture(file);		
		return ResponseEntity.created(uri).build();
	}

}
