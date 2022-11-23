package com.loja.loja.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;

import com.loja.loja.DTO.VendedorDTO;
import com.loja.loja.model.Vendedor;
import com.loja.loja.repository.VendedorRepository;

@Service
public class VendedorService {
    
    @Autowired
    private VendedorRepository vendedorRepository;

    @Autowired
    private ModelMapper modelMapper;

    //Convert Model class to DTO class
    public VendedorDTO toVendedorDTO(Vendedor vendedor) {
        return modelMapper.map(vendedor, VendedorDTO.class);
    }

    public List<VendedorDTO> getVendedor(Vendedor vendedor) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING);
        Example<Vendedor> v = Example.of(vendedor, matcher);
        return vendedorRepository.findAll(v).stream().map(this::toVendedorDTO).toList();
    }

    public void addVendedor(VendedorDTO dto) {
        Vendedor vendedor = new Vendedor();
        vendedor.setNome(dto.getNome());
        vendedor.setCpf(dto.getCpf());
        vendedorRepository.save(vendedor);
    }
}
