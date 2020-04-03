package com.icarodebarros.cursomc.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.icarodebarros.cursomc.domain.Cliente;

@Repository
public interface ClienteRepository extends GenericRepository<Cliente, Integer> {

	@Transactional(readOnly = true) // Melhora a performance indicando que é apenas uma consulta, não uma transação complexa
	Cliente findByEmail(String email);
}
