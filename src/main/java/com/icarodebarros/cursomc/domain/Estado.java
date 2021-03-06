package com.icarodebarros.cursomc.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Estado extends Pojo<Integer> {
	
	private static final long serialVersionUID = 1L;
	
	@Length(min = 4, max = 30, message = "O tamanho deve ser entre 4 e 30 caracteres")
	@Column(nullable = false)
	private String nome;
	
	@JsonIgnore // @JsonBackReference
	@OneToMany(mappedBy = "estado")
	private List<Cidade> cidades = new ArrayList<Cidade>();
	
	public Estado() {
		super();
	}

	public Estado(Integer id, String nome) {
		super(id);
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Cidade> getCidades() {
		return cidades;
	}

	public void setCidades(List<Cidade> cidades) {
		this.cidades = cidades;
	}	

}
