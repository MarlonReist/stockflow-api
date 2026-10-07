package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.enums.StatusEnum;
import com.marlondev.stockflow.domain.enums.AgrupamentoRelatorioEnum;
import com.marlondev.stockflow.dto.RelatorioTecnicoQuantidadePorStatusDTO;
import com.marlondev.stockflow.dto.RelatorioTecnicoQuantidadePorTecnicoDTO;
import com.marlondev.stockflow.dto.RelatorioTecnicoQuantidadePorTipoDTO;
import com.marlondev.stockflow.dto.RelatorioTecnicoResumoDTO;
import com.marlondev.stockflow.dto.RelatorioTecnicoSerieTemporalDTO;
import com.marlondev.stockflow.repositories.OrdemDeServicoRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RelatorioTecnicoServiceTest {

    @Mock
    private OrdemDeServicoRepository ordemDeServicoRepository;

    private RelatorioTecnicoService service;

    @BeforeEach
    void setUp() {
        service = new RelatorioTecnicoService(ordemDeServicoRepository);
    }

    @Test
    void buscarResumoDeveUsarPeriodoCompletoEFiltrosInformados() {
        LocalDate dataInicial = LocalDate.of(2026, 10, 1);
        LocalDate dataFinal = LocalDate.of(2026, 10, 6);
        LocalDateTime inicio = LocalDateTime.of(2026, 10, 1, 0, 0);
        LocalDateTime fimExclusivo = LocalDateTime.of(2026, 10, 7, 0, 0);
        Long tecnicoId = 10L;
        Long tipoOrdemServicoId = 20L;

        when(ordemDeServicoRepository.contarRealizadasPorPeriodo(inicio, fimExclusivo, tecnicoId, tipoOrdemServicoId))
                .thenReturn(8L);
        when(ordemDeServicoRepository.contarFechadasPorPeriodoEStatus(inicio, fimExclusivo, StatusEnum.FINALIZADA, tecnicoId, tipoOrdemServicoId))
                .thenReturn(5L);
        when(ordemDeServicoRepository.contarFechadasPorPeriodoEStatus(inicio, fimExclusivo, StatusEnum.CANCELADA, tecnicoId, tipoOrdemServicoId))
                .thenReturn(1L);
        when(ordemDeServicoRepository.contarRealizadasPorPeriodoEStatusAtual(inicio, fimExclusivo, StatusEnum.AGUARDANDO_CONFERENCIA, tecnicoId, tipoOrdemServicoId))
                .thenReturn(2L);
        when(ordemDeServicoRepository.calcularTempoMedioAtendimentoSegundos(inicio, fimExclusivo, tecnicoId, tipoOrdemServicoId))
                .thenReturn(3600L);
        when(ordemDeServicoRepository.contarTecnicosComAtendimentoPorPeriodo(inicio, fimExclusivo, tecnicoId, tipoOrdemServicoId))
                .thenReturn(1L);

        RelatorioTecnicoResumoDTO resumo = service.buscarResumo(
                dataInicial,
                dataFinal,
                tecnicoId,
                tipoOrdemServicoId
        );

        assertEquals(8L, resumo.getTotalOsRealizadas());
        assertEquals(5L, resumo.getTotalOsFinalizadas());
        assertEquals(1L, resumo.getTotalOsCanceladas());
        assertEquals(2L, resumo.getTotalAguardandoConferencia());
        assertEquals(3600L, resumo.getTempoMedioAtendimentoSegundos());
        assertEquals(1L, resumo.getQuantidadeTecnicosComAtendimento());

        verify(ordemDeServicoRepository).contarRealizadasPorPeriodo(inicio, fimExclusivo, tecnicoId, tipoOrdemServicoId);
        verify(ordemDeServicoRepository).contarFechadasPorPeriodoEStatus(inicio, fimExclusivo, StatusEnum.FINALIZADA, tecnicoId, tipoOrdemServicoId);
        verify(ordemDeServicoRepository).contarFechadasPorPeriodoEStatus(inicio, fimExclusivo, StatusEnum.CANCELADA, tecnicoId, tipoOrdemServicoId);
        verify(ordemDeServicoRepository).contarRealizadasPorPeriodoEStatusAtual(inicio, fimExclusivo, StatusEnum.AGUARDANDO_CONFERENCIA, tecnicoId, tipoOrdemServicoId);
        verify(ordemDeServicoRepository).calcularTempoMedioAtendimentoSegundos(inicio, fimExclusivo, tecnicoId, tipoOrdemServicoId);
        verify(ordemDeServicoRepository).contarTecnicosComAtendimentoPorPeriodo(inicio, fimExclusivo, tecnicoId, tipoOrdemServicoId);
    }

    @Test
    void buscarResumoDeveRetornarZerosQuandoNaoHouverDados() {
        LocalDate dataInicial = LocalDate.of(2026, 10, 1);
        LocalDate dataFinal = LocalDate.of(2026, 10, 1);
        LocalDateTime inicio = LocalDateTime.of(2026, 10, 1, 0, 0);
        LocalDateTime fimExclusivo = LocalDateTime.of(2026, 10, 2, 0, 0);

        when(ordemDeServicoRepository.contarRealizadasPorPeriodo(inicio, fimExclusivo, null, null))
                .thenReturn(0L);
        when(ordemDeServicoRepository.contarFechadasPorPeriodoEStatus(inicio, fimExclusivo, StatusEnum.FINALIZADA, null, null))
                .thenReturn(0L);
        when(ordemDeServicoRepository.contarFechadasPorPeriodoEStatus(inicio, fimExclusivo, StatusEnum.CANCELADA, null, null))
                .thenReturn(0L);
        when(ordemDeServicoRepository.contarRealizadasPorPeriodoEStatusAtual(inicio, fimExclusivo, StatusEnum.AGUARDANDO_CONFERENCIA, null, null))
                .thenReturn(0L);
        when(ordemDeServicoRepository.calcularTempoMedioAtendimentoSegundos(inicio, fimExclusivo, null, null))
                .thenReturn(0L);
        when(ordemDeServicoRepository.contarTecnicosComAtendimentoPorPeriodo(inicio, fimExclusivo, null, null))
                .thenReturn(0L);

        RelatorioTecnicoResumoDTO resumo = service.buscarResumo(dataInicial, dataFinal, null, null);

        assertEquals(0L, resumo.getTotalOsRealizadas());
        assertEquals(0L, resumo.getTotalOsFinalizadas());
        assertEquals(0L, resumo.getTotalOsCanceladas());
        assertEquals(0L, resumo.getTotalAguardandoConferencia());
        assertEquals(0L, resumo.getTempoMedioAtendimentoSegundos());
        assertEquals(0L, resumo.getQuantidadeTecnicosComAtendimento());
    }

    @Test
    void buscarResumoDeveBloquearPeriodoIncompleto() {
        assertThrows(DatabaseException.class, () -> service.buscarResumo(
                LocalDate.of(2026, 10, 1),
                null,
                null,
                null
        ));
    }

    @Test
    void buscarResumoDeveBloquearDataInicialMaiorQueFinal() {
        assertThrows(DatabaseException.class, () -> service.buscarResumo(
                LocalDate.of(2026, 10, 6),
                LocalDate.of(2026, 10, 1),
                null,
                null
        ));
    }

    @Test
    void buscarOsRealizadasPorTecnicoResponsavelDeveRetornarRankingOrdenadoPeloRepository() {
        LocalDate dataInicial = LocalDate.of(2026, 10, 1);
        LocalDate dataFinal = LocalDate.of(2026, 10, 6);
        LocalDateTime inicio = LocalDateTime.of(2026, 10, 1, 0, 0);
        LocalDateTime fimExclusivo = LocalDateTime.of(2026, 10, 7, 0, 0);

        List<RelatorioTecnicoQuantidadePorTecnicoDTO> esperado = List.of(
                new RelatorioTecnicoQuantidadePorTecnicoDTO(1L, "Jo\u00e3o", 27L),
                new RelatorioTecnicoQuantidadePorTecnicoDTO(2L, "Carlos", 21L)
        );

        when(ordemDeServicoRepository.contarRealizadasPorTecnicoResponsavel(inicio, fimExclusivo, null, 5L))
                .thenReturn(esperado);

        List<RelatorioTecnicoQuantidadePorTecnicoDTO> resultado =
                service.buscarOsRealizadasPorTecnicoResponsavel(dataInicial, dataFinal, null, 5L);

        assertEquals(esperado, resultado);
        verify(ordemDeServicoRepository).contarRealizadasPorTecnicoResponsavel(inicio, fimExclusivo, null, 5L);
    }

    @Test
    void buscarParticipacoesPorTecnicoAjudanteDeveRetornarRankingSeparadoDoResponsavel() {
        LocalDate dataInicial = LocalDate.of(2026, 10, 1);
        LocalDate dataFinal = LocalDate.of(2026, 10, 6);
        LocalDateTime inicio = LocalDateTime.of(2026, 10, 1, 0, 0);
        LocalDateTime fimExclusivo = LocalDateTime.of(2026, 10, 7, 0, 0);

        List<RelatorioTecnicoQuantidadePorTecnicoDTO> esperado = List.of(
                new RelatorioTecnicoQuantidadePorTecnicoDTO(3L, "Pedro", 12L),
                new RelatorioTecnicoQuantidadePorTecnicoDTO(4L, "Marcos", 7L)
        );

        when(ordemDeServicoRepository.contarParticipacoesPorTecnicoAjudante(inicio, fimExclusivo, null, null))
                .thenReturn(esperado);

        List<RelatorioTecnicoQuantidadePorTecnicoDTO> resultado =
                service.buscarParticipacoesPorTecnicoAjudante(dataInicial, dataFinal, null, null);

        assertEquals(esperado, resultado);
        verify(ordemDeServicoRepository).contarParticipacoesPorTecnicoAjudante(inicio, fimExclusivo, null, null);
    }

    @Test
    void buscarOsRealizadasAoLongoDoTempoDeveAgruparPorDia() {
        LocalDate dataInicial = LocalDate.of(2026, 10, 1);
        LocalDate dataFinal = LocalDate.of(2026, 10, 6);
        LocalDateTime inicio = LocalDateTime.of(2026, 10, 1, 0, 0);
        LocalDateTime fimExclusivo = LocalDateTime.of(2026, 10, 7, 0, 0);
        Long tecnicoId = 10L;
        Long tipoOrdemServicoId = 20L;

        List<Object[]> dadosRepository = List.of(
                new Object[]{"2026-10-01", 3L},
                new Object[]{"2026-10-02", 5L}
        );

        when(ordemDeServicoRepository.contarRealizadasPorDia(inicio, fimExclusivo, tecnicoId, tipoOrdemServicoId))
                .thenReturn(dadosRepository);

        List<RelatorioTecnicoSerieTemporalDTO> resultado =
                service.buscarOsRealizadasAoLongoDoTempo(
                        dataInicial,
                        dataFinal,
                        AgrupamentoRelatorioEnum.DIA,
                        tecnicoId,
                        tipoOrdemServicoId
                );

        assertEquals(2, resultado.size());
        assertEquals("2026-10-01", resultado.get(0).getPeriodo());
        assertEquals(3L, resultado.get(0).getQuantidade());
        assertEquals("2026-10-02", resultado.get(1).getPeriodo());
        assertEquals(5L, resultado.get(1).getQuantidade());

        verify(ordemDeServicoRepository).contarRealizadasPorDia(inicio, fimExclusivo, tecnicoId, tipoOrdemServicoId);
    }

    @Test
    void buscarOsRealizadasAoLongoDoTempoDeveAgruparPorSemana() {
        LocalDate dataInicial = LocalDate.of(2026, 10, 1);
        LocalDate dataFinal = LocalDate.of(2026, 10, 31);
        LocalDateTime inicio = LocalDateTime.of(2026, 10, 1, 0, 0);
        LocalDateTime fimExclusivo = LocalDateTime.of(2026, 11, 1, 0, 0);

        List<Object[]> dadosRepository = List.of(
                new Object[]{"2026-W40", 9L},
                new Object[]{"2026-W41", 12L}
        );

        when(ordemDeServicoRepository.contarRealizadasPorSemana(inicio, fimExclusivo, null, null))
                .thenReturn(dadosRepository);

        List<RelatorioTecnicoSerieTemporalDTO> resultado =
                service.buscarOsRealizadasAoLongoDoTempo(
                        dataInicial,
                        dataFinal,
                        AgrupamentoRelatorioEnum.SEMANA,
                        null,
                        null
                );

        assertEquals(2, resultado.size());
        assertEquals("2026-W40", resultado.get(0).getPeriodo());
        assertEquals(9L, resultado.get(0).getQuantidade());
        assertEquals("2026-W41", resultado.get(1).getPeriodo());
        assertEquals(12L, resultado.get(1).getQuantidade());

        verify(ordemDeServicoRepository).contarRealizadasPorSemana(inicio, fimExclusivo, null, null);
    }

    @Test
    void buscarOsRealizadasAoLongoDoTempoDeveAgruparPorMes() {
        LocalDate dataInicial = LocalDate.of(2026, 1, 1);
        LocalDate dataFinal = LocalDate.of(2026, 12, 31);
        LocalDateTime inicio = LocalDateTime.of(2026, 1, 1, 0, 0);
        LocalDateTime fimExclusivo = LocalDateTime.of(2027, 1, 1, 0, 0);

        List<Object[]> dadosRepository = List.of(
                new Object[]{"2026-01", 18L},
                new Object[]{"2026-02", 22L}
        );

        when(ordemDeServicoRepository.contarRealizadasPorMes(inicio, fimExclusivo, null, 5L))
                .thenReturn(dadosRepository);

        List<RelatorioTecnicoSerieTemporalDTO> resultado =
                service.buscarOsRealizadasAoLongoDoTempo(
                        dataInicial,
                        dataFinal,
                        AgrupamentoRelatorioEnum.MES,
                        null,
                        5L
                );

        assertEquals(2, resultado.size());
        assertEquals("2026-01", resultado.get(0).getPeriodo());
        assertEquals(18L, resultado.get(0).getQuantidade());
        assertEquals("2026-02", resultado.get(1).getPeriodo());
        assertEquals(22L, resultado.get(1).getQuantidade());

        verify(ordemDeServicoRepository).contarRealizadasPorMes(inicio, fimExclusivo, null, 5L);
    }

    @Test
    void buscarOsRealizadasAoLongoDoTempoDeveBloquearAgrupamentoNulo() {
        assertThrows(DatabaseException.class, () -> service.buscarOsRealizadasAoLongoDoTempo(
                LocalDate.of(2026, 10, 1),
                LocalDate.of(2026, 10, 6),
                null,
                null,
                null
        ));
    }

    @Test
    void buscarOsRealizadasPorTipoDeveRetornarDistribuicaoPorTipo() {
        LocalDate dataInicial = LocalDate.of(2026, 10, 1);
        LocalDate dataFinal = LocalDate.of(2026, 10, 6);
        LocalDateTime inicio = LocalDateTime.of(2026, 10, 1, 0, 0);
        LocalDateTime fimExclusivo = LocalDateTime.of(2026, 10, 7, 0, 0);
        Long tecnicoId = 10L;

        List<RelatorioTecnicoQuantidadePorTipoDTO> esperado = List.of(
                new RelatorioTecnicoQuantidadePorTipoDTO(1L, "Instalação", 40L),
                new RelatorioTecnicoQuantidadePorTipoDTO(2L, "Manutenção", 28L)
        );

        when(ordemDeServicoRepository.contarRealizadasPorTipo(inicio, fimExclusivo, tecnicoId))
                .thenReturn(esperado);

        List<RelatorioTecnicoQuantidadePorTipoDTO> resultado =
                service.buscarOsRealizadasPorTipo(dataInicial, dataFinal, tecnicoId);

        assertEquals(esperado, resultado);
        verify(ordemDeServicoRepository).contarRealizadasPorTipo(inicio, fimExclusivo, tecnicoId);
    }

    @Test
    void buscarDistribuicaoAtualPorStatusDeveRetornarStatusAtuais() {
        List<RelatorioTecnicoQuantidadePorStatusDTO> esperado = List.of(
                new RelatorioTecnicoQuantidadePorStatusDTO(StatusEnum.ABERTA, 5L),
                new RelatorioTecnicoQuantidadePorStatusDTO(StatusEnum.AGENDADA, 12L),
                new RelatorioTecnicoQuantidadePorStatusDTO(StatusEnum.EM_ATENDIMENTO, 3L)
        );

        when(ordemDeServicoRepository.contarPorStatusAtual()).thenReturn(esperado);

        List<RelatorioTecnicoQuantidadePorStatusDTO> resultado =
                service.buscarDistribuicaoAtualPorStatus();

        assertEquals(esperado, resultado);
        verify(ordemDeServicoRepository).contarPorStatusAtual();
    }
}
