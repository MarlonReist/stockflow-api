package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.dto.AjusteConferenciaRequestDTO;
import com.marlondev.stockflow.dto.AjusteEstoqueRequestDTO;
import com.marlondev.stockflow.dto.AjusteEstoqueResponseDTO;
import com.marlondev.stockflow.security.UsuarioDetails;
import com.marlondev.stockflow.services.AjusteEstoqueService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/ajustes-estoque")
public class AjusteEstoqueController {

    private final AjusteEstoqueService ajusteEstoqueService;

    public AjusteEstoqueController(AjusteEstoqueService ajusteEstoqueService) {
        this.ajusteEstoqueService = ajusteEstoqueService;
    }

    @PostMapping
    public ResponseEntity<AjusteEstoqueResponseDTO> ajustarManual(
            @RequestBody @Valid AjusteEstoqueRequestDTO dto,
            @AuthenticationPrincipal UsuarioDetails usuarioDetails
    ) {
        AjusteEstoqueResponseDTO response = ajusteEstoqueService.ajustarManual(
                dto,
                usuarioDetails.getUsuario()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(value = "/conferencia-itens/{itemId}")
    public ResponseEntity<AjusteEstoqueResponseDTO> ajustarPorConferencia(
            @PathVariable Long itemId,
            @RequestBody @Valid AjusteConferenciaRequestDTO dto,
            @AuthenticationPrincipal UsuarioDetails usuarioDetails
    ) {
        AjusteEstoqueResponseDTO response = ajusteEstoqueService.ajustarPorConferencia(
                itemId,
                dto,
                usuarioDetails.getUsuario()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AjusteEstoqueResponseDTO> buscarPorId(@PathVariable Long id) {
        AjusteEstoqueResponseDTO response = ajusteEstoqueService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<AjusteEstoqueResponseDTO>> listarTodos() {
        List<AjusteEstoqueResponseDTO> response = ajusteEstoqueService.listarTodos();
        return ResponseEntity.ok(response);
    }
}