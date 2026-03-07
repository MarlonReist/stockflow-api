package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.dto.AlmoxarifadoRequestDTO;
import com.marlondev.stockflow.dto.AlmoxarifadoResponseDTO;
import com.marlondev.stockflow.services.AlmoxarifadoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/almoxarifados")
public class AlmoxarifadoController {

    private final AlmoxarifadoService almoxarifadoService;

    public AlmoxarifadoController(AlmoxarifadoService almoxarifadoService){
        this.almoxarifadoService = almoxarifadoService;
    }

    @PostMapping
    public ResponseEntity<AlmoxarifadoResponseDTO> salvarAlmoxarifado(@RequestBody @Valid AlmoxarifadoRequestDTO dto) {
        AlmoxarifadoResponseDTO dtoSalvar = almoxarifadoService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoSalvar);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<AlmoxarifadoResponseDTO> buscarPorId(@PathVariable Long id) {
        AlmoxarifadoResponseDTO obj = almoxarifadoService.buscarPorId(id);
        return ResponseEntity.ok(obj);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletarAlmoxarifadoPorId(@PathVariable Long id) {
        almoxarifadoService.deletarAlmoxarifadoPorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<AlmoxarifadoResponseDTO>> listarTodos() {
        List<AlmoxarifadoResponseDTO> listDto = almoxarifadoService.listarTodos();
        return ResponseEntity.ok(listDto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<AlmoxarifadoResponseDTO> atualizarAlmoxarifado(@PathVariable Long id, @RequestBody @Valid AlmoxarifadoRequestDTO dto) {
        AlmoxarifadoResponseDTO dtoAtualizado = almoxarifadoService.atualizarAlmoxarifado(id, dto);
        return ResponseEntity.ok(dtoAtualizado);
    }
}