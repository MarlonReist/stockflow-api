package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.domain.Colaborador;
import com.marlondev.stockflow.services.ColaboradorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/colaboradores")
public class ColaboradorController {

    private final ColaboradorService colaboradorService;

    public ColaboradorController(ColaboradorService colaboradorService) {
        this.colaboradorService = colaboradorService;
    }

    @PostMapping
    public ResponseEntity<Void> salvarColaborador(@RequestBody Colaborador colaborador){
        colaboradorService.salvarColaborador(colaborador);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Colaborador> buscarPorId(@PathVariable Long id) {
        Colaborador obj = colaboradorService.buscarPorId(id);
        return ResponseEntity.ok().body(obj);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deletarPorId(@PathVariable Long id) {
        colaboradorService.deletarColaboradorPorId(id);
        return ResponseEntity.ok("Colaborador deletado!");
    }

    @GetMapping
    public ResponseEntity<List<Colaborador>> listarTodos(){
        List<Colaborador> list = colaboradorService.listarTodos();
        return ResponseEntity.ok().body(list);
    }

    @PutMapping (value = "/{id}")
    public ResponseEntity<Void> atualizarColaborador(@PathVariable Long id, @RequestBody Colaborador colaborador) {
        colaborador.setId(id);
        colaboradorService.atualizarColaborador(colaborador);
        return ResponseEntity.noContent().build();
    }
}
