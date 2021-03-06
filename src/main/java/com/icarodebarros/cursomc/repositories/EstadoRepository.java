package com.icarodebarros.cursomc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.icarodebarros.cursomc.domain.Estado;;

@Repository
public interface EstadoRepository extends GenericRepository<Estado, Integer> {
	
	@Transactional(readOnly = true)
	public List<Estado> findAllByOrderByNome();
	
	@Transactional(readOnly = true)
	@Query("SELECT obj.id, obj.nome FROM Estado obj ORDER BY obj.nome")
	List<Object[]> shortFindAll();

}
