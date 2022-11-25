package com.loja.loja.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.loja.loja.DTO.ItensPedidoDTO;
import com.loja.loja.DTO.PedidoDTO;
import com.loja.loja.model.Pedido;
import com.loja.loja.service.PedidoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void savePedido(@RequestBody PedidoDTO dto) {
        pedidoService.salvarPedido(dto);
    }

    @GetMapping
    public List<PedidoDTO> listarPedido(Pedido pedido) {
        return pedidoService.listarPedido(pedido);
    }
}
