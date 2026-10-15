package com.marlondev.stockflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.Valid;

public class OrdemDeServicoConclusaoAtendimentoRequestDTO {

    @NotBlank(message = "Observação de conclusão é obrigatória!")
    private String observacaoConclusao;

    @Valid
    private RoteadorPrincipalRequestDTO roteadorPrincipal;

    public OrdemDeServicoConclusaoAtendimentoRequestDTO() {
    }

    public String getObservacaoConclusao() {
        return observacaoConclusao;
    }

    public void setObservacaoConclusao(String observacaoConclusao) {
        this.observacaoConclusao = observacaoConclusao;
    }

    public RoteadorPrincipalRequestDTO getRoteadorPrincipal() { return roteadorPrincipal; }
    public void setRoteadorPrincipal(RoteadorPrincipalRequestDTO roteadorPrincipal) { this.roteadorPrincipal = roteadorPrincipal; }
}
