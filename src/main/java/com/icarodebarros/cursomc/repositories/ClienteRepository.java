package com.icarodebarros.cursomc.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.icarodebarros.cursomc.domain.Cliente;

@Repository
public interface ClienteRepository extends GenericRepository<Cliente, Integer> {

	@Transactional(readOnly = true) // Melhora a performance indicando que é apenas uma consulta, não uma transação complexa
	Cliente findByEmail(String email);
	
	@Transactional(readOnly = true)
	@Query("SELECT obj.id, obj.nome, obj.email FROM Cliente obj")
	List<Object[]> shortFindAll();
	
	@Transactional(readOnly = true)
	@Query("SELECT obj.id, obj.nome, obj.email FROM Cliente obj")
	Page<Object[]> shortFindAll(Pageable pageRequest);
}
