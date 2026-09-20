package com.marlondev.stockflow.repositories;

import com.marlondev.stockflow.domain.Almoxarifado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AlmoxarifadoRepository extends JpaRepository<Almoxarifado, Long> {

    Optional<Almoxarifado> findByNome(String nome);

    Optional<Almoxarifado> findByPrincipalTrue();

    @Modifying
    @Query("UPDATE Almoxarifado a SET a.principal = false WHERE a.principal = true")
    void desmarcarTodosComoPrincipal();
}