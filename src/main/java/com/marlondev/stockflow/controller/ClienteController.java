package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.domain.Cliente;
import com.marlondev.stockflow.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService){
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<Void> salvarCliente(@RequestBody Cliente cliente) {
        clienteService.salvarCliente(cliente);
        return ResponseEntity.ok().build();
    }

    @GetMapping (value = "/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id){
        Cliente obj = clienteService.buscarPorId(id);
        return ResponseEntity.ok().body(obj);
    }

    @DeleteMapping (value = "/{id}")
    public ResponseEntity<String> deletarClientePorId(@PathVariable Long id){
        clienteService.deletarClientePorId(id);
        return ResponseEntity.ok("Cliente deletado! ");
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodos() {
        List<Cliente> list = clienteService.listarTodos();
        return ResponseEntity.ok().body(list);
    }

    @PutMapping (value = "/{id}")
    public ResponseEntity<Void> atualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        cliente.setId(id);
        clienteService.atualizarCliente(cliente);
        return ResponseEntity.noContent().build();
    }



}
