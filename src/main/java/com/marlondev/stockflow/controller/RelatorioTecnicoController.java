package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.dto.*;
import com.marlondev.stockflow.services.PdfService;
import com.marlondev.stockflow.services.RelatorioTecnicoService;
import com.marlondev.stockflow.domain.enums.AgrupamentoRelatorioEnum;
import com.marlondev.stockflow.domain.enums.ParticipacaoRelatorioTecnicoEnum;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
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
    private final PdfService pdfService;

    public RelatorioTecnicoController(RelatorioTecnicoService relatorioTecnicoService, PdfService pdfService) {
        this.relatorioTecnicoService = relatorioTecnicoService;
        this.pdfService = pdfService;
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

    @GetMapping(value = "/os-realizadas-tempo")
    public ResponseEntity<List<RelatorioTecnicoSerieTemporalDTO>> buscarOsRealizadasAoLongoDoTempo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
            @RequestParam AgrupamentoRelatorioEnum agrupamento,
            @RequestParam(required = false) Long tecnicoId,
            @RequestParam(required = false) Long tipoOrdemServicoId
    ) {
        List<RelatorioTecnicoSerieTemporalDTO> dados =
                relatorioTecnicoService.buscarOsRealizadasAoLongoDoTempo(
                        dataInicial,
                        dataFinal,
                        agrupamento,
                        tecnicoId,
                        tipoOrdemServicoId
                );

        return ResponseEntity.ok().body(dados);
    }

    @GetMapping(value = "/os-por-tipo")
    public ResponseEntity<List<RelatorioTecnicoQuantidadePorTipoDTO>> buscarOsRealizadasPorTipo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
            @RequestParam(required = false) Long tecnicoId
    ) {
        List<RelatorioTecnicoQuantidadePorTipoDTO> dados =
                relatorioTecnicoService.buscarOsRealizadasPorTipo(
                        dataInicial,
                        dataFinal,
                        tecnicoId
                );
        return ResponseEntity.ok().body(dados);
    }

    @GetMapping(value = "/status-atual")
    public ResponseEntity<List<RelatorioTecnicoQuantidadePorStatusDTO>> buscarDistribuicaoAtualPorStatus() {
        List<RelatorioTecnicoQuantidadePorStatusDTO> dados =
                relatorioTecnicoService.buscarDistribuicaoAtualPorStatus();

        return ResponseEntity.ok().body(dados);
    }

    @GetMapping(value = "/atendimentos")
    public ResponseEntity<RelatorioTecnicoAtendimentoPaginaDTO> buscarAtendimentos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
            @RequestParam(required = false) Long tecnicoId,
            @RequestParam(defaultValue = "RESPONSAVEL") ParticipacaoRelatorioTecnicoEnum participacao,
            @RequestParam(required = false) Long tipoOrdemServicoId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size
    ) {
        RelatorioTecnicoAtendimentoPaginaDTO dados = relatorioTecnicoService.buscarAtendimentos(
                dataInicial,
                dataFinal,
                tecnicoId,
                participacao,
                tipoOrdemServicoId,
                page,
                size
        );

        return ResponseEntity.ok().body(dados);
    }

    @GetMapping(value = "/atendimentos/pdf")
    public ResponseEntity<byte[]> gerarAtendimentosPdf(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
            @RequestParam(required = false) Long tecnicoId,
            @RequestParam(defaultValue = "RESPONSAVEL") ParticipacaoRelatorioTecnicoEnum participacao,
            @RequestParam(required = false) Long tipoOrdemServicoId
    ) {
        byte[] pdf = pdfService.gerarRelatorioAtendimentosTecnicos(
                dataInicial,
                dataFinal,
                tecnicoId,
                participacao,
                tipoOrdemServicoId
        );

        return ResponseEntity.ok()
                .header("Content-Disposition", "inline; filename=relatorio-atendimentos-tecnicos-"
                        + dataInicial + "-" + dataFinal + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
