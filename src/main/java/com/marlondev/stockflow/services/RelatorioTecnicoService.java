package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.enums.StatusEnum;
import com.marlondev.stockflow.dto.*;
import com.marlondev.stockflow.repositories.OrdemDeServicoRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import org.springframework.stereotype.Service;
import com.marlondev.stockflow.domain.enums.AgrupamentoRelatorioEnum;


import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RelatorioTecnicoService {

    private final OrdemDeServicoRepository ordemDeServicoRepository;

    public RelatorioTecnicoService(OrdemDeServicoRepository ordemDeServicoRepository) {
        this.ordemDeServicoRepository = ordemDeServicoRepository;
    }

    public RelatorioTecnicoResumoDTO buscarResumo(
            LocalDate dataInicial,
            LocalDate dataFinal,
            Long tecnicoId,
            Long tipoOrdemServicoId
    ) {
        PeriodoRelatorio periodo = resolverPeriodo(dataInicial, dataFinal);

        Long totalOsRealizadas = ordemDeServicoRepository.contarRealizadasPorPeriodo(
                periodo.inicio(),
                periodo.fimExclusivo(),
                tecnicoId,
                tipoOrdemServicoId
        );

        Long totalOsFinalizadas = ordemDeServicoRepository.contarFechadasPorPeriodoEStatus(
                periodo.inicio(),
                periodo.fimExclusivo(),
                StatusEnum.FINALIZADA,
                tecnicoId,
                tipoOrdemServicoId
        );

        Long totalOsCanceladas = ordemDeServicoRepository.contarFechadasPorPeriodoEStatus(
                periodo.inicio(),
                periodo.fimExclusivo(),
                StatusEnum.CANCELADA,
                tecnicoId,
                tipoOrdemServicoId
        );

        Long totalAguardandoConferencia = ordemDeServicoRepository.contarRealizadasPorPeriodoEStatusAtual(
                periodo.inicio(),
                periodo.fimExclusivo(),
                StatusEnum.AGUARDANDO_CONFERENCIA,
                tecnicoId,
                tipoOrdemServicoId
        );

        Long tempoMedioAtendimentoSegundos = ordemDeServicoRepository.calcularTempoMedioAtendimentoSegundos(
                periodo.inicio(),
                periodo.fimExclusivo(),
                tecnicoId,
                tipoOrdemServicoId
        );

        Long quantidadeTecnicosComAtendimento = ordemDeServicoRepository.contarTecnicosComAtendimentoPorPeriodo(
                periodo.inicio(),
                periodo.fimExclusivo(),
                tecnicoId,
                tipoOrdemServicoId
        );

        return new RelatorioTecnicoResumoDTO(
                totalOsRealizadas,
                totalOsFinalizadas,
                totalOsCanceladas,
                totalAguardandoConferencia,
                tempoMedioAtendimentoSegundos,
                quantidadeTecnicosComAtendimento
        );
    }

    public List<RelatorioTecnicoQuantidadePorTecnicoDTO> buscarOsRealizadasPorTecnicoResponsavel(
            LocalDate dataInicial,
            LocalDate dataFinal,
            Long tecnicoId,
            Long tipoOrdemServicoId
    ) {
        PeriodoRelatorio periodo = resolverPeriodo(dataInicial, dataFinal);

        return ordemDeServicoRepository.contarRealizadasPorTecnicoResponsavel(
                periodo.inicio(),
                periodo.fimExclusivo(),
                tecnicoId,
                tipoOrdemServicoId
        );
    }

    public List<RelatorioTecnicoQuantidadePorTecnicoDTO> buscarParticipacoesPorTecnicoAjudante(
            LocalDate dataInicial,
            LocalDate dataFinal,
            Long tecnicoId,
            Long tipoOrdemServicoId
    ) {
        PeriodoRelatorio periodo = resolverPeriodo(dataInicial, dataFinal);

        return ordemDeServicoRepository.contarParticipacoesPorTecnicoAjudante(
                periodo.inicio(),
                periodo.fimExclusivo(),
                tecnicoId,
                tipoOrdemServicoId
        );
    }

    private PeriodoRelatorio resolverPeriodo(LocalDate dataInicial, LocalDate dataFinal) {
        if (dataInicial == null || dataFinal == null) {
            throw new DatabaseException("Data inicial e data final devem ser informadas!");
        }

        if (dataInicial.isAfter(dataFinal)) {
            throw new DatabaseException("Data inicial n\u00e3o pode ser maior que a data final!");
        }

        return new PeriodoRelatorio(
                dataInicial.atStartOfDay(),
                dataFinal.plusDays(1).atStartOfDay()
        );
    }

    public List<RelatorioTecnicoSerieTemporalDTO> buscarOsRealizadasAoLongoDoTempo(
            LocalDate dataInicial,
            LocalDate dataFinal,
            AgrupamentoRelatorioEnum agrupamento,
            Long tecnicoId,
            Long tipoOrdemServicoId
    ) {
        PeriodoRelatorio periodo = resolverPeriodo(dataInicial, dataFinal);

        if (agrupamento == null) {
            throw new DatabaseException("Agrupamento do relatório deve ser informado!");
        }

        List<Object[]> dados = switch (agrupamento) {
            case DIA -> ordemDeServicoRepository.contarRealizadasPorDia(
                    periodo.inicio(),
                    periodo.fimExclusivo(),
                    tecnicoId,
                    tipoOrdemServicoId
            );
            case SEMANA -> ordemDeServicoRepository.contarRealizadasPorSemana(
                    periodo.inicio(),
                    periodo.fimExclusivo(),
                    tecnicoId,
                    tipoOrdemServicoId
            );
            case MES -> ordemDeServicoRepository.contarRealizadasPorMes(
                    periodo.inicio(),
                    periodo.fimExclusivo(),
                    tecnicoId,
                    tipoOrdemServicoId
            );
        };

        return converterSerieTemporal(dados);
    }

    private List<RelatorioTecnicoSerieTemporalDTO> converterSerieTemporal(List<Object[]> dados) {
        List<RelatorioTecnicoSerieTemporalDTO> resultado = new ArrayList<>();

        for (Object[] linha : dados) {
            String periodo = String.valueOf(linha[0]);
            Long quantidade = ((Number) linha[1]).longValue();

            resultado.add(new RelatorioTecnicoSerieTemporalDTO(periodo, quantidade));
        }

        return resultado;
    }

    public List<RelatorioTecnicoQuantidadePorTipoDTO> buscarOsRealizadasPorTipo(
            LocalDate dataInicial,
            LocalDate dataFinal,
            Long tecnicoId
    ) {
        PeriodoRelatorio periodo = resolverPeriodo(dataInicial, dataFinal);

        return ordemDeServicoRepository.contarRealizadasPorTipo(
                periodo.inicio(),
                periodo.fimExclusivo(),
                tecnicoId
        );
    }

    public List<RelatorioTecnicoQuantidadePorStatusDTO> buscarDistribuicaoAtualPorStatus() {
        return ordemDeServicoRepository.contarPorStatusAtual();
    }

    private record PeriodoRelatorio(LocalDateTime inicio, LocalDateTime fimExclusivo) {
    }
}
