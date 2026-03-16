package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.dto.TransferenciaItemRequestDTO;
import com.marlondev.stockflow.dto.TransferenciaItemResponseDTO;
import com.marlondev.stockflow.services.TransferenciaItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/transferencia_itens")
public class TransferenciaItemController {

    private final TransferenciaItemService transferenciaItemService;

    public TransferenciaItemController(TransferenciaItemService transferenciaItemService){
        this.transferenciaItemService = transferenciaItemService;
    }

    @PostMapping
    public ResponseEntity<TransferenciaItemResponseDTO> salvar(@RequestBody @Valid TransferenciaItemRequestDTO dto) {
        TransferenciaItemResponseDTO dtoSalvar = transferenciaItemService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoSalvar);
    }

    @GetMapping (value = "/{id}")
    public ResponseEntity<TransferenciaItemResponseDTO> buscarPorId(@PathVariable Long id){
        TransferenciaItemResponseDTO dto = transferenciaItemService.buscarPorId(id);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping (value = "/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        transferenciaItemService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TransferenciaItemResponseDTO>> listarTodos() {
        List<TransferenciaItemResponseDTO> listDto = transferenciaItemService.listarTodos();
        return ResponseEntity.ok().body(listDto);
    }

    @PutMapping (value = "/{id}")
    public ResponseEntity<TransferenciaItemResponseDTO> atualizarTransferenciaItem(@PathVariable Long id, @RequestBody @Valid TransferenciaItemRequestDTO dto) {
        TransferenciaItemResponseDTO itemAtualizado = transferenciaItemService.atualizar(id, dto);
        return ResponseEntity.ok().body(itemAtualizado);
    }
}

