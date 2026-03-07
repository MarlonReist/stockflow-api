package com.marlondev.stockflow.dto;

import jakarta.validation.constraints.NotBlank;

public class AlmoxarifadoRequestDTO {

    @NotBlank(message = "Nome é obrigatório!")
    private String nome;

    public AlmoxarifadoRequestDTO(){
    }

    public AlmoxarifadoRequestDTO(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
