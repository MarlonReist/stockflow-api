package com.marlondev.stockflow.repositories;

import com.marlondev.stockflow.domain.ConviteUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConviteUsuarioRepository extends JpaRepository<ConviteUsuario, Long> {

    Optional<ConviteUsuario> findByTokenHash(String tokenHash);

    List<ConviteUsuario> findByUsuarioIdAndDataUtilizacaoIsNullAndDataCancelamentoIsNull(Long usuarioId);

    boolean existsByUsuarioIdAndDataUtilizacaoIsNullAndDataCancelamentoIsNullAndDataExpiracaoBefore(Long usuarioId, LocalDateTime data);

    Optional<ConviteUsuario> findFirstByUsuarioIdAndDataUtilizacaoIsNullAndDataCancelamentoIsNullOrderByDataCriacaoDesc(Long usuarioId);
}
