package com.marlondev.stockflow.dto;

import jakarta.validation.constraints.NotBlank;

public class OrdemDeServicoConclusaoAtendimentoRequestDTO {

    @NotBlank(message = "Observação de conclusão é obrigatória!")
    private String observacaoConclusao;

    public OrdemDeServicoConclusaoAtendimentoRequestDTO() {
    }

    public String getObservacaoConclusao() {
        return observacaoConclusao;
    }

    public void setObservacaoConclusao(String observacaoConclusao) {
        this.observacaoConclusao = observacaoConclusao;
    }
}