package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.dto.AlterarSenhaRequestDTO;
import com.marlondev.stockflow.dto.AlterarSenhaResponseDTO;
import com.marlondev.stockflow.dto.MeuPerfilResponseDTO;
import com.marlondev.stockflow.security.UsuarioDetails;
import com.marlondev.stockflow.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/perfil")
public class PerfilController {

    private final UsuarioService usuarioService;

    public PerfilController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<MeuPerfilResponseDTO> buscarMeuPerfil(
            @AuthenticationPrincipal UsuarioDetails usuarioDetails
    ) {
        MeuPerfilResponseDTO response = usuarioService.buscarMeuPerfil(usuarioDetails.getUsuario());
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/senha")
    public ResponseEntity<AlterarSenhaResponseDTO> alterarMinhaSenha(
            @AuthenticationPrincipal UsuarioDetails usuarioDetails,
            @RequestBody @Valid AlterarSenhaRequestDTO dto
    ) {
        AlterarSenhaResponseDTO response = usuarioService.alterarMinhaSenha(
                usuarioDetails.getUsuario(),
                dto
        );

        return ResponseEntity.ok(response);
    }
}