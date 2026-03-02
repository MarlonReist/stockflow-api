/*package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.domain.OrdemServicoItem;
import com.marlondev.stockflow.dto.OrdemServicoItemRequestDTO;
import com.marlondev.stockflow.dto.OrdemServicoItemResponseDTO;
import com.marlondev.stockflow.services.OrdemServicoItemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/item")
public class OrdemServicoItemController {

    private final OrdemServicoItemService orderItemService;

    public OrdemServicoItemController(OrdemServicoItemService orderItemService){
        this.orderItemService = orderItemService;
    }

    @PostMapping
    public ResponseEntity<Void> salvar(@RequestBody @Valid OrdemServicoItemRequestDTO dto) {
        orderItemService.salvar(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping (value = "/{id}")
    public ResponseEntity<OrdemServicoItemResponseDTO> buscarPorId(@PathVariable Long id){
        OrdemServicoItem obj = orderItemService.buscarPorId(id);
        OrdemServicoItemResponseDTO dto = new OrdemServicoItemResponseDTO(obj);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping (value = "/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id){
        orderItemService.deletarOrdemServicoItemPorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<OrdemServicoItemResponseDTO>> listarTodos() {
        List<OrdemServicoItem> list = orderItemService.listarTodos();
        List<OrdemServicoItemResponseDTO> listDto = list.stream().map(OrdemServicoItemResponseDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
/*
    @PutMapping (value = "/{item_id}")
    public ResponseEntity<Void> atualizarProduto(@PathVariable Long id, @RequestBody OrdemServicoItem orderItem) {
        orderItem.setId(id);
        orderItemService.
        return ResponseEntity.noContent().build();
    }



}
*/
