package com.loja.loja.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public void salvarPedido(PedidoDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getIdCliente()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Código do cliente inválido"));
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setData(LocalDateTime.now());
        pedido.setStatus("Em processo");
        pedido.setFormaPagamento(dto.getFormaPagamento());
        List<ItensPedido> itensPedido = salvarItensPedido(pedido, dto.getItensPedido());
        pedidoRepository.save(pedido);
        long quantidadeDeItens = itensPedido.stream().count();
            for(int c = 0; c < quantidadeDeItens; c++){
                long valorTotalDeItens = itensPedido.stream().mapToInt( i -> {
                    return i.getValor();
                }).sum();
                pedido.setValorTotal(valorTotalDeItens);
            }
        itensPedidoRepository.saveAll(itensPedido);
    }

    public List<ItensPedido> salvarItensPedido(Pedido pedido, List<ItensPedidoDTO> itens) {
        return itens.stream().map(dtoItens -> {
            Produto produto = produtoRepository.findById(dtoItens.getIdProduto()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Código do produto inválido"));
            ItensPedido itensPedido = new ItensPedido();
            itensPedido.setPedido(pedido);
            itensPedido.setProduto(produto);
            Integer quantidadeProdutoEstoque = produto.getQuantidade();
            Integer quantidadeProdutoPedido = dtoItens.getQuantidade();
                if(quantidadeProdutoEstoque >= quantidadeProdutoPedido) {
                Integer atualizarQuantidade = quantidadeProdutoEstoque - quantidadeProdutoPedido;
                Integer valorItens = produto.getValor() * dtoItens.getQuantidade();
                produto.setQuantidade(atualizarQuantidade);
                itensPedido.setValor(valorItens);
                itensPedido.setQuantidade(quantidadeProdutoPedido);
                produtoRepository.save(produto);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O produto não tem quantidade de estoque para quantidade de produto pedido. Verifique a quantidade disponível para venda deste produto.");
            }
            return itensPedido;
        }).collect(Collectors.toList());
    }

    public List<PedidoDTO> listarPedido(Pedido pedido){
        return pedidoRepository.findAll().stream().map(this::toPedidoDTO).collect(Collectors.toList());
    }

 
}
