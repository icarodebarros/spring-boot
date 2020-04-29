 package com.icarodebarros.cursomc.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;


@Entity
public class Categoria extends Pojo<Integer> {
	
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message = "Preenchimento obrigatório")
	@Length(min = 5, max = 80, message = "O tamanho deve ser entre 5 e 80 caracteres")
	@Column(nullable = false, length = 80)
	private String nome;
	
//	@JsonManagedReference
	@ManyToMany(mappedBy = "categorias")
	private List<Produto> produtos = new ArrayList<>();
	
	public Categoria() {
		super();
	}
	
	public Categoria(Integer id, String nome) {
		super(id);
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

}
