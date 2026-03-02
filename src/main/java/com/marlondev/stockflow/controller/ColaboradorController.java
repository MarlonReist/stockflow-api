package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.domain.Colaborador;
import com.marlondev.stockflow.dto.ClienteResponseDTO;
import com.marlondev.stockflow.dto.ColaboradorRequestDTO;
import com.marlondev.stockflow.dto.ColaboradorResponseDTO;
import com.marlondev.stockflow.services.ColaboradorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/colaborador")
public class ColaboradorController {

    private final ColaboradorService colaboradorService;

    public ColaboradorController(ColaboradorService colaboradorService) {
        this.colaboradorService = colaboradorService;
    }

    @PostMapping
    public ResponseEntity<Void> salvarColaborador(@RequestBody @Valid ColaboradorRequestDTO dto){
        Colaborador colaborador = new Colaborador();
        colaborador.setNome(dto.getNome());
        colaborador.setCargo(dto.getCargo());
        colaborador.setCpf(dto.getCpf());
        colaborador.setTelefone(dto.getTelefone());
        colaboradorService.salvarColaborador(colaborador);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ColaboradorResponseDTO> buscarPorId(@PathVariable Long id) {
        Colaborador obj = colaboradorService.buscarPorId(id);
        ColaboradorResponseDTO dto = new ColaboradorResponseDTO(obj);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deletarPorId(@PathVariable Long id) {
        colaboradorService.deletarColaboradorPorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ColaboradorResponseDTO>> listarTodos(){
        List<Colaborador> list = colaboradorService.listarTodos();
        List<ColaboradorResponseDTO> listDto = list.stream().map(ColaboradorResponseDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PutMapping (value = "/{id}")
    public ResponseEntity<Void> atualizarColaborador(@PathVariable Long id, @RequestBody @Valid ColaboradorRequestDTO dto) {
        Colaborador colaborador = colaboradorService.buscarPorId(id);
        colaborador.setNome(dto.getNome());
        colaborador.setCpf(dto.getCpf());
        colaborador.setCargo(dto.getCargo());
        colaborador.setTelefone(dto.getTelefone());
        colaboradorService.atualizarColaborador(colaborador);
        return ResponseEntity.noContent().build();
    }
}
