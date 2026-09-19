package com.marlondev.stockflow.repositories;

import com.marlondev.stockflow.domain.TipoOrdemServico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TipoOrdemServicoRepository extends JpaRepository<TipoOrdemServico, Long> {

    Optional<TipoOrdemServico> findByNomeIgnoreCase(String nome);

    List<TipoOrdemServico> findByAtivoTrueOrderByNomeAsc();
}