package com.marlondev.stockflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TransferenciaAlmoxarifadoRequestDTO {

    @NotNull(message = "Almoxarifado origem é obrigatório!")
    private Long almoxarifadoOrigemId;
    @NotNull(message = "Almoxarifado destino é obrigatório!")
    private Long almoxarifadoDestinoId;
    @NotBlank(message = "Observação é obrigatória!")
    private String observacao;

    public TransferenciaAlmoxarifadoRequestDTO(){
    }

    public TransferenciaAlmoxarifadoRequestDTO(Long almoxarifadoOrigemId, Long almoxarifadoDestinoId, String observacao) {
        this.almoxarifadoOrigemId = almoxarifadoOrigemId;
        this.almoxarifadoDestinoId = almoxarifadoDestinoId;
        this.observacao = observacao;
    }

    public Long getAlmoxarifadoOrigemId() {
        return almoxarifadoOrigemId;
    }

    public void setAlmoxarifadoOrigemId(Long almoxarifadoOrigemId) {
        this.almoxarifadoOrigemId = almoxarifadoOrigemId;
    }

    public Long getAlmoxarifadoDestinoId() {
        return almoxarifadoDestinoId;
    }

    public void setAlmoxarifadoDestinoId(Long almoxarifadoDestinoId) {
        this.almoxarifadoDestinoId = almoxarifadoDestinoId;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
