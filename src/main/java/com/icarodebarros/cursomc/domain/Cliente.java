package com.icarodebarros.cursomc.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.icarodebarros.cursomc.domain.enums.Perfil;
import com.icarodebarros.cursomc.domain.enums.TipoCliente;
import com.icarodebarros.cursomc.services.validation.ClienteInsert;
import com.icarodebarros.cursomc.services.validation.ClienteUpdate;

@ClienteInsert
@ClienteUpdate
@Entity
public class Cliente extends Pojo<Integer> {
	
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message = "Preenchimento obrigatório")
	@Length(min = 5, max = 120, message = "O tamanho deve ser entre 5 e 120 caracteres")
	@Column(nullable = false)
	private String nome;
	
	@NotEmpty(message = "Preenchimento obrigatório")
	@Email(message = "Email inválido")
	@Column(nullable = true, unique = true)
	private String email;
	
	// Existem anotações @CPF e @CNPJ do hibernate.validator usadas para validação dessas variáveis específicas
//	@NotEmpty(message = "Preenchimento obrigatório") // Validação transferida para ClientInsertValidator para não gerar erro no fluxo de update.
	@Column(nullable = false)
	private String cpfOuCnpj;
	
	@Column(nullable = false)
	private Integer tipo;
	
//	@JsonIgnore // Retirado pois na inserção de Cliente não se deve ignorar a senha. Alteração feira apos remoção do ClienteNewDTO.
	@Column(nullable = false)
	private String senha;

	//	@JsonManagedReference
	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL) // cascade ALL força a deleção dos Endereços ao apagar o Cliente
	private List<Endereco> enderecos = new ArrayList<Endereco>();
	
	@ElementCollection
	@CollectionTable(name = "TELEFONE")
	private Set<String> telefones = new HashSet<>();
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "PERFIS")
	private Set<Integer> perfis = new HashSet<>();
	
	@JsonIgnore // @JsonBackReference
	@OneToMany(mappedBy = "cliente")
	private List<Pedido> pedidos = new ArrayList<Pedido>();
	
	public Cliente() {
		super();
		addPerfil(Perfil.CLIENTE);
	}
	
	// Construtor do mapObjectToClass
	public Cliente(Integer id, String nome, String email) {
		super(id);
		this.nome = nome;
		this.email = email;
	}

	public Cliente(Integer id, String nome, String email, String cpfOuCnpj, TipoCliente tipo, String senha) {
		super(id);
		this.nome = nome;
		this.email = email;
		this.cpfOuCnpj = cpfOuCnpj;
		this.tipo = tipo == null ? null : tipo.getCod();
		this.senha = senha;
		addPerfil(Perfil.CLIENTE);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpfOuCnpj() {
		return cpfOuCnpj;
	}

	public void setCpfOuCnpj(String cpfOuCnpj) {
		this.cpfOuCnpj = cpfOuCnpj;
	}
	// * Get do tipo ENUM *
	public TipoCliente getTipo() { // Mostra ao front a String de descrição (Passível de mudança)
		return TipoCliente.toEnum(this.tipo);
	}
	// * Set do tipo ENUM *
//	public void setTipo(TipoCliente tipo) {
//		this.tipo = tipo.getCod();
//	}
	// * Get do tipo Primitivo (Integer) *	
//	public Integer getTipo() {
//		return tipo;
//	}
	// * Get do tipo Primitivo (Integer) *
	public void setTipo(Integer tipo) { // Recebe do front o Integer (Passível de mudança)
		this.tipo = tipo;
	}
	
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public Set<Perfil> getPerfis() {
		return this.perfis != null ? this.perfis.stream().map(perfil -> Perfil.toEnum(perfil)).collect(Collectors.toSet()) : null;
	}
	
	public void setPerfis(Set<Integer> perfis) {
		this.perfis = perfis;
	}
	
	public void addPerfil(Perfil perfil) {
		this.perfis.add(perfil.getCod());
	}

	public List<Endereco> getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(List<Endereco> enderecos) {
		this.enderecos = enderecos;
	}

	public Set<String> getTelefones() {
		return telefones;
	}

	public void setTelefones(Set<String> telefones) {
		this.telefones = telefones;
	}
	
	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}

}
