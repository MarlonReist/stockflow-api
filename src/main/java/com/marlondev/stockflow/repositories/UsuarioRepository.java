package com.marlondev.stockflow.repositories;

import com.marlondev.stockflow.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByLogin(String login);

    boolean existsByColaboradorId(Long colaboradorId);

    boolean existsByColaboradorIdAndIdNot(Long colaboradorId, Long usuarioId);
}