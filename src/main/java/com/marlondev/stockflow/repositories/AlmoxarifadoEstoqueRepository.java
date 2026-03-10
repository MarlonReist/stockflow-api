package com.marlondev.stockflow.repositories;

import com.marlondev.stockflow.domain.AlmoxarifadoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlmoxarifadoEstoqueRepository extends JpaRepository<AlmoxarifadoEstoque, Long> {

    boolean existsByAlmoxarifadoIdAndProdutoId(Long almoxarifadoId, Long produtoId);

    Optional<AlmoxarifadoEstoque> findByAlmoxarifadoIdAndProdutoId(Long almoxarifadoId, Long produtoId);
}