package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.dto.UsuarioRequestDTO;
import com.marlondev.stockflow.dto.UsuarioResponseDTO;
import com.marlondev.stockflow.services.UsuarioService;
import com.marlondev.stockflow.dto.ConviteUsuarioResponseDTO;
import com.marlondev.stockflow.services.ConviteUsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final ConviteUsuarioService conviteUsuarioService;

    public UsuarioController(UsuarioService usuarioService, ConviteUsuarioService conviteUsuarioService) {
        this.usuarioService = usuarioService;
        this.conviteUsuarioService = conviteUsuarioService;
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

    @PutMapping (value = "/{id}/bloquear")
    public ResponseEntity<UsuarioResponseDTO> bloquearUsuario(@PathVariable Long id){
        UsuarioResponseDTO bloquearUsuario = usuarioService.bloquearUsuario(id);
        return ResponseEntity.ok().body(bloquearUsuario);
    }

    @PutMapping (value = "/{id}/desbloquear")
    public ResponseEntity<UsuarioResponseDTO> desbloquearUsuario(@PathVariable Long id){
        UsuarioResponseDTO usuarioDesbloqueado = usuarioService.desbloquearUsuario(id);
        return ResponseEntity.ok().body(usuarioDesbloqueado);
    }

    @PutMapping (value = "/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(@PathVariable Long id, @RequestBody @Valid UsuarioRequestDTO dto) {
        UsuarioResponseDTO usuarioAtualizado = usuarioService.atualizarUsuario(id, dto);
        return ResponseEntity.ok().body(usuarioAtualizado);
    }

    @PostMapping(value = "/convites")
    public ResponseEntity<ConviteUsuarioResponseDTO> convidarUsuario(@RequestBody @Valid UsuarioRequestDTO dto) {
        ConviteUsuarioResponseDTO convite = conviteUsuarioService.convidarUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(convite);
    }
}
