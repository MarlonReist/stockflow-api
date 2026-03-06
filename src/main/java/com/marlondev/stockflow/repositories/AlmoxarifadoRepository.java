package com.marlondev.stockflow.repositories;

import com.marlondev.stockflow.domain.Almoxarifado;
import com.marlondev.stockflow.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlmoxarifadoRepository extends JpaRepository<Almoxarifado, Long> {

    Optional<Cliente> findByNome(String nome);
}