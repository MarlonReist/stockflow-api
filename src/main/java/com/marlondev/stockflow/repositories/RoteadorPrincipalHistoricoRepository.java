package com.marlondev.stockflow.repositories;

import com.marlondev.stockflow.domain.RoteadorPrincipalHistorico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoteadorPrincipalHistoricoRepository extends JpaRepository<RoteadorPrincipalHistorico, Long> {
    List<RoteadorPrincipalHistorico> findByClienteIdOrderByDataAlteracaoDescIdDesc(Long clienteId);
}
