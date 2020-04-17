package com.icarodebarros.cursomc.services;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
	 * @param id Id do objeto
	 * @return Objeto encontrado (class.T)
	 */
	public T find(ID id) {
		Optional<T> obj = this.getRepository().findById(id);
		
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
		obj = getRepository().save(obj);
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
		objDB = getRepository().save(objDB);
		this.postDependencies(objDB);
		return objDB;
	}
	
	/**
	 * Método de deleçao de objeto. Lança DataIntegrityException caso existam objetos relacionados.
	 * @param id Id do objeto a ser apagado
	 */
	@Transactional
	public void delete(ID id) {
		T obj = this.find(id);
		this.validateDelete(obj);
		this.deleteDependencies(obj);
		try {
			this.getRepository().deleteById(id);			
		} catch(DataIntegrityViolationException e) {
			String msg = this.getMessageDataIntegrityViolation() != null ? this.getMessageDataIntegrityViolation() : e.getMessage();
			throw new DataIntegrityException(msg);
		}
	}
	
	/**
	 * Método que busca todos os objetos completos da Classe.
	 * @return Todas as instâncias da classe
	 */
	public List<T> findAll() {
		return this.getRepository().findAll();
	}
	
	/**
	 * Método para listar objetos completos paginados.
	 * @param page Número da página que se deseja
	 * @param linesPerPage Número de objetos por página
	 * @param orderBy Campo pelo qual se deseja ordenar a busca
	 * @param direction Ordenação da busca (crescente ou decrescente)
	 * @return Lista de itens limitada pela paginação
	 */
	public Page<T> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return this.getRepository().findAll(pageRequest);
	}
	
	/**
	 * Método semelhante ao findAll() mas que retorna informações resumidas.
	 * Depende da implementação dos métodos repositoryShortFindAll() e mapObjectToCategoria(obj).
	 * @return Todas as instâncias resumidas da classe
	 */
	public List<T> shortFindAll() {
		List<Object[]> rawResult = this.repositoryShortFindAll();
		if (rawResult == null) {
			throw new NullPointerException("Necessária a implementação do método repositoryShortFindAll() no Service!");
		}
		List<T> objects = rawResult.stream().map(obj -> this.mapObjectToClass(obj)).collect(Collectors.toList());		
		return objects;
	}
	
	/**
	 * Método semelhante ao findPage(...) mas que retorna informações resumidas.
	 * Depende da implementação dos métodos repositoryShortFindAll(pageRequest) e mapObjectToCategoria(obj).
	 * @return Todas as instâncias resumidas da classe
	 */
	public Page<T> shortFindPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);	
		Page<Object[]> rawResult = this.repositoryShortFindAll(pageRequest);
		if (rawResult == null) {
			throw new NullPointerException("Necessária a implementação do método repositoryShortFindAll(PageRequest pageRequest) no Service!");
		}
		Page<T> objects = rawResult.map(obj -> this.mapObjectToClass(obj));		
		return objects;
	}
	
	/**
	 * Método auxiliar que deve ser sobrescrito sempre que for necessário usar o shortFindAll() do Service.
	 * @return Deve retorna a chamada do Repositório sobre a função customizada do shortFindAll().
	 */
	protected List<Object[]> repositoryShortFindAll() {
		return null;
	}
	
	/**
	 * Método auxiliar que deve ser sobrescrito sempre que for necessário usar o shortFindPage(...) do Service.
	 * @return Deve retorna a chamada do Repositório sobre a função customizada do shortFindAll(Pageable pageRequest).
	 */
	protected Page<Object[]> repositoryShortFindAll(PageRequest pageRequest) {
		return null;
	}
	
	/**
	 * Método auxiliar que deve ser sobrescrito sempre que for necessário usar o shortFindAll() ou o shortFindPage(...) do Service.
	 * @return Deve retorna o objeto da Classe T criado a partir dos resultados das querys SQL de busca resumida.
	 */	
	protected T mapObjectToClass(Object[] obj) {
		return null;
	}
	
	/**
	 * Método auxiliar que percorre um Objeto T e coloca valor null nos atributos do tipo List ou Set que estejam vazios.
	 * Deve ser usado dentro do método mapObjectToClass(...) para que as buscas resumidas (que não carregam os valores de suas listas)
	 * tenham essas propriedades do tipo coleção anuladas, deixando claro que essas propriedades não foram trazidas, e não que são vazias.
	 * @param obj Objeto a ter suas listas vazias anuladas.
	 */
	protected void annulObjectLists(T obj) {
		Field fields[] = this.entityClass.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			
			try {
				Boolean isEmpty = false;
				if (fields[i].getType() == List.class) { // Verifica atributos do tipo List vazios
					ArrayList<?> lst = ((ArrayList<?>) fields[i].get(obj));
					isEmpty = (lst != null && lst.size() == 0);
				
				} else if (fields[i].getType() == Set.class) { // Verifica atributos do tipo Set vazios
					HashSet<?> st = ((HashSet<?>) fields[i].get(obj));
					isEmpty = (st != null && st.size() == 0);
				}

				if (isEmpty) { // Coloca null nas coleções vazias
					fields[i].set(obj, null);
				}
				
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Método auxiliar do Update(obj) para definir quais atributos do objeto serão atualizados.  
	 * @param objDB Objeto a ser atualizado e salvo
	 * @param obj Objeto vindo do front-end com as novas informações
	 * @return Por padrão retorna o objeto recebido do front-end
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
