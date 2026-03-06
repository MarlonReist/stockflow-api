package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.domain.Cliente;
import com.marlondev.stockflow.dto.ClienteRequestDTO;
import com.marlondev.stockflow.dto.ClienteResponseDTO;
import com.marlondev.stockflow.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService){
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> salvarCliente(@RequestBody @Valid ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getCpf());
        cliente.setTelefone(dto.getTelefone());
        cliente.setEmail(dto.getEmail());
        cliente.setEndereco(dto.getEndereco());
        ClienteResponseDTO dtoSalvar = clienteService.salvarCliente(cliente);
        return ResponseEntity.ok(dtoSalvar);
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
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getCpf());
        cliente.setEmail(dto.getEmail());
        cliente.setEndereco(dto.getEndereco());
        cliente.setTelefone(dto.getTelefone());
        ClienteResponseDTO dtoAtualizado = clienteService.atualizarCliente(cliente);
        return ResponseEntity.ok(dtoAtualizado);
    }



}
