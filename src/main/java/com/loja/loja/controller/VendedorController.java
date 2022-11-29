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

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateVendedor(@PathVariable Integer id, @RequestBody VendedorDTO dto) {
        vendedorService.updateVendedor(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVendedor(@PathVariable Integer id) {
        vendedorService.deleteVendedor(id);
    }
}
