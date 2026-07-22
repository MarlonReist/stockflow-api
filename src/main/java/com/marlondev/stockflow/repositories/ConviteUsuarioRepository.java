package com.marlondev.stockflow.repositories;

import com.marlondev.stockflow.domain.ConviteUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConviteUsuarioRepository extends JpaRepository<ConviteUsuario, Long> {

    Optional<ConviteUsuario> findByTokenHash(String tokenHash);

    List<ConviteUsuario> findByUsuarioIdAndDataUtilizacaoIsNullAndDataCancelamentoIsNull(Long usuarioId);
}