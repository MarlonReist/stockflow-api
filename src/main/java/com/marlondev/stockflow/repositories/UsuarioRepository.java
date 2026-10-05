package com.marlondev.stockflow.repositories;

import com.marlondev.stockflow.domain.Usuario;
import com.marlondev.stockflow.domain.enums.PerfilUsuario;
import com.marlondev.stockflow.domain.enums.StatusUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByLogin(String login);

    boolean existsByColaboradorId(Long colaboradorId);

    boolean existsByColaboradorIdAndIdNot(Long colaboradorId, Long usuarioId);

    List<Usuario> findByPerfilAndStatusAndColaboradorIsNotNullOrderByColaboradorNomeAsc(
            PerfilUsuario perfil,
            StatusUsuario status
    );

    Optional<Usuario> findByColaboradorIdAndPerfilAndStatus(
            Long colaboradorId,
            PerfilUsuario perfil,
            StatusUsuario status
    );
}
