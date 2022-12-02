package com.loja.loja.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.loja.loja.DTO.ItensPedidoDTO;
import com.loja.loja.DTO.PedidoDTO;
import com.loja.loja.model.ItensPedido;
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

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePedido(@PathVariable Integer id, @RequestBody PedidoDTO dto) {
        pedidoService.updatePedido(id, dto);
    }

    @PatchMapping("/{id}/itens")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateItensPedido(@PathVariable Integer id, @RequestBody List<ItensPedido> itens) {
        pedidoService.updateItensPedido(id, itens);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePedido(@PathVariable Integer id) {
        pedidoService.deletePedido(id);
    }

    @GetMapping
    public List<PedidoDTO> listarPedido(Pedido pedido) {
        return pedidoService.listarPedido(pedido);
    }
}
