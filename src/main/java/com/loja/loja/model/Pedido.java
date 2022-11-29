package com.loja.loja.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;

@Entity
@Data
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    private Cliente cliente;

    private LocalDateTime data;

    private LocalDateTime dataEntrega;

    private String status;

    private BigDecimal valorTotal;

    private String formaPagamento;

    @OneToMany(mappedBy = "pedido")
    private List<ItensPedido> itensPedido;
    
}
