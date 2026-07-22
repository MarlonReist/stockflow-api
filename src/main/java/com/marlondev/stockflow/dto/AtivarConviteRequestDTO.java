package com.marlondev.stockflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AtivarConviteRequestDTO {

    @NotBlank(message = "Token é obrigatório!")
    private String token;

    @NotBlank(message = "Senha é obrigatória!")
    @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres!")
    private String senha;

    @NotBlank(message = "Confirmação de senha é obrigatória!")
    private String confirmacaoSenha;

    public AtivarConviteRequestDTO() {
    }

    public AtivarConviteRequestDTO(String token, String senha, String confirmacaoSenha) {
        this.token = token;
        this.senha = senha;
        this.confirmacaoSenha = confirmacaoSenha;
    }

    public String getToken() {
        return token;
    }

    public String getSenha() {
        return senha;
    }

    public String getConfirmacaoSenha() {
        return confirmacaoSenha;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setConfirmacaoSenha(String confirmacaoSenha) {
        this.confirmacaoSenha = confirmacaoSenha;
    }
}