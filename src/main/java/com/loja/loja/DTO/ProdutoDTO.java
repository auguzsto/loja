package com.loja.loja.DTO;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoDTO {
    
    private Integer id;
    private String nome;
    private Integer quantidade;
    private BigDecimal valor;
    private Integer idVendedor;
    private VendedorProdutoDTO vendedorProduto;
    
}
