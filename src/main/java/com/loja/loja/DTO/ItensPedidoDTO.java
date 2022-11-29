package com.loja.loja.DTO;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItensPedidoDTO {
    
    private Integer idProduto;
    private String nomeProduto;
    private Integer quantidade;
    private BigDecimal valor;

}
