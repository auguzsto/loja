package com.loja.loja.DTO;

import java.util.List;

import lombok.Getter;

import lombok.Setter;

@Getter
@Setter
public class PedidoDTO {

    private Integer id;
    private Integer idCliente;
    private List<ItensPedidoDTO> itensPedido;
    
}
