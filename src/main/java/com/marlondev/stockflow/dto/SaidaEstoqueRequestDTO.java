package com.marlondev.stockflow.dto;

import jakarta.validation.constraints.NotNull;

public class SaidaEstoqueRequestDTO {

    @NotNull(message = "Almoxarifado é obrigatório!")
    private Long almoxarifadoId;

    public SaidaEstoqueRequestDTO(){
    }

    public SaidaEstoqueRequestDTO(Long almoxarifadoId) {
        this.almoxarifadoId = almoxarifadoId;
    }

    public Long getAlmoxarifadoId() {
        return almoxarifadoId;
    }

    public void setAlmoxarifadoId(Long almoxarifadoId) {
        this.almoxarifadoId = almoxarifadoId;
    }
}
