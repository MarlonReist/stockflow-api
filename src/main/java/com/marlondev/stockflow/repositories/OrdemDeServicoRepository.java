package com.marlondev.stockflow.repositories;

import com.marlondev.stockflow.domain.OrdemDeServico;
import com.marlondev.stockflow.domain.enums.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
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
}
