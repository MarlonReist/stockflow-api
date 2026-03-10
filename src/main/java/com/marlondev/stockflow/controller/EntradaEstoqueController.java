package com.marlondev.stockflow.controller;
import com.marlondev.stockflow.dto.EntradaEstoqueRequestDTO;
import com.marlondev.stockflow.dto.EntradaEstoqueResponseDTO;
import com.marlondev.stockflow.services.EntradaEstoqueService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/entrada_estoque")
public class EntradaEstoqueController {

    private final EntradaEstoqueService entradaEstoque;

    public EntradaEstoqueController(EntradaEstoqueService entradaEstoque) {
        this.entradaEstoque = entradaEstoque;
    }

    @PostMapping
    public ResponseEntity<EntradaEstoqueResponseDTO> salvar(@RequestBody @Valid EntradaEstoqueRequestDTO dto){
        EntradaEstoqueResponseDTO dtoSalvar = entradaEstoque.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoSalvar);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<EntradaEstoqueResponseDTO> buscarPorId(@PathVariable Long id) {
        EntradaEstoqueResponseDTO obj = entradaEstoque.buscarPorId(id);
        return ResponseEntity.ok(obj);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable Long id) {
        entradaEstoque.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<EntradaEstoqueResponseDTO>> listarTodos(){
        List<EntradaEstoqueResponseDTO> listDto = entradaEstoque.listarTodos();
        return ResponseEntity.ok().body(listDto);
    }

    @PatchMapping (value = "/{id}/finalizar")
    public ResponseEntity<EntradaEstoqueResponseDTO> finalizarEntrada(@PathVariable Long id) {
        EntradaEstoqueResponseDTO entradaFinalizada = entradaEstoque.finalizarEntrada(id);
        return ResponseEntity.ok().body(entradaFinalizada);
    }

    @PatchMapping (value = "/{id}/cancelar")
    public ResponseEntity<EntradaEstoqueResponseDTO> cancelarEntrada(@PathVariable Long id) {
        EntradaEstoqueResponseDTO entradaCancelada = entradaEstoque.cancelarEntrada(id);
        return ResponseEntity.ok().body(entradaCancelada);
    }
}
