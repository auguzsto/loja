package com.loja.loja.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.loja.loja.DTO.ItensPedidoDTO;
import com.loja.loja.DTO.PedidoDTO;
import com.loja.loja.model.Cliente;
import com.loja.loja.model.ItensPedido;
import com.loja.loja.model.Pedido;
import com.loja.loja.model.Produto;
import com.loja.loja.repository.ClienteRepository;
import com.loja.loja.repository.ItensPedidoRepository;
import com.loja.loja.repository.PedidoRepository;
import com.loja.loja.repository.ProdutoRepository;

@Service
public class PedidoService {
    
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ItensPedidoRepository itensPedidoRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    //Convert Model class to DTO class.
    public PedidoDTO toPedidoDTO(Pedido pedido) {
        return modelMapper.map(pedido, PedidoDTO.class);
    }
    //Convert Model class to DTO class.
    public ItensPedidoDTO toItensPedidoDTO(ItensPedido itensPedido) {
        return modelMapper.map(itensPedido, ItensPedidoDTO.class);
    }

    
    public void salvarPedido(PedidoDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getIdCliente()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Código do cliente inválido"));
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        List<ItensPedido> itensPedido = salvarItensPedido(pedido, dto.getItensPedido());
        pedidoRepository.save(pedido);
        itensPedidoRepository.saveAll(itensPedido);
    }

    public List<ItensPedido> salvarItensPedido(Pedido pedido, List<ItensPedidoDTO> itens) {
        return itens.stream().map(dtoItens -> {
            Produto produto = produtoRepository.findById(dtoItens.getIdProduto()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Código do produto inválido"));
            ItensPedido itensPedido = new ItensPedido();
            itensPedido.setPedido(pedido);
            itensPedido.setProduto(produto);
            itensPedido.setQuantidade(dtoItens.getQuantidadeProduto());
            return itensPedido;            
        }).collect(Collectors.toList());
    }

    public List<PedidoDTO> listarPedido(Pedido pedido){
        return pedidoRepository.findAll().stream().map(this::toPedidoDTO).collect(Collectors.toList());
    }

 
}
