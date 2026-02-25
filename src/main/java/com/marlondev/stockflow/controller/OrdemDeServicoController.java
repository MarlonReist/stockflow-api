package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.domain.Cliente;
import com.marlondev.stockflow.domain.Colaborador;
import com.marlondev.stockflow.domain.OrdemDeServico;
import com.marlondev.stockflow.services.ColaboradorService;
import com.marlondev.stockflow.services.OrdemDeServicoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/os")
public class OrdemDeServicoController {

    private final OrdemDeServicoService osService;

    public OrdemDeServicoController(OrdemDeServicoService osService) {
        this.osService = osService;
    }

    @PostMapping
    public ResponseEntity<Void> salvarOs(@RequestBody OrdemDeServico ordemDeServico){
        osService.salvarOs(ordemDeServico);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<OrdemDeServico> buscarPorId(@PathVariable Long id) {
        OrdemDeServico obj = osService.buscarPorId(id);
        return ResponseEntity.ok().body(obj);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deletarPorId(@PathVariable Long id) {
        osService.deletarOsPorId(id);
        return ResponseEntity.ok("Ordem de Serviço deletada!");
    }

    @GetMapping
    public ResponseEntity<List<OrdemDeServico>> listarTodos(){
        List<OrdemDeServico> list = osService.listarTodos();
        return ResponseEntity.ok().body(list);
    }

    @PutMapping (value = "/{id}/descricao")
    public ResponseEntity<Void> atualizarDescricao(@PathVariable Long id, @RequestBody OrdemDeServico ordemDeServico) {
        ordemDeServico.setId(id);
        osService.atualizarDescricao(ordemDeServico);
        return ResponseEntity.noContent().build();
    }

    @PutMapping (value = "/{id}/finalizar")
    public ResponseEntity<Void> finalizarOs(@PathVariable Long id, @RequestBody OrdemDeServico ordemDeServico) {
        ordemDeServico.setId(id);
        osService.finalizarOs(ordemDeServico);
        return ResponseEntity.noContent().build();
    }

    @PutMapping (value = "/{id}/cancelar")
    public ResponseEntity<Void> cancelarOs(@PathVariable Long id, @RequestBody OrdemDeServico ordemDeServico) {
        ordemDeServico.setId(id);
        osService.cancelarOs(ordemDeServico);
        return ResponseEntity.noContent().build();
    }
}
