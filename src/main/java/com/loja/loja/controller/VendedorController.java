package com.loja.loja.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loja.loja.DTO.VendedorDTO;
import com.loja.loja.model.Vendedor;
import com.loja.loja.service.VendedorService;

@RestController
@RequestMapping("/vendedores")
public class VendedorController {
    
    @Autowired
    private VendedorService vendedorService;

    @GetMapping
    public List<VendedorDTO> getVendedor(Vendedor vendedor) {
        return vendedorService.getVendedor(vendedor);
    }

    @PostMapping
    public void addVendedor(@RequestBody VendedorDTO dto){
        vendedorService.addVendedor(dto);
    }
}
