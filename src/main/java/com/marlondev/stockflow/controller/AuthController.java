package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.dto.LoginRequestDTO;
import com.marlondev.stockflow.dto.LoginResponseDTO;
import com.marlondev.stockflow.dto.EsqueciSenhaRequestDTO;
import com.marlondev.stockflow.dto.EsqueciSenhaResponseDTO;
import com.marlondev.stockflow.dto.RecuperacaoSenhaValidacaoResponseDTO;
import com.marlondev.stockflow.dto.RedefinirSenhaRequestDTO;
import com.marlondev.stockflow.dto.RedefinirSenhaResponseDTO;
import com.marlondev.stockflow.services.AuthService;
import com.marlondev.stockflow.services.RecuperacaoSenhaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private final AuthService authService;
    private final RecuperacaoSenhaService recuperacaoSenhaService;

    public AuthController(AuthService authService, RecuperacaoSenhaService recuperacaoSenhaService) {
        this.authService = authService;
        this.recuperacaoSenhaService = recuperacaoSenhaService;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {
        LoginResponseDTO response = authService.login(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/esqueci-senha")
    public ResponseEntity<EsqueciSenhaResponseDTO> esqueciSenha(@RequestBody @Valid EsqueciSenhaRequestDTO dto) {
        EsqueciSenhaResponseDTO response = recuperacaoSenhaService.solicitarRecuperacao(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/recuperacao-senha/validar")
    public ResponseEntity<RecuperacaoSenhaValidacaoResponseDTO> validarRecuperacaoSenha(@RequestParam String token) {
        RecuperacaoSenhaValidacaoResponseDTO response = recuperacaoSenhaService.validarToken(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/redefinir-senha")
    public ResponseEntity<RedefinirSenhaResponseDTO> redefinirSenha(@RequestBody @Valid RedefinirSenhaRequestDTO dto) {
        RedefinirSenhaResponseDTO response = recuperacaoSenhaService.redefinirSenha(dto);
        return ResponseEntity.ok(response);
    }
}
