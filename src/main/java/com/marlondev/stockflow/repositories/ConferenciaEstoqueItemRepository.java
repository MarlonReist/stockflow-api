package com.marlondev.stockflow.repositories;

import com.marlondev.stockflow.domain.ConferenciaEstoqueItem;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface ConferenciaEstoqueItemRepository extends JpaRepository<ConferenciaEstoqueItem, Long> {

    List<ConferenciaEstoqueItem> findByConferenciaIdOrderByProdutoNomeAsc(Long conferenciaId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ConferenciaEstoqueItem> findWithLockById(Long id);
}
