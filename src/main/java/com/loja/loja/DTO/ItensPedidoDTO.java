package com.loja.loja.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItensPedidoDTO {
    
    private Integer idProduto;
    private String nomeProduto;
    private Integer quantidade;
    private Integer valor;

}
