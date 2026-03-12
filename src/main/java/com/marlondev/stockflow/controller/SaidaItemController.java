package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.dto.SaidaItemRequestDTO;
import com.marlondev.stockflow.dto.SaidaItemResponseDTO;
import com.marlondev.stockflow.services.SaidaItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/saida_itens")
public class SaidaItemController {

    private final SaidaItemService saidaItemService;

    public SaidaItemController(SaidaItemService saidaItemService){
        this.saidaItemService = saidaItemService;
    }

    @PostMapping
    public ResponseEntity<SaidaItemResponseDTO> salvar(@RequestBody @Valid SaidaItemRequestDTO dto) {
        SaidaItemResponseDTO dtoSalvar = saidaItemService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoSalvar);
    }

    @GetMapping (value = "/{id}")
    public ResponseEntity<SaidaItemResponseDTO> buscarPorId(@PathVariable Long id){
        SaidaItemResponseDTO dto = saidaItemService.buscarPorId(id);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping (value = "/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        saidaItemService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<SaidaItemResponseDTO>> listarTodos() {
        List<SaidaItemResponseDTO> listDto = saidaItemService.listarTodos();
        return ResponseEntity.ok().body(listDto);
    }

    @PutMapping (value = "/{id}")
    public ResponseEntity<SaidaItemResponseDTO> atualizarSaidaItem(@PathVariable Long id, @RequestBody @Valid SaidaItemRequestDTO dto) {
        SaidaItemResponseDTO itemAtualizado = saidaItemService.atualizarSaidaItem(id, dto);
        return ResponseEntity.ok().body(itemAtualizado);
    }
}

