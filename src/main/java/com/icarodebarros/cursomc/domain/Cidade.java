package com.icarodebarros.cursomc.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

@Entity
public class Cidade extends Pojo<Integer> {
	
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message = "Preenchimento obrigatório")
	@Length(min = 3, max = 50, message = "O tamanho deve ser entre 3 e 50 caracteres")
	@Column(nullable = false, length = 50)
	private String nome;
	
//	@JsonManagedReference
	@ManyToOne
	@JoinColumn(name = "estado_id", nullable = false)
	private Estado estado;
	
	public Cidade() {
		super();
	}
	
	public Cidade(Integer id, String nome) {
		super(id);
		this.nome = nome;
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
