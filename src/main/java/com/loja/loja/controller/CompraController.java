package com.loja.loja.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.loja.loja.DTO.CompraDTO;
import com.loja.loja.service.CompraService;

@RestController
@RequestMapping("/compras")
public class CompraController {
    
    @Autowired
    private CompraService compraService;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String buyProduto(@RequestBody CompraDTO dto){
        return compraService.buyProduto(dto);
    }

}
