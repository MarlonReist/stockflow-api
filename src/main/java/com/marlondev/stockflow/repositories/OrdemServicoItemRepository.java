package com.marlondev.stockflow.repositories;


import com.marlondev.stockflow.domain.OrdemServicoItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdemServicoItemRepository extends JpaRepository<OrdemServicoItem, Long> {

    List<OrdemServicoItem> findByOrdemDeServicoId(Long osId);
}
