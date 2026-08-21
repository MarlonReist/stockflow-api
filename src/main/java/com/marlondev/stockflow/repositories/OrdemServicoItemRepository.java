package com.marlondev.stockflow.repositories;


import com.marlondev.stockflow.domain.OrdemServicoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OrdemServicoItemRepository extends JpaRepository<OrdemServicoItem, Long> {

    List<OrdemServicoItem> findByOrdemDeServicoId(Long osId);

    @Query("""
    SELECT COALESCE(SUM(item.quantidade * item.valorUnitario), 0)
    FROM OrdemServicoItem item
    WHERE item.ordemDeServico.dataAbertura BETWEEN :dataInicio AND :dataFim
    """)
    Double somarCustoTotalPorPeriodo(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim
    );
}
