package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.dto.UsuarioRequestDTO;
import com.marlondev.stockflow.dto.UsuarioResponseDTO;
import com.marlondev.stockflow.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> salvarUsuario(@RequestBody @Valid UsuarioRequestDTO dto) {
        UsuarioResponseDTO dtoSalvar = usuarioService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoSalvar);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id){
        UsuarioResponseDTO obj = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(obj);
    }

    @DeleteMapping (value = "/{id}")
    public ResponseEntity<Void> deletarUsuarioPorId(@PathVariable Long id){
        usuarioService.deletarUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
        List<UsuarioResponseDTO> listDto = usuarioService.listarTodos();
        return ResponseEntity.ok(listDto);
    }

    @PutMapping (value = "/{id}/desativar")
    public ResponseEntity<UsuarioResponseDTO> desativarUsuario(@PathVariable Long id){
        UsuarioResponseDTO usuarioDesativado = usuarioService.desativarUsuario(id);
        return ResponseEntity.ok().body(usuarioDesativado);
    }

    @PutMapping (value = "/{id}/ativar")
    public ResponseEntity<UsuarioResponseDTO> ativarUsuario(@PathVariable Long id){
        UsuarioResponseDTO usuarioAtivado = usuarioService.ativarUsuario(id);
        return ResponseEntity.ok().body(usuarioAtivado);
    }

    @PutMapping (value = "/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(@PathVariable Long id, @RequestBody @Valid UsuarioRequestDTO dto) {
        UsuarioResponseDTO usuarioAtualizado = usuarioService.atualizarUsuario(id, dto);
        return ResponseEntity.ok().body(usuarioAtualizado);
    }
}
