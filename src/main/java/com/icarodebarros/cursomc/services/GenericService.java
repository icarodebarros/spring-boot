package com.icarodebarros.cursomc.services;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.annotation.Transactional;

import com.icarodebarros.cursomc.domain.Pojo;
import com.icarodebarros.cursomc.repositories.GenericRepository;
import com.icarodebarros.cursomc.services.exceptions.DataIntegrityException;
import com.icarodebarros.cursomc.services.exceptions.ObjectNotFoundException;

public abstract class GenericService<T extends Pojo<ID>, ID extends Serializable> {
	
	@Autowired
	private GenericRepository<T, ID> repository;
	
	private final Class<T> entityClass;
		
	@SuppressWarnings("unchecked")
	public GenericService() {
		this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public GenericRepository<T, ID> getRepository() {
		return repository;
	}

	public void setRepository(final GenericRepository<T, ID> repository) {
		this.repository = repository;
	}
	
	/**
	 * Método que busca objeto por Id. Lança ObjectNotFoundException caso não encontre.
	 * @param id do objeto
	 * @return objeto encontrado (class.T)
	 */
	public T find(ID id) {
		Optional<T> obj = this.repository.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + this.entityClass.getName()));
	}
	
	/**
	 * Método de inserção de objeto.
	 * @param obj Objeto a ser inserido
	 * @return obj Objeto após ser inserido
	 */
	@Transactional
	public T insert(T obj) {
		obj.setId(null);
		this.validateInsert(obj);
		this.preDependencies(obj);
		obj = repository.save(obj);
		this.postDependencies(obj);
		return obj;
	}
	
	/**
	 * Método de atualização de objeto.
	 * @param obj Objeto a ser atualizado
	 * @return obj Objeto após atualização
	 */
	@Transactional
	public T update(T obj) {
		T objDB = this.find(obj.getId());
		objDB = this.updateData(objDB, obj);
		this.validateUpdate(obj);
		this.preDependencies(objDB);
		objDB = repository.save(objDB);
		this.postDependencies(objDB);
		return objDB;
	}
	
	/**
	 * Método de deleçao de objeto. Lança DataIntegrityException caso existam objetos relacionados.
	 * @param id do objeto a ser apagado
	 */
	@Transactional
	public void delete(ID id) {
		T obj = this.find(id);
		this.validateDelete(obj);
		this.deleteDependencies(obj);
		try {
			this.repository.deleteById(id);			
		} catch(DataIntegrityViolationException e) {
			String msg = this.getMessageDataIntegrityViolation() != null ? this.getMessageDataIntegrityViolation() : e.getMessage();
			throw new DataIntegrityException(msg);
		}
	}
	
	/**
	 * Método que busca todos os objetos da Classe.
	 * @return todas as instâncias da classe
	 */
	public List<T> findAll() {
		return this.repository.findAll();
	}
	
	/**
	 * Método para listar objetos paginados.
	 * @param page Número da página que se deseja
	 * @param linesPerPage Número de objetos por página
	 * @param orderBy Campo pelo qual se deseja ordenar a busca
	 * @param direction Ordenação da busca (crescente ou decrescente)
	 * @return lista de itens limitada pela paginação
	 */
	public Page<T> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return this.repository.findAll(pageRequest);
	}
	
	/**
	 * Método auxiliar do Update(obj) para definir quais atributos do objeto serão atualizados.  
	 * @param objDB Objeto a ser atualizado e salvo
	 * @param obj Objeto vindo do front-end com as novas informações
	 * @return por padrão retorna o objeto recebido do front-end
	 */
	protected T updateData(T objDB, T obj) {
		return obj;
	}
	
	/**
	 * Método que deve trazer a mensagem customizada de erro para tentativa de apagar instância que tenha objetos relacionados.
	 * @return Retorna mensagem customizada para DataIntegrityException
	 */
	protected String getMessageDataIntegrityViolation() {
		return null;
	}
	
	/**
	 * Método que valida inserção de um objeto. Deve lançar uma excessão caso exista atributo inválido.
	 * @param obj
	 */
	protected void validateInsert(T obj) {}
	
	/**
	 * Método que valida alteração de um objeto. Deve lançar uma excessão caso exista alteração inválida.
	 * @param obj
	 */
	protected void validateUpdate(T obj) {}
	
	/**
	 * Método que valida deleção de um objeto, verificando relacionamentos do objeto. Deve lançar uma excessão caso exista alteração inválida.
	 * @param obj
	 */
	protected void validateDelete(T obj) {}
	
	/**
	 * Método que resolve as pós dependências de inserção/atualização de um objeto.
	 * @param obj
	 */
	protected void preDependencies(T obj) {}
	
	/**
	 * Método que resolve as pré dependências de inserção/atualização de um objeto.
	 * @param obj
	 */
	protected void postDependencies(T obj) {}
	
	/**
	 * Método que resolve as dependências da deleção de um objeto.
	 * @param obj
	 */
	protected void deleteDependencies(T obj) {}

	
//	protected Class<T> getEntityClass() {
//		if (entityClass == null) {
//			Class<?> genericSuperclass = (Class<?>) getClass().getGenericSuperclass();
//			if (genericSuperclass != null) {
//				Type gs = genericSuperclass.getGenericSuperclass();
//				if (gs instanceof ParameterizedType) {
//					Type type = ((ParameterizedType) gs).getActualTypeArguments()[0];
//					this.entityClass = (Class<T>) type;
//				}
//			}
//		}
//		return this.entityClass;
//	}

}
