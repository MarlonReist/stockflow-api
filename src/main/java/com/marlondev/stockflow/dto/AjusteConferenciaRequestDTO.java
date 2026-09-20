package com.marlondev.stockflow.dto;

import jakarta.validation.constraints.NotBlank;

public class AjusteConferenciaRequestDTO {

    @NotBlank(message = "Motivo é obrigatório!")
    private String motivo;

    public AjusteConferenciaRequestDTO() {
    }

    public AjusteConferenciaRequestDTO(String motivo) {
        this.motivo = motivo;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}