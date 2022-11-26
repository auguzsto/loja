package com.loja.loja.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.loja.loja.model.ItensPedido;
import com.loja.loja.model.Pedido;

@Repository
public interface ItensPedidoRepository extends JpaRepository<ItensPedido, Integer>{
    
    Optional<Pedido> findByPedido(Integer pedido);
}
