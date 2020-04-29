package com.icarodebarros.cursomc.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Endereco extends Pojo<Integer> {
	
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message = "Preenchimento obrigatório")
	@Column(nullable = false)
	private String logradouro;
	
	@NotEmpty(message = "Preenchimento obrigatório")
	@Column(nullable = false)
	private String numero;
	
	private String complemento;
	
	@NotEmpty(message = "Preenchimento obrigatório")
	@Column(nullable = false)
	private String bairro;
	
	@NotEmpty(message = "Preenchimento obrigatório")
	@Length(min = 8, max = 9, message = "CEP inválido")
	@Column(nullable = false)
	private String cep;
	
	@JsonIgnore // @JsonBackReference
	@ManyToOne
	@JoinColumn(name = "cliente_id", nullable = false)
	private Cliente cliente;
	
	@ManyToOne
	@JoinColumn(name = "cidade_id", nullable = false)
	private Cidade cidade;
	
	public Endereco() {
		super();
	}

	public Endereco(Integer id, String logradouro, String numero, String complemento, String bairro, String cep,
			Cliente cliente, Cidade cidade) {
		super(id);
		this.logradouro = logradouro;
		this.numero = numero;
		this.complemento = complemento;
		this.bairro = bairro;
		this.cep = cep;
		this.cliente = cliente;
		this.setCidade(cidade);
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

}
