package com.icarodebarros.cursomc.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Cidade extends Pojo<Integer> {
	
	private static final long serialVersionUID = 1L;
		
	private String nome;
	
//	@JsonManagedReference
	@ManyToOne
	@JoinColumn(name = "estado_id")
	private Estado estado;
	
	public Cidade() {
		super();
	}

	public Cidade(Integer id, String nome, Estado estado) {
		super(id);
		this.nome = nome;
		this.estado = estado;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

}
