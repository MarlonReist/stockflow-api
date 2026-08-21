package com.marlondev.stockflow.repositories;


import com.marlondev.stockflow.domain.EntradaItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface EntradaItemRepository extends JpaRepository<EntradaItem, Long> {

    @Query("""
        SELECT COALESCE(SUM(item.quantidade * item.valorUnitario), 0)
        FROM EntradaItem item
        WHERE item.entradaEstoque.dataEntrada BETWEEN :dataInicio AND :dataFim
        """)
    Double somarValorTotalPorPeriodo(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim
    );
}