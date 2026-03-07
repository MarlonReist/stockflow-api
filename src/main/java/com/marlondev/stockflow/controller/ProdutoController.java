package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.dto.ProdutoRequestDTO;
import com.marlondev.stockflow.dto.ProdutoResponseDTO;
import com.marlondev.stockflow.services.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService){
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> salvarProduto(@RequestBody @Valid ProdutoRequestDTO dto) {
        ProdutoResponseDTO dtoSalvar = produtoService.salvarProduto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoSalvar);
    }

    @GetMapping (value = "/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id){
        ProdutoResponseDTO obj = produtoService.buscarPorId(id);
        return ResponseEntity.ok(obj);
    }

    @DeleteMapping (value = "/{id}")
    public ResponseEntity<Void> deletarProdutoPorId(@PathVariable Long id){
        produtoService.deletarProdutoPorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listarTodos() {
        List<ProdutoResponseDTO> listDto = produtoService.listarTodos();
        return ResponseEntity.ok(listDto);
    }

    @PutMapping (value = "/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizarProduto(@PathVariable Long id, @RequestBody @Valid ProdutoRequestDTO dto) {
        ProdutoResponseDTO produtoAtualizado = produtoService.atualizarProduto(id, dto);
        return ResponseEntity.ok().body(produtoAtualizado);
    }
}
