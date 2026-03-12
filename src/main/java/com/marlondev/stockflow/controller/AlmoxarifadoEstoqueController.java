package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.dto.AlmoxarifadoEstoqueRequestDTO;
import com.marlondev.stockflow.dto.AlmoxarifadoEstoqueResponseDTO;
import com.marlondev.stockflow.services.AlmoxarifadoEstoqueService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/estoques")
public class AlmoxarifadoEstoqueController {

    private final AlmoxarifadoEstoqueService almoxarifadoEstoqueService;

    public AlmoxarifadoEstoqueController(AlmoxarifadoEstoqueService almoxarifadoEstoqueService){
        this.almoxarifadoEstoqueService = almoxarifadoEstoqueService;
    }

    @PostMapping
    public ResponseEntity<AlmoxarifadoEstoqueResponseDTO> salvar(@RequestBody @Valid AlmoxarifadoEstoqueRequestDTO dto) {
        AlmoxarifadoEstoqueResponseDTO dtoSalvar = almoxarifadoEstoqueService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoSalvar);
    }

    @GetMapping (value = "/{id}")
    public ResponseEntity<AlmoxarifadoEstoqueResponseDTO> buscarPorId(@PathVariable Long id){
        AlmoxarifadoEstoqueResponseDTO obj = almoxarifadoEstoqueService.buscarPorId(id);
        return ResponseEntity.ok(obj);
    }

    @DeleteMapping (value = "/{id}")
    public ResponseEntity<Void> deletarAlmoxarifadoEstoquePorId(@PathVariable Long id){
        almoxarifadoEstoqueService.deletarEstoque(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<AlmoxarifadoEstoqueResponseDTO>> listarTodos() {
        List<AlmoxarifadoEstoqueResponseDTO> listDto = almoxarifadoEstoqueService.listarTodos();
        return ResponseEntity.ok(listDto);
    }

    @PutMapping (value = "/{id}")
    public ResponseEntity<AlmoxarifadoEstoqueResponseDTO> atualizarAlmoxarifadoEstoque(@PathVariable Long id, @RequestBody @Valid AlmoxarifadoEstoqueRequestDTO dto) {
        AlmoxarifadoEstoqueResponseDTO almoxarifadoEstoqueAtualizado = almoxarifadoEstoqueService.atualizarEstoque(id, dto);
        return ResponseEntity.ok().body(almoxarifadoEstoqueAtualizado);
    }
}
