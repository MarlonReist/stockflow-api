package com.marlondev.stockflow.repositories;

import com.marlondev.stockflow.domain.TransferenciaItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferenciaItemRepository extends JpaRepository<TransferenciaItem, Long> {

    boolean existsByTransferenciaId(Long transferenciaId);
}