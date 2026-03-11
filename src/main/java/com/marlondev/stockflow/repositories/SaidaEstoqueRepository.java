package com.marlondev.stockflow.repositories;

import com.marlondev.stockflow.domain.SaidaEstoque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaidaEstoqueRepository extends JpaRepository<SaidaEstoque, Long> {
}