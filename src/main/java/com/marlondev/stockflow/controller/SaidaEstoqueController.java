package com.marlondev.stockflow.controller;
import com.marlondev.stockflow.dto.SaidaEstoqueRequestDTO;
import com.marlondev.stockflow.dto.SaidaEstoqueResponseDTO;
import com.marlondev.stockflow.services.SaidaEstoqueService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/saida_estoque")
public class SaidaEstoqueController {

    private final SaidaEstoqueService saidaEstoque;

    public SaidaEstoqueController(SaidaEstoqueService saidaEstoque) {
        this.saidaEstoque = saidaEstoque;
    }

    @PostMapping
    public ResponseEntity<SaidaEstoqueResponseDTO> salvar(@RequestBody @Valid SaidaEstoqueRequestDTO dto){
        SaidaEstoqueResponseDTO dtoSalvar = saidaEstoque.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoSalvar);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<SaidaEstoqueResponseDTO> buscarPorId(@PathVariable Long id) {
        SaidaEstoqueResponseDTO obj = saidaEstoque.buscarPorId(id);
        return ResponseEntity.ok(obj);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable Long id) {
        saidaEstoque.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<SaidaEstoqueResponseDTO>> listarTodos(){
        List<SaidaEstoqueResponseDTO> listDto = saidaEstoque.listarTodos();
        return ResponseEntity.ok().body(listDto);
    }

    @PatchMapping (value = "/{id}/finalizar")
    public ResponseEntity<SaidaEstoqueResponseDTO> finalizarSaida(@PathVariable Long id) {
        SaidaEstoqueResponseDTO saidaFinalizada = saidaEstoque.finalizarSaida(id);
        return ResponseEntity.ok().body(saidaFinalizada);
    }

    @PatchMapping (value = "/{id}/cancelar")
    public ResponseEntity<SaidaEstoqueResponseDTO> cancelarSaida(@PathVariable Long id) {
        SaidaEstoqueResponseDTO saidaCancelada = saidaEstoque.cancelarSaida(id);
        return ResponseEntity.ok().body(saidaCancelada);
    }
}
