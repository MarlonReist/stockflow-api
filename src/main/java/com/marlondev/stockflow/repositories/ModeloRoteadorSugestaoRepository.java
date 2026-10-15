package com.marlondev.stockflow.repositories;

import com.marlondev.stockflow.domain.ModeloRoteadorSugestao;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ModeloRoteadorSugestaoRepository extends JpaRepository<ModeloRoteadorSugestao, Long> {

    @Modifying
    @Query(value = """
            INSERT INTO modelo_roteador_sugestao (modelo, modelo_normalizado, frequencia, atualizado_em)
            VALUES (:modelo, :modeloNormalizado, 1, :atualizadoEm)
            ON DUPLICATE KEY UPDATE frequencia = frequencia + 1, atualizado_em = VALUES(atualizado_em)
            """, nativeQuery = true)
    void registrarUso(@Param("modelo") String modelo, @Param("modeloNormalizado") String modeloNormalizado,
                      @Param("atualizadoEm") LocalDateTime atualizadoEm);

    List<ModeloRoteadorSugestao> findByModeloNormalizadoContainingOrderByFrequenciaDescModeloAsc(
            String consultaNormalizada, Pageable pageable);
}
