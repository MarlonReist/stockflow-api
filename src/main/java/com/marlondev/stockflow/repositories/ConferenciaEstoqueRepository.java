package com.marlondev.stockflow.repositories;

import com.marlondev.stockflow.domain.ConferenciaEstoque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConferenciaEstoqueRepository extends JpaRepository<ConferenciaEstoque, Long> {
}