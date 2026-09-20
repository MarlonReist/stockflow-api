package com.marlondev.stockflow.dto;

import jakarta.validation.constraints.NotNull;

public class ConferenciaEstoqueRequestDTO {

    @NotNull(message = "Almoxarifado é obrigatório!")
    private Long almoxarifadoId;

    public ConferenciaEstoqueRequestDTO() {
    }

    public ConferenciaEstoqueRequestDTO(Long almoxarifadoId) {
        this.almoxarifadoId = almoxarifadoId;
    }

    public Long getAlmoxarifadoId() {
        return almoxarifadoId;
    }

    public void setAlmoxarifadoId(Long almoxarifadoId) {
        this.almoxarifadoId = almoxarifadoId;
    }
}