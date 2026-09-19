package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.dto.TipoOrdemServicoRequestDTO;
import com.marlondev.stockflow.dto.TipoOrdemServicoResponseDTO;
import com.marlondev.stockflow.services.TipoOrdemServicoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/tipos-os")
public class TipoOrdemServicoController {

    private final TipoOrdemServicoService tipoOrdemServicoService;

    public TipoOrdemServicoController(TipoOrdemServicoService tipoOrdemServicoService) {
        this.tipoOrdemServicoService = tipoOrdemServicoService;
    }

    @PostMapping
    public ResponseEntity<TipoOrdemServicoResponseDTO> salvar(@RequestBody @Valid TipoOrdemServicoRequestDTO dto) {
        TipoOrdemServicoResponseDTO tipoSalvo = tipoOrdemServicoService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoSalvo);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TipoOrdemServicoResponseDTO> buscarPorId(@PathVariable Long id) {
        TipoOrdemServicoResponseDTO tipo = tipoOrdemServicoService.buscarPorId(id);
        return ResponseEntity.ok(tipo);
    }

    @GetMapping
    public ResponseEntity<List<TipoOrdemServicoResponseDTO>> listarTodos() {
        List<TipoOrdemServicoResponseDTO> tipos = tipoOrdemServicoService.listarTodos();
        return ResponseEntity.ok(tipos);
    }

    @GetMapping(value = "/ativos")
    public ResponseEntity<List<TipoOrdemServicoResponseDTO>> listarAtivos() {
        List<TipoOrdemServicoResponseDTO> tipos = tipoOrdemServicoService.listarAtivos();
        return ResponseEntity.ok(tipos);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<TipoOrdemServicoResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid TipoOrdemServicoRequestDTO dto
    ) {
        TipoOrdemServicoResponseDTO tipoAtualizado = tipoOrdemServicoService.atualizar(id, dto);
        return ResponseEntity.ok(tipoAtualizado);
    }

    @PatchMapping(value = "/{id}/ativar")
    public ResponseEntity<TipoOrdemServicoResponseDTO> ativar(@PathVariable Long id) {
        TipoOrdemServicoResponseDTO tipoAtivado = tipoOrdemServicoService.ativar(id);
        return ResponseEntity.ok(tipoAtivado);
    }

    @PatchMapping(value = "/{id}/desativar")
    public ResponseEntity<TipoOrdemServicoResponseDTO> desativar(@PathVariable Long id) {
        TipoOrdemServicoResponseDTO tipoDesativado = tipoOrdemServicoService.desativar(id);
        return ResponseEntity.ok(tipoDesativado);
    }
}