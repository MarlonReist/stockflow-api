package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.dto.ClienteRequestDTO;
import com.marlondev.stockflow.dto.ClienteResponseDTO;
import com.marlondev.stockflow.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ClienteResponseDTO> salvarCliente(@RequestBody @Valid ClienteRequestDTO dto) {
        ClienteResponseDTO dtoSalvar = clienteService.salvarCliente(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoSalvar);
    }

    @GetMapping (value = "/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id){
        ClienteResponseDTO obj = clienteService.buscarPorId(id);
        return ResponseEntity.ok(obj);
    }

    @DeleteMapping (value = "/{id}")
    public ResponseEntity<Void> deletarClientePorId(@PathVariable Long id){
        clienteService.deletarClientePorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarTodos() {
        List<ClienteResponseDTO> listDto = clienteService.listarTodos();
        return ResponseEntity.ok(listDto);
    }

    @PutMapping (value = "/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizarCliente(@PathVariable Long id, @RequestBody @Valid ClienteRequestDTO dto) {
        ClienteResponseDTO dtoAtualizado = clienteService.atualizarCliente(id, dto);
        return ResponseEntity.ok(dtoAtualizado);
    }



}
