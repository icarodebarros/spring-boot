package com.icarodebarros.cursomc.repositories;

import org.springframework.stereotype.Repository;

import com.icarodebarros.cursomc.domain.Categoria;

@Repository
public interface CategoriaRepository extends GenericRepository<Categoria, Integer> {

}
