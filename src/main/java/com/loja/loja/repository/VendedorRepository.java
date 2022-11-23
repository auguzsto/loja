package com.loja.loja.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.loja.loja.model.Vendedor;

@Repository
public interface VendedorRepository extends JpaRepository<Vendedor, Integer>{
    
}
