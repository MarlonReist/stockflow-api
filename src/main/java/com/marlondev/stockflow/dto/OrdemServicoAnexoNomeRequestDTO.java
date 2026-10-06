package com.marlondev.stockflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class OrdemServicoAnexoNomeRequestDTO {

    @NotBlank(message = "Nome do anexo \u00e9 obrigat\u00f3rio!")
    @Size(max = 120, message = "Nome do anexo deve ter no m\u00e1ximo 120 caracteres!")
    private String nome;

    public OrdemServicoAnexoNomeRequestDTO() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
