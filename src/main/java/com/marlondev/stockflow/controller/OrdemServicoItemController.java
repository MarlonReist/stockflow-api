package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.dto.OrdemServicoItemRequestDTO;
import com.marlondev.stockflow.dto.OrdemServicoItemResponseDTO;
import com.marlondev.stockflow.services.OrdemServicoItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/itens")
public class OrdemServicoItemController {

    private final OrdemServicoItemService orderItemService;

    public OrdemServicoItemController(OrdemServicoItemService orderItemService){
        this.orderItemService = orderItemService;
    }

    @PostMapping
    public ResponseEntity<OrdemServicoItemResponseDTO> salvar(@RequestBody @Valid OrdemServicoItemRequestDTO dto) {
        OrdemServicoItemResponseDTO dtoSalvar = orderItemService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoSalvar);
    }

    @GetMapping (value = "/{id}")
    public ResponseEntity<OrdemServicoItemResponseDTO> buscarPorId(@PathVariable Long id){
        OrdemServicoItemResponseDTO dto = orderItemService.buscarPorId(id);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping (value = "/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        orderItemService.deletarOrdemServicoItemPorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<OrdemServicoItemResponseDTO>> listarTodos() {
        List<OrdemServicoItemResponseDTO> listDto = orderItemService.listarTodos();
        return ResponseEntity.ok().body(listDto);
    }

    @PutMapping (value = "/{id}")
    public ResponseEntity<OrdemServicoItemResponseDTO> atualizarOrdemItem(@PathVariable Long id, @RequestBody @Valid OrdemServicoItemRequestDTO dto) {
        OrdemServicoItemResponseDTO itemAtualizado = orderItemService.atualizarOrdemItem(id, dto);
        return ResponseEntity.ok().body(itemAtualizado);
    }
}

