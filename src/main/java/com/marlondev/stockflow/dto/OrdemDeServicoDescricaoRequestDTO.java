package com.marlondev.stockflow.dto;

import jakarta.validation.constraints.NotBlank;

public class OrdemDeServicoDescricaoRequestDTO {

    @NotBlank(message = "Descrição é obrigatória!")
    private String descricao;

    public OrdemDeServicoDescricaoRequestDTO() {
    }

    public OrdemDeServicoDescricaoRequestDTO(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}