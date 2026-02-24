package com.marlondev.stockflow.repositories;

import com.marlondev.stockflow.domain.Cliente;
import com.marlondev.stockflow.domain.Produto;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Optional<Produto> findByNome(String nome);
}
