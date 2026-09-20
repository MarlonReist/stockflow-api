package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.dto.ConferenciaEstoqueItemRequestDTO;
import com.marlondev.stockflow.dto.ConferenciaEstoqueItemResponseDTO;
import com.marlondev.stockflow.dto.ConferenciaEstoqueRequestDTO;
import com.marlondev.stockflow.dto.ConferenciaEstoqueResponseDTO;
import com.marlondev.stockflow.security.UsuarioDetails;
import com.marlondev.stockflow.services.ConferenciaEstoqueService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/conferencias-estoque")
public class ConferenciaEstoqueController {

    private final ConferenciaEstoqueService conferenciaEstoqueService;

    public ConferenciaEstoqueController(ConferenciaEstoqueService conferenciaEstoqueService) {
        this.conferenciaEstoqueService = conferenciaEstoqueService;
    }

    @PostMapping
    public ResponseEntity<ConferenciaEstoqueResponseDTO> iniciar(
            @RequestBody @Valid ConferenciaEstoqueRequestDTO dto,
            @AuthenticationPrincipal UsuarioDetails usuarioDetails
    ) {
        ConferenciaEstoqueResponseDTO response = conferenciaEstoqueService.iniciar(
                dto,
                usuarioDetails.getUsuario()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ConferenciaEstoqueResponseDTO> buscarPorId(@PathVariable Long id) {
        ConferenciaEstoqueResponseDTO response = conferenciaEstoqueService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/itens/{itemId}/contagem")
    public ResponseEntity<ConferenciaEstoqueItemResponseDTO> informarQuantidadeContada(
            @PathVariable Long itemId,
            @RequestBody @Valid ConferenciaEstoqueItemRequestDTO dto
    ) {
        ConferenciaEstoqueItemResponseDTO response = conferenciaEstoqueService.informarQuantidadeContada(itemId, dto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/{id}/finalizar")
    public ResponseEntity<ConferenciaEstoqueResponseDTO> finalizar(@PathVariable Long id) {
        ConferenciaEstoqueResponseDTO response = conferenciaEstoqueService.finalizar(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ConferenciaEstoqueResponseDTO>> listarTodos() {
        List<ConferenciaEstoqueResponseDTO> response = conferenciaEstoqueService.listarTodos();
        return ResponseEntity.ok(response);
    }
}