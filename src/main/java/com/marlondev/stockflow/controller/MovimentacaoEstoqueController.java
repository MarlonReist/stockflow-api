package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.dto.MovimentacaoEstoqueResponseDTO;
import com.marlondev.stockflow.services.MovimentacaoEstoqueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/historico")
public class MovimentacaoEstoqueController {

    private final MovimentacaoEstoqueService movimentacaoEstoqueService;

    public MovimentacaoEstoqueController(MovimentacaoEstoqueService movimentacaoEstoqueService) {
        this.movimentacaoEstoqueService = movimentacaoEstoqueService;
    }

    @GetMapping (value = "/{id}")
    public ResponseEntity<MovimentacaoEstoqueResponseDTO> buscarPorId(@PathVariable Long id){
        MovimentacaoEstoqueResponseDTO dto = movimentacaoEstoqueService.buscarPorId(id);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping
    public ResponseEntity<List<MovimentacaoEstoqueResponseDTO>> listarTodos() {
        List<MovimentacaoEstoqueResponseDTO> listDto = movimentacaoEstoqueService.listarTodos();
        return ResponseEntity.ok().body(listDto);
    }
}