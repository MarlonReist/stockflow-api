package com.marlondev.stockflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RedefinirSenhaRequestDTO {

    @NotBlank(message = "Token é obrigatório!")
    private String token;

    @NotBlank(message = "Senha é obrigatória!")
    @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres!")
    @Pattern(regexp = ".*[A-Z].*", message = "Senha deve conter pelo menos uma letra maiúscula!")
    @Pattern(regexp = ".*[a-z].*", message = "Senha deve conter pelo menos uma letra minúscula!")
    @Pattern(regexp = ".*[0-9].*", message = "Senha deve conter pelo menos um número!")
    @Pattern(regexp = ".*[^A-Za-z0-9].*", message = "Senha deve conter pelo menos um caractere especial!")
    private String senha;

    @NotBlank(message = "Confirmação de senha é obrigatória!")
    private String confirmacaoSenha;

    public RedefinirSenhaRequestDTO() {
    }

    public RedefinirSenhaRequestDTO(String token, String senha, String confirmacaoSenha) {
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
