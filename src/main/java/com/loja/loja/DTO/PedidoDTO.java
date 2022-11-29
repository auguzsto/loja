package com.loja.loja.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

import lombok.Setter;

@Getter
@Setter
public class PedidoDTO {

    private Integer id;
    private Integer idCliente;

    @JsonFormat(pattern="dd/MM/yyyy HH:mm")
    private LocalDateTime data;

    @JsonFormat(pattern="dd/MM/yyyy HH:mm")
    private LocalDateTime dataEntrega;
    
    private BigDecimal valorTotal;
    private String status;
    private String formaPagamento;
    private List<ItensPedidoDTO> itensPedido;
    
}
