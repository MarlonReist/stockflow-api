package com.marlondev.stockflow.repositories;


import com.marlondev.stockflow.domain.EntradaItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntradaItemRepository extends JpaRepository<EntradaItem, Long> {

}