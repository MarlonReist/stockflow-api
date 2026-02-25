package com.marlondev.stockflow.repositories;

import com.marlondev.stockflow.domain.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {

    Optional<Colaborador> findByCpf(String cpf);
}
