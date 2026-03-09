package com.marlondev.stockflow.repositories;
import com.marlondev.stockflow.domain.OrdemDeServico;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrdemDeServicoRepository extends JpaRepository<OrdemDeServico, Long> {
}
