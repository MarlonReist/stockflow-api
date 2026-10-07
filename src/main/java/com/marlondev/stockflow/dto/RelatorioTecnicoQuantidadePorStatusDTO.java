package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.enums.StatusEnum;

public class RelatorioTecnicoQuantidadePorStatusDTO {

    private StatusEnum status;
    private Long quantidade;

    public RelatorioTecnicoQuantidadePorStatusDTO() {
    }

    public RelatorioTecnicoQuantidadePorStatusDTO(StatusEnum status, Long quantidade) {
        this.status = status;
        this.quantidade = quantidade;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public Long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }
}