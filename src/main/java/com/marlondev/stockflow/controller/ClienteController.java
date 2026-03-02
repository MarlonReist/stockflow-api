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
@RequestMapping(value = "/cliente")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService){
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<Void> salvarCliente(@RequestBody @Valid ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getCpf());
        cliente.setTelefone(dto.getTelefone());
        cliente.setEmail(dto.getEmail());
        clienteService.salvarCliente(cliente);
        return ResponseEntity.ok().build();
    }

    @GetMapping (value = "/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id){
        Cliente obj = clienteService.buscarPorId(id);
        ClienteResponseDTO dto = new ClienteResponseDTO(obj);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping (value = "/{id}")
    public ResponseEntity<String> deletarClientePorId(@PathVariable Long id){
        clienteService.deletarClientePorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarTodos() {
        List<Cliente> list = clienteService.listarTodos();
        List<ClienteResponseDTO> listDto = list.stream().map(ClienteResponseDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PutMapping (value = "/{id}")
    public ResponseEntity<Void> atualizarCliente(@PathVariable Long id, @RequestBody @Valid ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getCpf());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
        clienteService.atualizarCliente(cliente);
        return ResponseEntity.noContent().build();
    }



}
