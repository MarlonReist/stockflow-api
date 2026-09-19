package com.marlondev.stockflow.dto;

import jakarta.validation.constraints.NotBlank;

public class TipoOrdemServicoRequestDTO {

    @NotBlank(message = "Nome é obrigatório!")
    private String nome;

    public TipoOrdemServicoRequestDTO() {
    }

    public TipoOrdemServicoRequestDTO(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}