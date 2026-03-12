package com.marlondev.stockflow.repositories;

import com.marlondev.stockflow.domain.SaidaItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaidaItemRepository extends JpaRepository<SaidaItem, Long> {
}
