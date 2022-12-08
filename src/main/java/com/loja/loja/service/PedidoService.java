package com.loja.loja.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
        Cliente cliente = clienteRepository.findById(dto.getIdCliente()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Código do cliente inválido"));
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setData(LocalDateTime.now());
        pedido.setStatus("Em processo");
        pedido.setFormaPagamento(dto.getFormaPagamento());
        List<ItensPedido> itensPedido = salvarItensPedido(pedido, dto.getItensPedido());
        pedido.setValorTotal(calcularValorTotalNovoPedido(itensPedido));
        pedidoRepository.save(pedido);
        itensPedidoRepository.saveAll(itensPedido);
    }

    @Transactional
    public List<ItensPedido> salvarItensPedido(Pedido pedido, List<ItensPedidoDTO> itens) {
        return itens.stream().map(dtoItens -> {
            Produto produto = produtoRepository.findById(dtoItens.getIdProduto()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Código do produto inválido"));
            ItensPedido itensPedido = new ItensPedido();
            itensPedido.setPedido(pedido);
            itensPedido.setProduto(produto);
            produto.setQuantidade(verificarDisponibilidadeDoProduto(produto.getQuantidade(), dtoItens.getQuantidade(), produto.getId()));
            itensPedido.setValor(calcularNovoValorItens(produto.getValor(), dtoItens.getQuantidade()));
            itensPedido.setQuantidade(dtoItens.getQuantidade());
            produtoRepository.save(produto);
            return itensPedido;
        }).collect(Collectors.toList());
    }

    public void updatePedido(Integer id, PedidoDTO dto) {
        pedidoRepository.findById(id).map(pedido -> {

            if(dto.getStatus() != null && !pedido.getStatus().equals(dto.getStatus())) pedido.setStatus(dto.getStatus());
            if(dto.getStatus().equals("Finalizado")) pedido.setDataEntrega(LocalDateTime.now());
            if(dto.getFormaPagamento() != null && !pedido.getFormaPagamento().equals(dto.getFormaPagamento())) pedido.setFormaPagamento(dto.getFormaPagamento());
            return pedidoRepository.save(pedido);

        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Código do pedido inválido"));

    }

    public List<ItensPedido> updateItensPedido(Integer idPedido, ItensPedidoDTO dtoItem, Integer id) {
        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Código do pedido inválido."));
        itensPedidoRepository.findById(id).stream().map(itensPedido -> {
            Produto produto = itensPedido.getProduto();
            if(produto.getQuantidade() >= dtoItem.getQuantidade()) {
                itensPedido.setValor(calcularValorItens(produto.getValor(), dtoItem.getQuantidade(), itensPedido.getQuantidade()));
                produto.setQuantidade(calcularNovaQuantidadeProduto(produto.getQuantidade(), dtoItem.getQuantidade()));
                itensPedido.setQuantidade(quantidadeDeProdutoPedido(dtoItem.getQuantidade(), itensPedido.getQuantidade()));
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não há esta quantidade no estoque");
            }
            pedido.setValorTotal(calcularValorTotalPedido(pedido));
            return itensPedidoRepository.save(itensPedido);
        }).collect(Collectors.toList());

        return pedido.getItensPedido();
    }

    public void deletePedido(Integer id) {
        pedidoRepository.findById(id).map(pedido -> {
            pedidoRepository.deleteById(id);
            return pedido;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Código do pedido inválido."));
    }

    public List<PedidoDTO> listarPedido(Pedido pedido){
        return pedidoRepository.findAll().stream().map(this::toPedidoDTO).collect(Collectors.toList());
    }

    //Operational functions.
    public BigDecimal calcularValorItens(BigDecimal valor, Integer quantidade, Integer quatidadeNoPedido) {
        if(quantidade < 0) {
            BigDecimal valorItensTotal = valor.multiply(new BigDecimal(quatidadeNoPedido - Math.abs(quantidade)));
            return valorItensTotal;
        } else {
            BigDecimal valorItensTotal = valor.multiply(new BigDecimal(quatidadeNoPedido + quantidade));
            return valorItensTotal;
        }
    }

    public BigDecimal calcularNovoValorItens(BigDecimal valor, Integer quantidade) {
       return valor.multiply(new BigDecimal(quantidade));
    }

    public BigDecimal calcularValorTotalPedido(Pedido pedido) {
        BigDecimal valorTotalPedido = new BigDecimal("0.2");
        long quantidade = pedido.getItensPedido().stream().count();
        for(int c = 0; c < quantidade; c++) {
            valorTotalPedido = pedido.getItensPedido().stream().map(valor -> {
                return valor.getValor();
            }).reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        return valorTotalPedido;
    }

    public BigDecimal calcularValorTotalNovoPedido(List<ItensPedido> i) {
        BigDecimal valorTotalNovoPedido = new BigDecimal("0.2");
        long quantidade = i.stream().count();
        for(int c = 0; c < quantidade; c++) {
            valorTotalNovoPedido = i.stream().map(valor -> {
                return valor.getValor();
            }).reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        return  valorTotalNovoPedido;
    }

    public Integer verificarDisponibilidadeDoProduto(Integer quantidadeDeProdutoNoEstoque, Integer quantidadeDeProdutoPedido, Integer idProduto) {
        if(quantidadeDeProdutoNoEstoque >= quantidadeDeProdutoPedido) {
            return quantidadeDeProdutoNoEstoque - quantidadeDeProdutoPedido;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não há esta quantidade do produto "+idProduto+" disponível no estoque");
        }
    }

    public Integer calcularNovaQuantidadeProduto(Integer quantidadeProduto, Integer quantidadeProdutoPedido) {
        return quantidadeProduto - quantidadeProdutoPedido;
    }

    public Integer quantidadeDeProdutoPedido(Integer quantidadeProdutoPedido, Integer quantidadeNoPedido) {
        if(quantidadeProdutoPedido < 0 ) {
            return quantidadeNoPedido - Math.abs(quantidadeProdutoPedido);
        } else {
            return quantidadeNoPedido + quantidadeProdutoPedido;
        }
    }
}