package com.icarodebarros.cursomc.repositories;

import org.springframework.stereotype.Repository;

import com.icarodebarros.cursomc.domain.Pagamento;

@Repository
public interface PagamentoRepository extends GenericRepository<Pagamento, Integer> {

}
