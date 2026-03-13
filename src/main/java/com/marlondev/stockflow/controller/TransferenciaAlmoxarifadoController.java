package com.marlondev.stockflow.controller;
import com.marlondev.stockflow.dto.TransferenciaAlmoxarifadoRequestDTO;
import com.marlondev.stockflow.dto.TransferenciaAlmoxarifadoResponseDTO;
import com.marlondev.stockflow.services.TransferenciaAlmoxarifadoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/transferencia_almoxarifado")
public class TransferenciaAlmoxarifadoController {

    private final TransferenciaAlmoxarifadoService transferenciaAlmoxarifado;

    public TransferenciaAlmoxarifadoController(TransferenciaAlmoxarifadoService transferenciaAlmoxarifado) {
        this.transferenciaAlmoxarifado = transferenciaAlmoxarifado;
    }

    @PostMapping
    public ResponseEntity<TransferenciaAlmoxarifadoResponseDTO> salvar(@RequestBody @Valid TransferenciaAlmoxarifadoRequestDTO dto){
        TransferenciaAlmoxarifadoResponseDTO dtoSalvar = transferenciaAlmoxarifado.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoSalvar);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TransferenciaAlmoxarifadoResponseDTO> buscarPorId(@PathVariable Long id) {
        TransferenciaAlmoxarifadoResponseDTO obj = transferenciaAlmoxarifado.buscarPorId(id);
        return ResponseEntity.ok(obj);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable Long id) {
        transferenciaAlmoxarifado.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TransferenciaAlmoxarifadoResponseDTO>> listarTodos(){
        List<TransferenciaAlmoxarifadoResponseDTO> listDto = transferenciaAlmoxarifado.listarTodos();
        return ResponseEntity.ok().body(listDto);
    }
}
