package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.dto.EntradaItemRequestDTO;
import com.marlondev.stockflow.dto.EntradaItemResponseDTO;
import com.marlondev.stockflow.services.EntradaItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/entrada_itens")
public class EntradaItemController {

    private final EntradaItemService entradaItemService;

    public EntradaItemController(EntradaItemService entradaItemService){
        this.entradaItemService = entradaItemService;
    }

    @PostMapping
    public ResponseEntity<EntradaItemResponseDTO> salvar(@RequestBody @Valid EntradaItemRequestDTO dto) {
        EntradaItemResponseDTO dtoSalvar = entradaItemService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoSalvar);
    }

    @GetMapping (value = "/{id}")
    public ResponseEntity<EntradaItemResponseDTO> buscarPorId(@PathVariable Long id){
        EntradaItemResponseDTO dto = entradaItemService.buscarPorId(id);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping (value = "/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        entradaItemService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<EntradaItemResponseDTO>> listarTodos() {
        List<EntradaItemResponseDTO> listDto = entradaItemService.listarTodos();
        return ResponseEntity.ok().body(listDto);
    }

    @PutMapping (value = "/{id}")
    public ResponseEntity<EntradaItemResponseDTO> atualizarEntradaItem(@PathVariable Long id, @RequestBody @Valid EntradaItemRequestDTO dto) {
        EntradaItemResponseDTO itemAtualizado = entradaItemService.atualizarEntrada(id, dto);
        return ResponseEntity.ok().body(itemAtualizado);
    }
}

