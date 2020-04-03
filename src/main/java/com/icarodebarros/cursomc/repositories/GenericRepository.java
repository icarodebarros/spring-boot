package com.icarodebarros.cursomc.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.icarodebarros.cursomc.domain.Pojo;

public interface GenericRepository<T extends Pojo<ID>, ID extends Serializable> extends JpaRepository<T, ID> {

}
