package com.marlondev.stockflow.repositories;

import com.marlondev.stockflow.domain.OrdemServicoAnexo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdemServicoAnexoRepository extends JpaRepository<OrdemServicoAnexo, Long> {

    List<OrdemServicoAnexo> findByOrdemDeServicoIdOrderByDataUploadDesc(Long osId);
}