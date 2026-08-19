package com.marlondev.stockflow.repositories;

import com.marlondev.stockflow.domain.OrdemDeServico;
import com.marlondev.stockflow.domain.enums.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;


public interface OrdemDeServicoRepository extends JpaRepository<OrdemDeServico, Long> {

    long countByStatus(StatusEnum status);

    long countByStatusAndDataAberturaBetween(
            StatusEnum status,
            LocalDate dataInicial,
            LocalDate dataFinal
    );
}
