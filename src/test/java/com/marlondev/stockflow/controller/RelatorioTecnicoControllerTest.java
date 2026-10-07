package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.domain.enums.AgrupamentoRelatorioEnum;
import com.marlondev.stockflow.domain.enums.StatusEnum;
import com.marlondev.stockflow.dto.RelatorioTecnicoQuantidadePorStatusDTO;
import com.marlondev.stockflow.dto.RelatorioTecnicoQuantidadePorTecnicoDTO;
import com.marlondev.stockflow.dto.RelatorioTecnicoQuantidadePorTipoDTO;
import com.marlondev.stockflow.dto.RelatorioTecnicoResumoDTO;
import com.marlondev.stockflow.dto.RelatorioTecnicoSerieTemporalDTO;
import com.marlondev.stockflow.services.RelatorioTecnicoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RelatorioTecnicoControllerTest {

    @Mock
    private RelatorioTecnicoService relatorioTecnicoService;

    private RelatorioTecnicoController controller;

    @BeforeEach
    void setUp() {
        controller = new RelatorioTecnicoController(relatorioTecnicoService);
    }

    @Test
    void buscarResumoDeveRetornarResumoDoService() {
        LocalDate dataInicial = LocalDate.of(2026, 10, 1);
        LocalDate dataFinal = LocalDate.of(2026, 10, 7);
        Long tecnicoId = 10L;
        Long tipoOrdemServicoId = 20L;
        RelatorioTecnicoResumoDTO esperado = new RelatorioTecnicoResumoDTO(8L, 5L, 1L, 2L, 3600L, 3L);

        when(relatorioTecnicoService.buscarResumo(dataInicial, dataFinal, tecnicoId, tipoOrdemServicoId))
                .thenReturn(esperado);

        ResponseEntity<RelatorioTecnicoResumoDTO> response =
                controller.buscarResumo(dataInicial, dataFinal, tecnicoId, tipoOrdemServicoId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(esperado, response.getBody());
        verify(relatorioTecnicoService).buscarResumo(dataInicial, dataFinal, tecnicoId, tipoOrdemServicoId);
    }

    @Test
    void buscarOsPorTecnicoResponsavelDeveRetornarDadosDoService() {
        LocalDate dataInicial = LocalDate.of(2026, 10, 1);
        LocalDate dataFinal = LocalDate.of(2026, 10, 7);
        List<RelatorioTecnicoQuantidadePorTecnicoDTO> esperado = List.of(
                new RelatorioTecnicoQuantidadePorTecnicoDTO(1L, "João", 27L)
        );

        when(relatorioTecnicoService.buscarOsRealizadasPorTecnicoResponsavel(dataInicial, dataFinal, null, 5L))
                .thenReturn(esperado);

        ResponseEntity<List<RelatorioTecnicoQuantidadePorTecnicoDTO>> response =
                controller.buscarOsPorTecnicoResponsavel(dataInicial, dataFinal, null, 5L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(esperado, response.getBody());
        verify(relatorioTecnicoService).buscarOsRealizadasPorTecnicoResponsavel(dataInicial, dataFinal, null, 5L);
    }

    @Test
    void buscarParticipacoesPorTecnicoAjudanteDeveRetornarDadosDoService() {
        LocalDate dataInicial = LocalDate.of(2026, 10, 1);
        LocalDate dataFinal = LocalDate.of(2026, 10, 7);
        List<RelatorioTecnicoQuantidadePorTecnicoDTO> esperado = List.of(
                new RelatorioTecnicoQuantidadePorTecnicoDTO(2L, "Carlos", 9L)
        );

        when(relatorioTecnicoService.buscarParticipacoesPorTecnicoAjudante(dataInicial, dataFinal, 2L, null))
                .thenReturn(esperado);

        ResponseEntity<List<RelatorioTecnicoQuantidadePorTecnicoDTO>> response =
                controller.buscarParticipacoesPorTecnicoAjudante(dataInicial, dataFinal, 2L, null);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(esperado, response.getBody());
        verify(relatorioTecnicoService).buscarParticipacoesPorTecnicoAjudante(dataInicial, dataFinal, 2L, null);
    }

    @Test
    void buscarOsRealizadasAoLongoDoTempoDeveRetornarSerieTemporalDoService() {
        LocalDate dataInicial = LocalDate.of(2026, 10, 1);
        LocalDate dataFinal = LocalDate.of(2026, 10, 7);
        List<RelatorioTecnicoSerieTemporalDTO> esperado = List.of(
                new RelatorioTecnicoSerieTemporalDTO("2026-10-01", 3L)
        );

        when(relatorioTecnicoService.buscarOsRealizadasAoLongoDoTempo(
                dataInicial,
                dataFinal,
                AgrupamentoRelatorioEnum.DIA,
                10L,
                20L
        )).thenReturn(esperado);

        ResponseEntity<List<RelatorioTecnicoSerieTemporalDTO>> response =
                controller.buscarOsRealizadasAoLongoDoTempo(
                        dataInicial,
                        dataFinal,
                        AgrupamentoRelatorioEnum.DIA,
                        10L,
                        20L
                );

        assertEquals(200, response.getStatusCode().value());
        assertEquals(esperado, response.getBody());
        verify(relatorioTecnicoService).buscarOsRealizadasAoLongoDoTempo(
                dataInicial,
                dataFinal,
                AgrupamentoRelatorioEnum.DIA,
                10L,
                20L
        );
    }

    @Test
    void buscarOsRealizadasPorTipoDeveRetornarDistribuicaoPorTipoDoService() {
        LocalDate dataInicial = LocalDate.of(2026, 10, 1);
        LocalDate dataFinal = LocalDate.of(2026, 10, 7);
        List<RelatorioTecnicoQuantidadePorTipoDTO> esperado = List.of(
                new RelatorioTecnicoQuantidadePorTipoDTO(1L, "Instalação", 40L)
        );

        when(relatorioTecnicoService.buscarOsRealizadasPorTipo(dataInicial, dataFinal, 10L))
                .thenReturn(esperado);

        ResponseEntity<List<RelatorioTecnicoQuantidadePorTipoDTO>> response =
                controller.buscarOsRealizadasPorTipo(dataInicial, dataFinal, 10L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(esperado, response.getBody());
        verify(relatorioTecnicoService).buscarOsRealizadasPorTipo(dataInicial, dataFinal, 10L);
    }

    @Test
    void buscarDistribuicaoAtualPorStatusDeveRetornarStatusDoService() {
        List<RelatorioTecnicoQuantidadePorStatusDTO> esperado = List.of(
                new RelatorioTecnicoQuantidadePorStatusDTO(StatusEnum.ABERTA, 5L),
                new RelatorioTecnicoQuantidadePorStatusDTO(StatusEnum.AGENDADA, 12L)
        );

        when(relatorioTecnicoService.buscarDistribuicaoAtualPorStatus()).thenReturn(esperado);

        ResponseEntity<List<RelatorioTecnicoQuantidadePorStatusDTO>> response =
                controller.buscarDistribuicaoAtualPorStatus();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(esperado, response.getBody());
        verify(relatorioTecnicoService).buscarDistribuicaoAtualPorStatus();
    }
}
