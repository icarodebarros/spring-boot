package com.icarodebarros.cursomc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.icarodebarros.cursomc.domain.Cidade;;

@Repository
public interface CidadeRepository extends GenericRepository<Cidade, Integer> {
	
	@Transactional(readOnly = true)
	@Query("SELECT obj FROM Cidade obj WHERE obj.estado.id = :estadoId ORDER BY obj.nome")
	public List<Cidade> findCidades(@Param("estadoId") Integer estado_id);
	
	@Transactional(readOnly = true)
	@Query("SELECT obj.id, obj.nome FROM Cidade obj WHERE obj.estado.id = :estadoId ORDER BY obj.nome")
	public List<Object[]> shortFindCidades(@Param("estadoId") Integer estado_id);

}
