package com.icarodebarros.cursomc.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.icarodebarros.cursomc.domain.Categoria;

@Repository
public interface CategoriaRepository extends GenericRepository<Categoria, Integer> {
	
	@Transactional(readOnly = true)
	@Query("SELECT obj.id, obj.nome FROM Categoria obj")
	List<Object[]> shortFindAll();
	
	@Transactional(readOnly = true)
	@Query("SELECT obj.id, obj.nome FROM Categoria obj")
	Page<Object[]> shortFindAll(Pageable pageRequest);

}
