package com.loja.loja.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.loja.loja.DTO.ProdutoDTO;
import com.loja.loja.model.Produto;
import com.loja.loja.repository.ProdutoRepository;
import com.loja.loja.repository.VendedorRepository;

@Service
public class ProdutoService {
    
    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private VendedorRepository vendedorRepository;

    @Autowired
    private ModelMapper modelMapper;

    //Convert Model class to DTO class.
    public ProdutoDTO toProdutoDTO(Produto produto){ 
        return modelMapper.map(produto, ProdutoDTO.class);
    }
    
    public List<ProdutoDTO> getProduto(Produto produto) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING);
        Example<Produto> p = Example.of(produto, matcher);
        return produtoRepository.findAll(p).stream().map(this::toProdutoDTO).collect(Collectors.toList());
    }

    public Integer addProduto(ProdutoDTO dto) {
        Produto produto = new Produto();
        vendedorRepository.findById(dto.getIdVendedor()).map(vendedor -> {
        produto.setNome(dto.getNome());
        produto.setValor(dto.getValor());
        produto.setQuantidade(dto.getQuantidade());
        produto.setVendedor(vendedor);
        produtoRepository.save(produto);
        return Void.TYPE;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return produto.getId();
    }
    
}
