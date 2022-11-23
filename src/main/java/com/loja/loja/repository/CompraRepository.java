package com.loja.loja.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.loja.loja.model.Compra;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Integer>{
    
}
