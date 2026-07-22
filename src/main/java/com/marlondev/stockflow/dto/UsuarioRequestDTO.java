package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.enums.PerfilUsuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UsuarioRequestDTO {

    @NotBlank(message = "Nome é obrigatório!")
    private String nome;

    @NotBlank(message = "Login é obrigatório!")
    private String login;

    @NotNull
    private PerfilUsuario perfil;

    public UsuarioRequestDTO(){
    }

    public UsuarioRequestDTO(String nome, String login, PerfilUsuario perfil) {
        this.nome = nome;
        this.login = login;
        this.perfil = perfil;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public PerfilUsuario getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilUsuario perfil) {
        this.perfil = perfil;
    }
}
