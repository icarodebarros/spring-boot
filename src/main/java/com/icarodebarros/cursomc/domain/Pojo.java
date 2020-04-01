package com.icarodebarros.cursomc.domain;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Pojo<T extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private T id;
	
//	@Version
//	private Long versao;
//
//	@Temporal(TemporalType.TIMESTAMP)
//	private Date dataInclusao;
//
//	@Temporal(TemporalType.TIMESTAMP)
//	private Date dataAlteracao;
//
//	@Temporal(TemporalType.TIMESTAMP)
//	private Date dataExclusao;
//	
//	@Embedded
//	private UsuarioLogado usuarioVersao;
	
	public Pojo() {
		super();
	}
	
	public Pojo(T id) {
		super();
		this.setId(id);
	}

	/**
	 * Retorna o ID do objeto
	 * 
	 * @return ID do objeto
	 */
	public T getId() {
		return this.id;
	}
	
	/**
	 * Define o novo valor para o ID
	 * 
	 * @param id Novo valor de ID
	 */
	public void setId(final T id) {
		this.id = id;
	}
	
	
	/**
	 * Metodo hashcode que considera o id como identidade do objeto.
	 * 
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}
	
	/**
	 * Metodo para comparar os objetos Pojo.
	 * 
	 * @param obj Objeto que deseja comparar
	 * @return <code>true</code> se os objetos forem iguais
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pojo<?> other = (Pojo<?>) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

}
