package com.marlondev.stockflow.repositories;

import com.marlondev.stockflow.domain.Almoxarifado;
import com.marlondev.stockflow.domain.Categoria;
import com.marlondev.stockflow.domain.Cliente;
import com.marlondev.stockflow.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByNome(String nome);
}