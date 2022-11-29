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
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Código do vendedor inválido"));
        return produto.getId();
    }

    public void updateProduto(Integer id, ProdutoDTO dto) {
        produtoRepository.findById(id).map(produto -> {

            if(dto.getNome() != null && !produto.getNome().equals(dto.getNome())) produto.setNome(dto.getNome());
            if(dto.getValor() != null && !produto.getValor().equals(dto.getValor())) produto.setValor(dto.getValor());
            if(dto.getQuantidade() != null && !produto.getQuantidade().equals(dto.getQuantidade())) produto.setQuantidade(dto.getQuantidade());
            return produtoRepository.save(produto);

        }).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Código do produto inválido."));
    }

    public void deleteProduto(Integer id) {
        produtoRepository.findById(id).map(produto -> {
            produtoRepository.delete(produto);
            return produto;
        }).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Código do produto inválido"));
    }
    
}
