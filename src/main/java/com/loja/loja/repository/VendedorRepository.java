package com.loja.loja.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.loja.loja.model.Vendedor;

@Repository
public interface VendedorRepository extends JpaRepository<Vendedor, Integer>{

    Optional<Vendedor> findByCpf(String cpf);
    
}
