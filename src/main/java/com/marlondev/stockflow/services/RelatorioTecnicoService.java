package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.enums.StatusEnum;
import com.marlondev.stockflow.domain.enums.ParticipacaoRelatorioTecnicoEnum;
import com.marlondev.stockflow.dto.*;
import com.marlondev.stockflow.repositories.ColaboradorRepository;
import com.marlondev.stockflow.repositories.OrdemDeServicoRepository;
import com.marlondev.stockflow.repositories.TipoOrdemServicoRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.marlondev.stockflow.domain.enums.AgrupamentoRelatorioEnum;


import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RelatorioTecnicoService {

    private final OrdemDeServicoRepository ordemDeServicoRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final TipoOrdemServicoRepository tipoOrdemServicoRepository;

    public RelatorioTecnicoService(OrdemDeServicoRepository ordemDeServicoRepository,
                                   ColaboradorRepository colaboradorRepository,
                                   TipoOrdemServicoRepository tipoOrdemServicoRepository) {
        this.ordemDeServicoRepository = ordemDeServicoRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.tipoOrdemServicoRepository = tipoOrdemServicoRepository;
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

    public RelatorioTecnicoAtendimentoPaginaDTO buscarAtendimentos(
            LocalDate dataInicial,
            LocalDate dataFinal,
            Long tecnicoId,
            ParticipacaoRelatorioTecnicoEnum participacao,
            Long tipoOrdemServicoId,
            Integer page,
            Integer size
    ) {
        PeriodoRelatorio periodo = resolverPeriodo(dataInicial, dataFinal);
        ParticipacaoRelatorioTecnicoEnum participacaoResolvida = resolverParticipacao(participacao);
        validarFiltros(tecnicoId, tipoOrdemServicoId);
        validarPaginacao(page, size);

        Page<RelatorioTecnicoAtendimentoDTO> pagina = ordemDeServicoRepository.buscarAtendimentosTecnicos(
                periodo.inicio(),
                periodo.fimExclusivo(),
                tecnicoId,
                participacaoResolvida.name(),
                tipoOrdemServicoId,
                PageRequest.of(page, size)
        );

        preencherParticipacaoDoTecnico(pagina.getContent(), tecnicoId);

        return new RelatorioTecnicoAtendimentoPaginaDTO(
                pagina.getContent(),
                pagina.getTotalElements(),
                pagina.getTotalPages(),
                pagina.getNumber(),
                pagina.getSize()
        );
    }

    public List<RelatorioTecnicoAtendimentoDTO> buscarAtendimentosParaPdf(
            LocalDate dataInicial,
            LocalDate dataFinal,
            Long tecnicoId,
            ParticipacaoRelatorioTecnicoEnum participacao,
            Long tipoOrdemServicoId
    ) {
        PeriodoRelatorio periodo = resolverPeriodo(dataInicial, dataFinal);
        ParticipacaoRelatorioTecnicoEnum participacaoResolvida = resolverParticipacao(participacao);
        validarFiltros(tecnicoId, tipoOrdemServicoId);

        List<RelatorioTecnicoAtendimentoDTO> atendimentos = ordemDeServicoRepository.buscarAtendimentosTecnicos(
                periodo.inicio(),
                periodo.fimExclusivo(),
                tecnicoId,
                participacaoResolvida.name(),
                tipoOrdemServicoId
        );

        preencherParticipacaoDoTecnico(atendimentos, tecnicoId);
        return atendimentos;
    }

    public String buscarNomeTecnicoFiltro(Long tecnicoId) {
        if (tecnicoId == null) {
            return "Todos os técnicos";
        }
        return colaboradorRepository.findById(tecnicoId)
                .orElseThrow(() -> new ResourceNotFoundException(tecnicoId))
                .getNome();
    }

    public String buscarNomeTipoOrdemServicoFiltro(Long tipoOrdemServicoId) {
        if (tipoOrdemServicoId == null) {
            return null;
        }
        return tipoOrdemServicoRepository.findById(tipoOrdemServicoId)
                .orElseThrow(() -> new ResourceNotFoundException(tipoOrdemServicoId))
                .getNome();
    }

    private ParticipacaoRelatorioTecnicoEnum resolverParticipacao(ParticipacaoRelatorioTecnicoEnum participacao) {
        return participacao == null ? ParticipacaoRelatorioTecnicoEnum.RESPONSAVEL : participacao;
    }

    private void validarFiltros(Long tecnicoId, Long tipoOrdemServicoId) {
        if (tecnicoId != null && !colaboradorRepository.existsById(tecnicoId)) {
            throw new ResourceNotFoundException(tecnicoId);
        }
        if (tipoOrdemServicoId != null && !tipoOrdemServicoRepository.existsById(tipoOrdemServicoId)) {
            throw new ResourceNotFoundException(tipoOrdemServicoId);
        }
    }

    private void validarPaginacao(Integer page, Integer size) {
        if (page == null || page < 0) {
            throw new DatabaseException("Página deve ser maior ou igual a zero!");
        }
        if (size == null || size <= 0) {
            throw new DatabaseException("Tamanho da página deve ser maior que zero!");
        }
        if (size > 100) {
            throw new DatabaseException("Tamanho da página deve ser no máximo 100!");
        }
    }

    private void preencherParticipacaoDoTecnico(List<RelatorioTecnicoAtendimentoDTO> atendimentos, Long tecnicoId) {
        if (tecnicoId == null) {
            return;
        }

        for (RelatorioTecnicoAtendimentoDTO atendimento : atendimentos) {
            boolean responsavel = tecnicoId.equals(atendimento.getTecnicoResponsavelId());
            boolean ajudante = tecnicoId.equals(atendimento.getTecnicoAjudanteId());

            if (responsavel && ajudante) {
                atendimento.setParticipacaoDoTecnico("RESPONSAVEL_E_AJUDANTE");
            } else if (responsavel) {
                atendimento.setParticipacaoDoTecnico("RESPONSAVEL");
            } else if (ajudante) {
                atendimento.setParticipacaoDoTecnico("AJUDANTE");
            }
        }
    }

    private record PeriodoRelatorio(LocalDateTime inicio, LocalDateTime fimExclusivo) {
    }
}
