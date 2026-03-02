package com.marlondev.stockflow.controller;
import com.marlondev.stockflow.domain.OrdemDeServico;
import com.marlondev.stockflow.dto.ColaboradorRequestDTO;
import com.marlondev.stockflow.dto.OrdemDeServicoRequestDTO;
import com.marlondev.stockflow.dto.OrdemDeServicoResponseDTO;
import com.marlondev.stockflow.services.OrdemDeServicoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/os")
public class OrdemDeServicoController {

    private final OrdemDeServicoService osService;

    public OrdemDeServicoController(OrdemDeServicoService osService) {
        this.osService = osService;
    }

    @PostMapping
    public ResponseEntity<Void> salvarOs(@RequestBody @Valid OrdemDeServicoRequestDTO dto){
        osService.salvarOs(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<OrdemDeServicoResponseDTO> buscarPorId(@PathVariable Long id) {
        OrdemDeServico obj = osService.buscarPorId(id);
        OrdemDeServicoResponseDTO dto = new OrdemDeServicoResponseDTO(obj);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deletarPorId(@PathVariable Long id) {
        osService.deletarOsPorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<OrdemDeServicoResponseDTO>> listarTodos(){
        List<OrdemDeServico> list = osService.listarTodos();
        List<OrdemDeServicoResponseDTO> listDto = list.stream().map(OrdemDeServicoResponseDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PutMapping (value = "/{id}/descricao")
    public ResponseEntity<Void> atualizarDescricao(@PathVariable Long id, @RequestBody @Valid OrdemDeServicoRequestDTO dto) {

        osService.atualizarDescricao(id, dto.getDescricao());
        return ResponseEntity.noContent().build();
    }

    @PutMapping (value = "/{id}/finalizar")
    public ResponseEntity<Void> finalizarOs(@PathVariable Long id) {
        osService.finalizarOs(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping (value = "/{id}/cancelar")
    public ResponseEntity<Void> cancelarOs(@PathVariable Long id) {
        osService.cancelarOs(id);
        return ResponseEntity.noContent().build();
    }
}
