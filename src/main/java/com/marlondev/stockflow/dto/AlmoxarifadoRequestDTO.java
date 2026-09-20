package com.marlondev.stockflow.dto;

import jakarta.validation.constraints.NotBlank;

public class AlmoxarifadoRequestDTO {

    @NotBlank(message = "Nome é obrigatório!")
    private String nome;

    private boolean principal;

    public AlmoxarifadoRequestDTO(){
    }

    public AlmoxarifadoRequestDTO(String nome, boolean principal) {
        this.nome = nome;
        this.principal = principal;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }
}
