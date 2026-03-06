package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.domain.Produto;
import com.marlondev.stockflow.dto.ProdutoRequestDTO;
import com.marlondev.stockflow.dto.ProdutoResponseDTO;
import com.marlondev.stockflow.services.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/produto")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService){
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<Void> salvarProduto(@RequestBody @Valid ProdutoRequestDTO dto) {
        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setPreco(dto.getPreco());
        produtoService.salvarProduto(produto);
        return ResponseEntity.ok().build();
    }

    @GetMapping (value = "/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id){
        Produto obj = produtoService.buscarPorId(id);
        ProdutoResponseDTO dto = new ProdutoResponseDTO(obj);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping (value = "/{id}")
    public ResponseEntity<String> deletarProdutoPorId(@PathVariable Long id){
        produtoService.deletarProdutoPorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listarTodos() {
        List<Produto> list = produtoService.listarTodos();
        List<ProdutoResponseDTO> listDto = list.stream().map(ProdutoResponseDTO:: new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PutMapping (value = "/{id}")
    public ResponseEntity<Void> atualizarProduto(@PathVariable Long id, @RequestBody @Valid ProdutoRequestDTO dto) {
        Produto produto = new Produto();
        produto = produtoService.buscarPorId(id);
        produto.setNome(dto.getNome());
        produto.setPreco(dto.getPreco());
        produtoService.atualizarProduto(produto);
        return ResponseEntity.noContent().build();
    }
}
