package com.marlondev.stockflow.repositories;

import com.marlondev.stockflow.domain.SaidaItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface SaidaItemRepository extends JpaRepository<SaidaItem, Long> {

    @Query("""
    SELECT COALESCE(SUM(item.quantidade * item.valorUnitario), 0)
    FROM SaidaItem item
    WHERE item.saidaEstoque.dataSaida BETWEEN :dataInicio AND :dataFim
    """)
    Double somarValorTotalPorPeriodo(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim
    );
}
