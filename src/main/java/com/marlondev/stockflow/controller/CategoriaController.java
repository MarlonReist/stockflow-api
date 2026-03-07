package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.dto.CategoriaRequestDTO;
import com.marlondev.stockflow.dto.CategoriaResponseDTO;
import com.marlondev.stockflow.services.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService){
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> salvarCategoria(@RequestBody @Valid CategoriaRequestDTO dto) {
        CategoriaResponseDTO dtoSalvar = categoriaService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoSalvar);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoriaResponseDTO> buscarPorId(@PathVariable Long id) {
        CategoriaResponseDTO obj = categoriaService.buscarPorId(id);
        return ResponseEntity.ok(obj);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletarClientePorId(@PathVariable Long id) {
        categoriaService.deletarCategoriaPorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listarTodos() {
        List<CategoriaResponseDTO> listDto = categoriaService.listarTodos();
        return ResponseEntity.ok(listDto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CategoriaResponseDTO> atualizarCliente(@PathVariable Long id, @RequestBody @Valid CategoriaRequestDTO dto) {
        CategoriaResponseDTO dtoAtualizado = categoriaService.atualizarCategoria(id, dto);
        return ResponseEntity.ok(dtoAtualizado);

    }
}