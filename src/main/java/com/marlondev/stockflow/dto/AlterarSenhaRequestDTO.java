package com.marlondev.stockflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AlterarSenhaRequestDTO {

    @NotBlank(message = "Senha atual é obrigatória!")
    private String senhaAtual;

    @NotBlank(message = "Nova senha é obrigatória!")
    @Size(min = 8, message = "Nova senha deve ter no mínimo 8 caracteres!")
    @Pattern(regexp = ".*[A-Z].*", message = "Nova senha deve conter pelo menos uma letra maiúscula!")
    @Pattern(regexp = ".*[a-z].*", message = "Nova senha deve conter pelo menos uma letra minúscula!")
    @Pattern(regexp = ".*[0-9].*", message = "Nova senha deve conter pelo menos um número!")
    @Pattern(regexp = ".*[^A-Za-z0-9].*", message = "Nova senha deve conter pelo menos um caractere especial!")
    private String novaSenha;

    @NotBlank(message = "Confirmação de senha é obrigatória!")
    private String confirmacaoSenha;

    public AlterarSenhaRequestDTO() {
    }

    public AlterarSenhaRequestDTO(String senhaAtual, String novaSenha, String confirmacaoSenha) {
        this.senhaAtual = senhaAtual;
        this.novaSenha = novaSenha;
        this.confirmacaoSenha = confirmacaoSenha;
    }

    public String getSenhaAtual() {
        return senhaAtual;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public String getConfirmacaoSenha() {
        return confirmacaoSenha;
    }

    public void setSenhaAtual(String senhaAtual) {
        this.senhaAtual = senhaAtual;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public void setConfirmacaoSenha(String confirmacaoSenha) {
        this.confirmacaoSenha = confirmacaoSenha;
    }
}