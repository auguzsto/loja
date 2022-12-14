package com.loja.loja.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.loja.loja.DTO.ProdutoDTO;
import com.loja.loja.model.Produto;
import com.loja.loja.service.ProdutoService;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {
    
    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public List<ProdutoDTO> getProduto(Produto produto){
        return produtoService.getProduto(produto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer addProduto(@RequestBody ProdutoDTO dto){
        return produtoService.addProduto(dto);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProduto(@PathVariable Integer id, @RequestBody ProdutoDTO dto) {
        produtoService.updateProduto(id, dto);
    } 

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduto(@PathVariable Integer id) {
        produtoService.deleteProduto(id);
    }
}
