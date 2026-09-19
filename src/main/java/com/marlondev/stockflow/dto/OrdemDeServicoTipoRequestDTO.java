package com.marlondev.stockflow.dto;

import jakarta.validation.constraints.NotNull;

public class OrdemDeServicoTipoRequestDTO {

    @NotNull(message = "Tipo de ordem de serviço é obrigatório!")
    private Long tipoOrdemServicoId;

    public OrdemDeServicoTipoRequestDTO() {
    }

    public OrdemDeServicoTipoRequestDTO(Long tipoOrdemServicoId) {
        this.tipoOrdemServicoId = tipoOrdemServicoId;
    }

    public Long getTipoOrdemServicoId() {
        return tipoOrdemServicoId;
    }

    public void setTipoOrdemServicoId(Long tipoOrdemServicoId) {
        this.tipoOrdemServicoId = tipoOrdemServicoId;
    }
}