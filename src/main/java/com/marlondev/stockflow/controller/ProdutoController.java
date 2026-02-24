package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.domain.Produto;
import com.marlondev.stockflow.services.ProdutoService;
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
    public ResponseEntity<Void> salvarProduto(@RequestBody Produto produto) {
        produtoService.salvarProduto(produto);
        return ResponseEntity.ok().build();
    }

    @GetMapping (value = "/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id){
        Produto obj = produtoService.buscarPorId(id);
        return ResponseEntity.ok().body(obj);
    }

    @DeleteMapping (value = "/{id}")
    public ResponseEntity<String> deletarProdutoPorId(@PathVariable Long id){
        produtoService.deletarProdutoPorId(id);
        return ResponseEntity.ok("Produto deletado! ");
    }

    @GetMapping
    public ResponseEntity<List<Produto>> listarTodos() {
        List<Produto> list = produtoService.listarTodos();
        return ResponseEntity.ok().body(list);
    }

    @PutMapping (value = "/{id}")
    public ResponseEntity<Void> atualizarProduto(@PathVariable Long id, @RequestBody Produto produto) {
        produto.setId(id);
        produtoService.atualizarProduto(produto);
        return ResponseEntity.noContent().build();
    }



}
