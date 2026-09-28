package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.dto.OrdemDeServicoConclusaoAtendimentoRequestDTO;
import com.marlondev.stockflow.dto.OrdemDeServicoResponseDTO;
import com.marlondev.stockflow.dto.TecnicoOrdemServicoDetalheDTO;
import com.marlondev.stockflow.dto.TecnicoOrdemServicoResumoDTO;
import com.marlondev.stockflow.security.UsuarioDetails;
import com.marlondev.stockflow.services.TecnicoOrdemServicoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/tecnico")
public class TecnicoOrdemServicoController {

    private final TecnicoOrdemServicoService tecnicoOrdemServicoService;

    public TecnicoOrdemServicoController(TecnicoOrdemServicoService tecnicoOrdemServicoService) {
        this.tecnicoOrdemServicoService = tecnicoOrdemServicoService;
    }

    @GetMapping(value = "/minhas-os")
    public ResponseEntity<List<TecnicoOrdemServicoResumoDTO>> listarMinhasOrdens(
            @AuthenticationPrincipal UsuarioDetails usuarioDetails
    ) {
        List<TecnicoOrdemServicoResumoDTO> ordens =
                tecnicoOrdemServicoService.listarMinhasOrdens(usuarioDetails.getUsuario());

        return ResponseEntity.ok().body(ordens);
    }

    @GetMapping(value = "/os/{id}")
    public ResponseEntity<TecnicoOrdemServicoDetalheDTO> buscarMinhaOrdemPorId(
            @AuthenticationPrincipal UsuarioDetails usuarioDetails,
            @PathVariable Long id
    ) {
        TecnicoOrdemServicoDetalheDTO ordem =
                tecnicoOrdemServicoService.buscarMinhaOrdemPorId(usuarioDetails.getUsuario(), id);

        return ResponseEntity.ok().body(ordem);
    }

    @PatchMapping(value = "/os/{id}/iniciar")
    public ResponseEntity<OrdemDeServicoResponseDTO> iniciarAtendimento(
            @AuthenticationPrincipal UsuarioDetails usuarioDetails,
            @PathVariable Long id
    ) {
        OrdemDeServicoResponseDTO ordem =
                tecnicoOrdemServicoService.iniciarAtendimento(usuarioDetails.getUsuario(), id);

        return ResponseEntity.ok().body(ordem);
    }

    @PatchMapping(value = "/os/{id}/concluir-atendimento")
    public ResponseEntity<OrdemDeServicoResponseDTO> concluirAtendimento(
            @AuthenticationPrincipal UsuarioDetails usuarioDetails,
            @PathVariable Long id,
            @RequestBody @Valid OrdemDeServicoConclusaoAtendimentoRequestDTO dto
    ) {
        OrdemDeServicoResponseDTO ordem =
                tecnicoOrdemServicoService.concluirAtendimento(usuarioDetails.getUsuario(), id, dto);

        return ResponseEntity.ok().body(ordem);
    }
}