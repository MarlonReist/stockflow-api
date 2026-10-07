package com.marlondev.stockflow.repositories;

import com.marlondev.stockflow.domain.OrdemDeServico;
import com.marlondev.stockflow.domain.enums.StatusEnum;
import com.marlondev.stockflow.dto.RelatorioTecnicoQuantidadePorTecnicoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


public interface OrdemDeServicoRepository extends JpaRepository<OrdemDeServico, Long> {

    long countByStatus(StatusEnum status);

    long countByStatusAndDataAberturaBetween(
            StatusEnum status,
            LocalDate dataInicial,
            LocalDate dataFinal
    );

    List<OrdemDeServico> findByColaboradorIdAndStatusInOrderByDataAgendadaAsc(
            Long colaboradorId,
            Collection<StatusEnum> status
    );

    Optional<OrdemDeServico> findByIdAndColaboradorId(Long id, Long colaboradorId);

    @Query("""
            SELECT COUNT(os)
            FROM OrdemDeServico os
            WHERE os.fimAtendimento >= :inicio
              AND os.fimAtendimento < :fimExclusivo
              AND (:tecnicoId IS NULL OR os.colaborador.id = :tecnicoId)
              AND (:tipoOrdemServicoId IS NULL OR os.tipoOrdemServico.id = :tipoOrdemServicoId)
            """)
    Long contarRealizadasPorPeriodo(
            @Param("inicio") LocalDateTime inicio,
            @Param("fimExclusivo") LocalDateTime fimExclusivo,
            @Param("tecnicoId") Long tecnicoId,
            @Param("tipoOrdemServicoId") Long tipoOrdemServicoId
    );

    @Query("""
            SELECT COUNT(os)
            FROM OrdemDeServico os
            WHERE os.dataFechamento >= :inicio
              AND os.dataFechamento < :fimExclusivo
              AND os.status = :status
              AND (:tecnicoId IS NULL OR os.colaborador.id = :tecnicoId)
              AND (:tipoOrdemServicoId IS NULL OR os.tipoOrdemServico.id = :tipoOrdemServicoId)
            """)
    Long contarFechadasPorPeriodoEStatus(
            @Param("inicio") LocalDateTime inicio,
            @Param("fimExclusivo") LocalDateTime fimExclusivo,
            @Param("status") StatusEnum status,
            @Param("tecnicoId") Long tecnicoId,
            @Param("tipoOrdemServicoId") Long tipoOrdemServicoId
    );

    @Query("""
            SELECT COUNT(os)
            FROM OrdemDeServico os
            WHERE os.fimAtendimento >= :inicio
              AND os.fimAtendimento < :fimExclusivo
              AND os.status = :status
              AND (:tecnicoId IS NULL OR os.colaborador.id = :tecnicoId)
              AND (:tipoOrdemServicoId IS NULL OR os.tipoOrdemServico.id = :tipoOrdemServicoId)
            """)
    Long contarRealizadasPorPeriodoEStatusAtual(
            @Param("inicio") LocalDateTime inicio,
            @Param("fimExclusivo") LocalDateTime fimExclusivo,
            @Param("status") StatusEnum status,
            @Param("tecnicoId") Long tecnicoId,
            @Param("tipoOrdemServicoId") Long tipoOrdemServicoId
    );

    @Query("""
            SELECT COUNT(DISTINCT os.colaborador.id)
            FROM OrdemDeServico os
            WHERE os.fimAtendimento >= :inicio
              AND os.fimAtendimento < :fimExclusivo
              AND os.colaborador IS NOT NULL
              AND (:tecnicoId IS NULL OR os.colaborador.id = :tecnicoId)
              AND (:tipoOrdemServicoId IS NULL OR os.tipoOrdemServico.id = :tipoOrdemServicoId)
            """)
    Long contarTecnicosComAtendimentoPorPeriodo(
            @Param("inicio") LocalDateTime inicio,
            @Param("fimExclusivo") LocalDateTime fimExclusivo,
            @Param("tecnicoId") Long tecnicoId,
            @Param("tipoOrdemServicoId") Long tipoOrdemServicoId
    );

    @Query(value = """
            SELECT COALESCE(ROUND(AVG(TIMESTAMPDIFF(SECOND, inicio_atendimento, fim_atendimento))), 0)
            FROM ordem_de_servico
            WHERE fim_atendimento >= :inicio
              AND fim_atendimento < :fimExclusivo
              AND inicio_atendimento IS NOT NULL
              AND fim_atendimento IS NOT NULL
              AND (:tecnicoId IS NULL OR colaborador_id = :tecnicoId)
              AND (:tipoOrdemServicoId IS NULL OR tipo_ordem_servico_id = :tipoOrdemServicoId)
            """, nativeQuery = true)
    Long calcularTempoMedioAtendimentoSegundos(
            @Param("inicio") LocalDateTime inicio,
            @Param("fimExclusivo") LocalDateTime fimExclusivo,
            @Param("tecnicoId") Long tecnicoId,
            @Param("tipoOrdemServicoId") Long tipoOrdemServicoId
    );

    @Query("""
            SELECT new com.marlondev.stockflow.dto.RelatorioTecnicoQuantidadePorTecnicoDTO(
                os.colaborador.id,
                os.colaborador.nome,
                COUNT(os)
            )
            FROM OrdemDeServico os
            WHERE os.fimAtendimento >= :inicio
              AND os.fimAtendimento < :fimExclusivo
              AND os.colaborador IS NOT NULL
              AND (:tecnicoId IS NULL OR os.colaborador.id = :tecnicoId)
              AND (:tipoOrdemServicoId IS NULL OR os.tipoOrdemServico.id = :tipoOrdemServicoId)
            GROUP BY os.colaborador.id, os.colaborador.nome
            ORDER BY COUNT(os) DESC, os.colaborador.nome ASC
            """)
    List<RelatorioTecnicoQuantidadePorTecnicoDTO> contarRealizadasPorTecnicoResponsavel(
            @Param("inicio") LocalDateTime inicio,
            @Param("fimExclusivo") LocalDateTime fimExclusivo,
            @Param("tecnicoId") Long tecnicoId,
            @Param("tipoOrdemServicoId") Long tipoOrdemServicoId
    );

    @Query("""
            SELECT new com.marlondev.stockflow.dto.RelatorioTecnicoQuantidadePorTecnicoDTO(
                os.ajudante.id,
                os.ajudante.nome,
                COUNT(os)
            )
            FROM OrdemDeServico os
            WHERE os.fimAtendimento >= :inicio
              AND os.fimAtendimento < :fimExclusivo
              AND os.ajudante IS NOT NULL
              AND (:tecnicoId IS NULL OR os.ajudante.id = :tecnicoId)
              AND (:tipoOrdemServicoId IS NULL OR os.tipoOrdemServico.id = :tipoOrdemServicoId)
            GROUP BY os.ajudante.id, os.ajudante.nome
            ORDER BY COUNT(os) DESC, os.ajudante.nome ASC
            """)
    List<RelatorioTecnicoQuantidadePorTecnicoDTO> contarParticipacoesPorTecnicoAjudante(
            @Param("inicio") LocalDateTime inicio,
            @Param("fimExclusivo") LocalDateTime fimExclusivo,
            @Param("tecnicoId") Long tecnicoId,
            @Param("tipoOrdemServicoId") Long tipoOrdemServicoId
    );
}
