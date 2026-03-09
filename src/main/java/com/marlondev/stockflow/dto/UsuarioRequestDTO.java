package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.enums.PerfilUsuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UsuarioRequestDTO {

    @NotBlank(message = "Login é obrigatório!")
    private String login;

    @NotBlank(message = "Senha é obrigatória!")
    @Size(min = 8)
    private String senha;

    @NotNull
    private PerfilUsuario perfil;

    public UsuarioRequestDTO(){
    }

    public UsuarioRequestDTO(String login, String senha, PerfilUsuario perfil) {
        this.login = login;
        this.senha = senha;
        this.perfil = perfil;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public PerfilUsuario getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilUsuario perfil) {
        this.perfil = perfil;
    }
}
