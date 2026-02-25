package com.marlondev.stockflow.repositories;

import com.marlondev.stockflow.domain.Colaborador;
import com.marlondev.stockflow.domain.OrdemDeServico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrdemDeServicoRepository extends JpaRepository<OrdemDeServico, Long> {


}
