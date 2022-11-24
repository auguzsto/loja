package com.loja.loja.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.loja.loja.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer>{

    Optional<Cliente> findByLogin(String login);
    
}
