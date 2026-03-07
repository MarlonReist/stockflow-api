package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.dto.FornecedorRequestDTO;
import com.marlondev.stockflow.dto.FornecedorResponseDTO;
import com.marlondev.stockflow.services.FornecedorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/fornecedores")
public class FornecedorController {

    private final FornecedorService fornecedorService;

    public FornecedorController(FornecedorService fornecedorService){
        this.fornecedorService = fornecedorService;
    }

    @PostMapping
    public ResponseEntity<FornecedorResponseDTO> salvarFornecedor(@RequestBody @Valid FornecedorRequestDTO dto) {
        FornecedorResponseDTO dtoSalvar = fornecedorService.salvarFornecedor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoSalvar);
    }

    @GetMapping (value = "/{id}")
    public ResponseEntity<FornecedorResponseDTO> buscarPorId(@PathVariable Long id){
        FornecedorResponseDTO obj = fornecedorService.buscarPorId(id);
        return ResponseEntity.ok(obj);
    }

    @DeleteMapping (value = "/{id}")
    public ResponseEntity<Void> deletarFornecedorPorId(@PathVariable Long id){
        fornecedorService.deletarFornecedorPorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<FornecedorResponseDTO>> listarTodos() {
        List<FornecedorResponseDTO> listDto = fornecedorService.listarTodos();
        return ResponseEntity.ok(listDto);
    }
    @PutMapping (value = "/{id}")
    public ResponseEntity<FornecedorResponseDTO> atualizarFornecedor(@PathVariable Long id, @RequestBody @Valid FornecedorRequestDTO dto) {
        FornecedorResponseDTO dtoAtualizado = fornecedorService.atualizarFornecedor(id, dto);
        return ResponseEntity.ok(dtoAtualizado);
    }
}
