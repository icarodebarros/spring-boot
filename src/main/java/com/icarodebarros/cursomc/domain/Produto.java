package com.icarodebarros.cursomc.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Produto extends Pojo<Integer> {
	
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message = "Preenchimento obrigatório")
	@Length(min = 5, max = 120, message = "O tamanho deve ser entre 5 e 120 caracteres")
	@Column(nullable = false)
	private String nome;
	
	@Column(nullable = false)
	private Double preco;
	
	@JsonIgnore // @JsonBackReference
	@ManyToMany
	@JoinTable(name = "PRODUTO_CATEGORIA",
		joinColumns = @JoinColumn(name = "produto_id", nullable = false),
		inverseJoinColumns = @JoinColumn(name = "categoria_id", nullable = false)
	)
	private List<Categoria> categorias = new ArrayList<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "id.produto")
	private Set<ItemPedido> itens = new HashSet<>();
	
	public Produto() {
		super();
	}

	public Produto(Integer id, String nome, Double preco) {
		super(id);
		this.nome = nome;
		this.preco = preco;
	}
	
	@JsonIgnore
	public List<Pedido> getPedidos() {
		List<Pedido> lista = new ArrayList<>();
		for (ItemPedido ip: itens) {
			lista.add(ip.getPedido());
		}
		return lista;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}	

	public Set<ItemPedido> getItens() {
		return itens;
	}

	public void setItens(Set<ItemPedido> itens) {
		this.itens = itens;
	}

}
