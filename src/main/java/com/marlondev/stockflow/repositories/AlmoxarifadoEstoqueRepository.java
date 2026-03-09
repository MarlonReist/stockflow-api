package com.marlondev.stockflow.repositories;

import com.marlondev.stockflow.domain.AlmoxarifadoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlmoxarifadoEstoqueRepository extends JpaRepository<AlmoxarifadoEstoque, Long> {

    boolean existsByAlmoxarifadoIdAndProdutoId(Long almoxarifadoId, Long produtoId);
}