package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.dto.ClienteRequestDTO;
import com.marlondev.stockflow.dto.ClienteResponseDTO;
import com.marlondev.stockflow.dto.RoteadorPrincipalRequestDTO;
import com.marlondev.stockflow.dto.RoteadorPrincipalHistoricoResponseDTO;
import com.marlondev.stockflow.dto.ModeloRoteadorSugestaoResponseDTO;
import com.marlondev.stockflow.security.UsuarioDetails;
import com.marlondev.stockflow.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

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

    @GetMapping(value = "/roteador-principal/sugestoes")
    public ResponseEntity<List<ModeloRoteadorSugestaoResponseDTO>> buscarSugestoesRoteador(
            @RequestParam String q,
            @RequestParam(defaultValue = "10") Integer limite
    ) {
        return ResponseEntity.ok(clienteService.buscarSugestoesRoteador(q, limite));
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

    @PutMapping(value = "/{id}/roteador-principal")
    public ResponseEntity<ClienteResponseDTO> atualizarRoteadorPrincipal(
            @PathVariable Long id,
            @RequestBody @Valid RoteadorPrincipalRequestDTO dto,
            @AuthenticationPrincipal UsuarioDetails usuarioDetails
    ) {
        return ResponseEntity.ok(clienteService.atualizarRoteadorPrincipal(id, dto, usuarioDetails.getUsuario()));
    }

    @GetMapping(value = "/{id}/roteador-principal/historico")
    public ResponseEntity<List<RoteadorPrincipalHistoricoResponseDTO>> listarHistoricoRoteador(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.listarHistoricoRoteador(id));
    }
}
