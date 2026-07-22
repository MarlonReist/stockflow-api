package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.dto.AtivarConviteRequestDTO;
import com.marlondev.stockflow.dto.ConviteValidacaoResponseDTO;
import com.marlondev.stockflow.services.ConviteUsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/convites")
public class ConviteUsuarioController {

    private final ConviteUsuarioService conviteUsuarioService;

    public ConviteUsuarioController(ConviteUsuarioService conviteUsuarioService) {
        this.conviteUsuarioService = conviteUsuarioService;
    }

    @GetMapping(value = "/validar")
    public ResponseEntity<ConviteValidacaoResponseDTO> validarConvite(@RequestParam String token) {
        ConviteValidacaoResponseDTO response = conviteUsuarioService.validarConvite(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/ativar")
    public ResponseEntity<ConviteValidacaoResponseDTO> ativarConvite(@RequestBody @Valid AtivarConviteRequestDTO dto) {
        ConviteValidacaoResponseDTO response = conviteUsuarioService.ativarConvite(dto);
        return ResponseEntity.ok(response);
    }
}