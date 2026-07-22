package com.marlondev.stockflow.repositories;

import com.marlondev.stockflow.domain.MovimentacaoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {

    long countByDataMovimentacaoBetween(LocalDate dataInicial, LocalDate dataFinal);

    List<MovimentacaoEstoque> findTop5ByOrderByDataMovimentacaoDescIdDesc();
}
