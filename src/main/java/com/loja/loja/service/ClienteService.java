package com.loja.loja.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
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

    public String updateCliente(Integer id, ClienteDTO dto) {
        clienteRepository.findById(id).map(cliente -> {
            
            if(dto.getLogin() != null && !dto.getLogin().equals(cliente.getLogin())) cliente.setLogin(dto.getLogin());
            if(dto.getNome() != null && !dto.getNome().equals(cliente.getNome())) cliente.setNome(dto.getNome());

            return clienteRepository.save(cliente);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Código do cliente inválido."));

        return "Seus dados foram atualizados";
    }

    public String deleteCliente(Integer id) {
        clienteRepository.findById(id).map(cliente -> {
            clienteRepository.deleteById(id);
            return cliente;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Código do cliente inválido"));

        return "Cliente " +id+ " deletado!";
    }

    public List<ClienteDTO> getCliente(Cliente cliente) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING);
        Example<Cliente> c = Example.of(cliente, matcher);
        return clienteRepository.findAll(c).stream().map(this::toClienteDTO).toList();
    } 
}
