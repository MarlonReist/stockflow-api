package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.dto.RelatorioTecnicoQuantidadePorTecnicoDTO;
import com.marlondev.stockflow.dto.RelatorioTecnicoResumoDTO;
import com.marlondev.stockflow.services.RelatorioTecnicoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/relatorios-tecnicos")
public class RelatorioTecnicoController {

    private final RelatorioTecnicoService relatorioTecnicoService;

    public RelatorioTecnicoController(RelatorioTecnicoService relatorioTecnicoService) {
        this.relatorioTecnicoService = relatorioTecnicoService;
    }

    @GetMapping(value = "/resumo")
    public ResponseEntity<RelatorioTecnicoResumoDTO> buscarResumo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
            @RequestParam(required = false) Long tecnicoId,
            @RequestParam(required = false) Long tipoOrdemServicoId
    ) {
        RelatorioTecnicoResumoDTO resumo = relatorioTecnicoService.buscarResumo(
                dataInicial,
                dataFinal,
                tecnicoId,
                tipoOrdemServicoId
        );

        return ResponseEntity.ok().body(resumo);
    }

    @GetMapping(value = "/os-por-tecnico")
    public ResponseEntity<List<RelatorioTecnicoQuantidadePorTecnicoDTO>> buscarOsPorTecnicoResponsavel(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
            @RequestParam(required = false) Long tecnicoId,
            @RequestParam(required = false) Long tipoOrdemServicoId
    ) {
        List<RelatorioTecnicoQuantidadePorTecnicoDTO> dados =
                relatorioTecnicoService.buscarOsRealizadasPorTecnicoResponsavel(
                        dataInicial,
                        dataFinal,
                        tecnicoId,
                        tipoOrdemServicoId
                );

        return ResponseEntity.ok().body(dados);
    }

    @GetMapping(value = "/participacoes-ajudante")
    public ResponseEntity<List<RelatorioTecnicoQuantidadePorTecnicoDTO>> buscarParticipacoesPorTecnicoAjudante(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
            @RequestParam(required = false) Long tecnicoId,
            @RequestParam(required = false) Long tipoOrdemServicoId
    ) {
        List<RelatorioTecnicoQuantidadePorTecnicoDTO> dados =
                relatorioTecnicoService.buscarParticipacoesPorTecnicoAjudante(
                        dataInicial,
                        dataFinal,
                        tecnicoId,
                        tipoOrdemServicoId
                );

        return ResponseEntity.ok().body(dados);
    }
}
