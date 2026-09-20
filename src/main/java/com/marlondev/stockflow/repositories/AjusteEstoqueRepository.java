package com.marlondev.stockflow.repositories;

import com.marlondev.stockflow.domain.AjusteEstoque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AjusteEstoqueRepository extends JpaRepository<AjusteEstoque, Long> {

    boolean existsByConferenciaEstoqueItemId(Long conferenciaEstoqueItemId);
}