package com.marlondev.stockflow.repositories;

import com.marlondev.stockflow.domain.ConferenciaEstoqueItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConferenciaEstoqueItemRepository extends JpaRepository<ConferenciaEstoqueItem, Long> {

    List<ConferenciaEstoqueItem> findByConferenciaIdOrderByProdutoNomeAsc(Long conferenciaId);
}