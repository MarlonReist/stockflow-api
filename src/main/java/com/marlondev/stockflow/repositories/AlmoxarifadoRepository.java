package com.marlondev.stockflow.repositories;

import com.marlondev.stockflow.domain.Almoxarifado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlmoxarifadoRepository extends JpaRepository<Almoxarifado, Long> {

    Optional<Almoxarifado> findByNome(String nome);
}