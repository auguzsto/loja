package com.loja.loja.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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

import com.loja.loja.DTO.ClienteDTO;
import com.loja.loja.model.Cliente;
import com.loja.loja.service.ClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    
    @Autowired
    private ClienteService clienteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer addCliente(@RequestBody ClienteDTO dto) {
        return clienteService.addCliente(dto);
    }

    @PatchMapping("/{id}")
    public String updateCliente(@PathVariable Integer id, @RequestBody ClienteDTO dto) {
        return clienteService.updateCliente(id, dto);
    }

    @DeleteMapping("/{id}")
    public String deleteCliente(@PathVariable Integer id) {
        return clienteService.deleteCliente(id);
    }

    @GetMapping
    public List<ClienteDTO> getCliente(Cliente cliente) {
        return clienteService.getCliente(cliente);
    }
}
