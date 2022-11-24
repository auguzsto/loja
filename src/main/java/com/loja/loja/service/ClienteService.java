package com.loja.loja.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.loja.loja.DTO.ClienteDTO;
import com.loja.loja.model.Cliente;
import com.loja.loja.repository.ClienteRepository;

@Service
public class ClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ModelMapper modelMapper;

    //Convert Model class to DTO class
    public ClienteDTO toClienteDTO(Cliente cliente) {
        return modelMapper.map(cliente, ClienteDTO.class);
    }

    public Integer addCliente(ClienteDTO dto) {
        clienteRepository.findByLogin(dto.getLogin()).map(cliente -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um cliente com este login.");
            //return Void.TYPE;
        });
        Cliente cliente = new Cliente();
        cliente.setLogin(dto.getLogin());
        cliente.setNome(dto.getNome());
        clienteRepository.save(cliente);
        return cliente.getId();
    }
}
