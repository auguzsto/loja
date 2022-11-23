package com.loja.loja.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoDTO {
    
    private Integer id;
    private String nome;
    private Integer quantidade;
    private Integer valor;
    private Integer idVendedor;
    private VendedorProdutoDTO vendedorProduto;
    
}
