package com.marlondev.stockflow.dto;

import jakarta.validation.constraints.NotBlank;

public class EsqueciSenhaRequestDTO {

    @NotBlank(message = "Login é obrigatório!")
    private String login;

    public EsqueciSenhaRequestDTO() {
    }

    public EsqueciSenhaRequestDTO(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
