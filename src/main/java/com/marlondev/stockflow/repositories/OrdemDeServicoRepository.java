package com.marlondev.stockflow.repositories;

import com.marlondev.stockflow.domain.OrdemDeServico;
import com.marlondev.stockflow.domain.enums.StatusEnum;
import com.marlondev.stockflow.dto.RelatorioTecnicoAtendimentoDTO;
import com.marlondev.stockflow.dto.RelatorioTecnicoQuantidadePorStatusDTO;
import com.marlondev.stockflow.dto.RelatorioTecnicoQuantidadePorTecnicoDTO;
import com.marlondev.stockflow.dto.RelatorioTecnicoQuantidadePorTipoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            SELECT os
            FROM OrdemDeServico os
            JOIN FETCH os.cliente
            LEFT JOIN FETCH os.tipoOrdemServico
            LEFT JOIN FETCH os.colaborador
            LEFT JOIN FETCH os.ajudante
            WHERE os.id = :id
              AND os.fimAtendimento IS NOT NULL
              AND (
                    os.colaborador.id = :tecnicoId
                    OR os.ajudante.id = :tecnicoId
              )
            """)
    Optional<OrdemDeServico> buscarHistoricoDoTecnicoPorId(
            @Param("id") Long id,
            @Param("tecnicoId") Long tecnicoId
    );

    @Query(value = """
            SELECT os
            FROM OrdemDeServico os
            JOIN os.cliente cliente
            LEFT JOIN os.tipoOrdemServico tipo
            LEFT JOIN os.colaborador responsavel
            LEFT JOIN os.ajudante ajudante
            WHERE os.fimAtendimento IS NOT NULL
              AND (:osId IS NULL OR os.id = :osId)
              AND (:inicioConclusao IS NULL OR os.fimAtendimento >= :inicioConclusao)
              AND (:fimConclusaoExclusivo IS NULL OR os.fimAtendimento < :fimConclusaoExclusivo)
              AND (:tipoOrdemServicoId IS NULL OR tipo.id = :tipoOrdemServicoId)
              AND (
                    responsavel.id = :tecnicoId
                    OR ajudante.id = :tecnicoId
              )
            ORDER BY os.fimAtendimento DESC, os.id DESC
            """,
            countQuery = """
            SELECT COUNT(os)
            FROM OrdemDeServico os
            LEFT JOIN os.tipoOrdemServico tipo
            LEFT JOIN os.colaborador responsavel
            LEFT JOIN os.ajudante ajudante
            WHERE os.fimAtendimento IS NOT NULL
              AND (:osId IS NULL OR os.id = :osId)
              AND (:inicioConclusao IS NULL OR os.fimAtendimento >= :inicioConclusao)
              AND (:fimConclusaoExclusivo IS NULL OR os.fimAtendimento < :fimConclusaoExclusivo)
              AND (:tipoOrdemServicoId IS NULL OR tipo.id = :tipoOrdemServicoId)
              AND (
                    responsavel.id = :tecnicoId
                    OR ajudante.id = :tecnicoId
              )
            """)
    Page<OrdemDeServico> buscarHistoricoDoTecnico(
            @Param("tecnicoId") Long tecnicoId,
            @Param("osId") Long osId,
            @Param("inicioConclusao") LocalDateTime inicioConclusao,
            @Param("fimConclusaoExclusivo") LocalDateTime fimConclusaoExclusivo,
            @Param("tipoOrdemServicoId") Long tipoOrdemServicoId,
            Pageable pageable
    );

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

    @Query(value = """
        SELECT DATE_FORMAT(fim_atendimento, '%Y-%m-%d') AS periodo,
               COUNT(*) AS quantidade
        FROM ordem_de_servico
        WHERE fim_atendimento >= :inicio
          AND fim_atendimento < :fimExclusivo
          AND (:tecnicoId IS NULL OR colaborador_id = :tecnicoId)
          AND (:tipoOrdemServicoId IS NULL OR tipo_ordem_servico_id = :tipoOrdemServicoId)
        GROUP BY DATE_FORMAT(fim_atendimento, '%Y-%m-%d')
        ORDER BY periodo ASC
        """, nativeQuery = true)
    List<Object[]> contarRealizadasPorDia(
            @Param("inicio") LocalDateTime inicio,
            @Param("fimExclusivo") LocalDateTime fimExclusivo,
            @Param("tecnicoId") Long tecnicoId,
            @Param("tipoOrdemServicoId") Long tipoOrdemServicoId
    );

    @Query(value = """
        SELECT DATE_FORMAT(fim_atendimento, '%x-W%v') AS periodo,
               COUNT(*) AS quantidade
        FROM ordem_de_servico
        WHERE fim_atendimento >= :inicio
          AND fim_atendimento < :fimExclusivo
          AND (:tecnicoId IS NULL OR colaborador_id = :tecnicoId)
          AND (:tipoOrdemServicoId IS NULL OR tipo_ordem_servico_id = :tipoOrdemServicoId)
        GROUP BY DATE_FORMAT(fim_atendimento, '%x-W%v')
        ORDER BY periodo ASC
        """, nativeQuery = true)
    List<Object[]> contarRealizadasPorSemana(
            @Param("inicio") LocalDateTime inicio,
            @Param("fimExclusivo") LocalDateTime fimExclusivo,
            @Param("tecnicoId") Long tecnicoId,
            @Param("tipoOrdemServicoId") Long tipoOrdemServicoId
    );

    @Query(value = """
        SELECT DATE_FORMAT(fim_atendimento, '%Y-%m') AS periodo,
               COUNT(*) AS quantidade
        FROM ordem_de_servico
        WHERE fim_atendimento >= :inicio
          AND fim_atendimento < :fimExclusivo
          AND (:tecnicoId IS NULL OR colaborador_id = :tecnicoId)
          AND (:tipoOrdemServicoId IS NULL OR tipo_ordem_servico_id = :tipoOrdemServicoId)
        GROUP BY DATE_FORMAT(fim_atendimento, '%Y-%m')
        ORDER BY periodo ASC
        """, nativeQuery = true)
    List<Object[]> contarRealizadasPorMes(
            @Param("inicio") LocalDateTime inicio,
            @Param("fimExclusivo") LocalDateTime fimExclusivo,
            @Param("tecnicoId") Long tecnicoId,
            @Param("tipoOrdemServicoId") Long tipoOrdemServicoId
    );

    @Query("""
        SELECT new com.marlondev.stockflow.dto.RelatorioTecnicoQuantidadePorTipoDTO(
            os.tipoOrdemServico.id,
            os.tipoOrdemServico.nome,
            COUNT(os)
        )
        FROM OrdemDeServico os
        WHERE os.fimAtendimento >= :inicio
          AND os.fimAtendimento < :fimExclusivo
          AND os.tipoOrdemServico IS NOT NULL
          AND (:tecnicoId IS NULL OR os.colaborador.id = :tecnicoId)
        GROUP BY os.tipoOrdemServico.id, os.tipoOrdemServico.nome
        ORDER BY COUNT(os) DESC, os.tipoOrdemServico.nome ASC
        """)
    List<RelatorioTecnicoQuantidadePorTipoDTO> contarRealizadasPorTipo(
            @Param("inicio") LocalDateTime inicio,
            @Param("fimExclusivo") LocalDateTime fimExclusivo,
            @Param("tecnicoId") Long tecnicoId
    );

    @Query("""
        SELECT new com.marlondev.stockflow.dto.RelatorioTecnicoQuantidadePorStatusDTO(
            os.status,
            COUNT(os)
        )
        FROM OrdemDeServico os
        GROUP BY os.status
        ORDER BY os.status ASC
        """)
    List<RelatorioTecnicoQuantidadePorStatusDTO> contarPorStatusAtual();

    @Query(value = """
            SELECT new com.marlondev.stockflow.dto.RelatorioTecnicoAtendimentoDTO(
                os.id,
                cliente.nome,
                tipo.id,
                tipo.nome,
                responsavel.id,
                responsavel.nome,
                ajudante.id,
                ajudante.nome,
                os.inicioAtendimento,
                os.fimAtendimento,
                os.status
            )
            FROM OrdemDeServico os
            JOIN os.cliente cliente
            LEFT JOIN os.tipoOrdemServico tipo
            LEFT JOIN os.colaborador responsavel
            LEFT JOIN os.ajudante ajudante
            WHERE os.fimAtendimento >= :inicio
              AND os.fimAtendimento < :fimExclusivo
              AND (:tipoOrdemServicoId IS NULL OR tipo.id = :tipoOrdemServicoId)
              AND (
                    (:participacao = 'RESPONSAVEL' AND (
                        (:tecnicoId IS NULL AND responsavel IS NOT NULL)
                        OR (:tecnicoId IS NOT NULL AND responsavel.id = :tecnicoId)
                    ))
                    OR (:participacao = 'AJUDANTE' AND (
                        (:tecnicoId IS NULL AND ajudante IS NOT NULL)
                        OR (:tecnicoId IS NOT NULL AND ajudante.id = :tecnicoId)
                    ))
                    OR (:participacao = 'AMBOS' AND (
                        (:tecnicoId IS NULL AND (responsavel IS NOT NULL OR ajudante IS NOT NULL))
                        OR (:tecnicoId IS NOT NULL AND (responsavel.id = :tecnicoId OR ajudante.id = :tecnicoId))
                    ))
              )
            ORDER BY os.fimAtendimento ASC, os.id ASC
            """,
            countQuery = """
            SELECT COUNT(os)
            FROM OrdemDeServico os
            LEFT JOIN os.tipoOrdemServico tipo
            LEFT JOIN os.colaborador responsavel
            LEFT JOIN os.ajudante ajudante
            WHERE os.fimAtendimento >= :inicio
              AND os.fimAtendimento < :fimExclusivo
              AND (:tipoOrdemServicoId IS NULL OR tipo.id = :tipoOrdemServicoId)
              AND (
                    (:participacao = 'RESPONSAVEL' AND (
                        (:tecnicoId IS NULL AND responsavel IS NOT NULL)
                        OR (:tecnicoId IS NOT NULL AND responsavel.id = :tecnicoId)
                    ))
                    OR (:participacao = 'AJUDANTE' AND (
                        (:tecnicoId IS NULL AND ajudante IS NOT NULL)
                        OR (:tecnicoId IS NOT NULL AND ajudante.id = :tecnicoId)
                    ))
                    OR (:participacao = 'AMBOS' AND (
                        (:tecnicoId IS NULL AND (responsavel IS NOT NULL OR ajudante IS NOT NULL))
                        OR (:tecnicoId IS NOT NULL AND (responsavel.id = :tecnicoId OR ajudante.id = :tecnicoId))
                    ))
              )
            """)
    Page<RelatorioTecnicoAtendimentoDTO> buscarAtendimentosTecnicos(
            @Param("inicio") LocalDateTime inicio,
            @Param("fimExclusivo") LocalDateTime fimExclusivo,
            @Param("tecnicoId") Long tecnicoId,
            @Param("participacao") String participacao,
            @Param("tipoOrdemServicoId") Long tipoOrdemServicoId,
            Pageable pageable
    );

    @Query("""
            SELECT new com.marlondev.stockflow.dto.RelatorioTecnicoAtendimentoDTO(
                os.id,
                cliente.nome,
                tipo.id,
                tipo.nome,
                responsavel.id,
                responsavel.nome,
                ajudante.id,
                ajudante.nome,
                os.inicioAtendimento,
                os.fimAtendimento,
                os.status
            )
            FROM OrdemDeServico os
            JOIN os.cliente cliente
            LEFT JOIN os.tipoOrdemServico tipo
            LEFT JOIN os.colaborador responsavel
            LEFT JOIN os.ajudante ajudante
            WHERE os.fimAtendimento >= :inicio
              AND os.fimAtendimento < :fimExclusivo
              AND (:tipoOrdemServicoId IS NULL OR tipo.id = :tipoOrdemServicoId)
              AND (
                    (:participacao = 'RESPONSAVEL' AND (
                        (:tecnicoId IS NULL AND responsavel IS NOT NULL)
                        OR (:tecnicoId IS NOT NULL AND responsavel.id = :tecnicoId)
                    ))
                    OR (:participacao = 'AJUDANTE' AND (
                        (:tecnicoId IS NULL AND ajudante IS NOT NULL)
                        OR (:tecnicoId IS NOT NULL AND ajudante.id = :tecnicoId)
                    ))
                    OR (:participacao = 'AMBOS' AND (
                        (:tecnicoId IS NULL AND (responsavel IS NOT NULL OR ajudante IS NOT NULL))
                        OR (:tecnicoId IS NOT NULL AND (responsavel.id = :tecnicoId OR ajudante.id = :tecnicoId))
                    ))
              )
            ORDER BY os.fimAtendimento ASC, os.id ASC
            """)
    List<RelatorioTecnicoAtendimentoDTO> buscarAtendimentosTecnicos(
            @Param("inicio") LocalDateTime inicio,
            @Param("fimExclusivo") LocalDateTime fimExclusivo,
            @Param("tecnicoId") Long tecnicoId,
            @Param("participacao") String participacao,
            @Param("tipoOrdemServicoId") Long tipoOrdemServicoId
    );
}
