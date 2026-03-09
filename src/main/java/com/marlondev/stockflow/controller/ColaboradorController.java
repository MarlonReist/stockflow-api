package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.dto.*;
import com.marlondev.stockflow.services.ColaboradorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/colaboradores")
public class ColaboradorController {

    private final ColaboradorService colaboradorService;

    public ColaboradorController(ColaboradorService colaboradorService) {
        this.colaboradorService = colaboradorService;
    }

    @PostMapping
    public ResponseEntity<ColaboradorResponseDTO> salvarColaborador(@RequestBody @Valid ColaboradorRequestDTO dto) {
        ColaboradorResponseDTO dtoSalvar = colaboradorService.salvarColaborador(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoSalvar);
    }

    @GetMapping (value = "/{id}")
    public ResponseEntity<ColaboradorResponseDTO> buscarPorId(@PathVariable Long id){
        ColaboradorResponseDTO obj = colaboradorService.buscarPorId(id);
        return ResponseEntity.ok(obj);
    }

    @DeleteMapping (value = "/{id}")
    public ResponseEntity<Void> deletarColaboradorPorId(@PathVariable Long id){
        colaboradorService.deletarColaboradorPorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ColaboradorResponseDTO>> listarTodos() {
        List<ColaboradorResponseDTO> listDto = colaboradorService.listarTodos();
        return ResponseEntity.ok(listDto);
    }
    @PutMapping (value = "/{id}")
    public ResponseEntity<ColaboradorResponseDTO> atualizarColaborador(@PathVariable Long id, @RequestBody @Valid ColaboradorRequestDTO dto) {
        ColaboradorResponseDTO dtoAtualizado = colaboradorService.atualizarColaborador(id, dto);
        return ResponseEntity.ok(dtoAtualizado);
    }
}
