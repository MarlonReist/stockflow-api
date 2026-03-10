package com.marlondev.stockflow.repositories;

import com.marlondev.stockflow.domain.EntradaEstoque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntradaEstoqueRepository extends JpaRepository<EntradaEstoque, Long> {
}