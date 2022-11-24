package com.loja.loja.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.loja.loja.DTO.CompraDTO;
import com.loja.loja.model.Compra;
import com.loja.loja.repository.ClienteRepository;
import com.loja.loja.repository.CompraRepository;
import com.loja.loja.repository.ProdutoRepository;

@Service
public class CompraService {
    
    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired ClienteRepository clienteRepository;

    @Autowired
    private ModelMapper modelMapper;

    //Convert Model class to DTO class.
    public CompraDTO toClienteDTO(Compra cliente){
        return modelMapper.map(cliente, CompraDTO.class);
    }

    //Cliente buying product.
    public String buyProduto(CompraDTO dtoCompra) {
        clienteRepository.findById(dtoCompra.getCliente()).map(cliente -> {
            produtoRepository.findById(dtoCompra.getProduto()).map(produto -> {
            Integer getEstoque = produto.getQuantidade();
            Integer getQuantidadeCompra = dtoCompra.getQuantidade();
                if(getEstoque >= getQuantidadeCompra) {
                    Compra compra = new Compra();
                    Integer cal = getEstoque - getQuantidadeCompra;
                    produto.setQuantidade(cal);
                    produtoRepository.save(produto);
                    compra.setProduto(produto);
                    compra.setQuantidade(getQuantidadeCompra);
                    compra.setCliente(cliente);
                    compraRepository.save(compra);
                    //return "Sua compra foi efetuada com sucesso!";
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A quantidade do produto na compra ultrapassa a quantidade do estoque disponível para venda.");
                }
                return Void.TYPE;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
            return "Compra efetuada com sucesso"; 
        });
        return "Compra efetuada com sucesso";
    }
}
