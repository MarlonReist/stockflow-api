package com.marlondev.stockflow.repositories;

import com.marlondev.stockflow.domain.AlmoxarifadoEstoque;
import com.marlondev.stockflow.domain.MovimentacaoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {
}