package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.enums.PerfilUsuario;
import com.marlondev.stockflow.domain.enums.StatusUsuario;

public class LoginResponseDTO {

    private Long usuarioId;
    private String nome;
    private String login;
    private PerfilUsuario perfil;
    private StatusUsuario status;
    private Boolean autenticado;
    private String token;

    public LoginResponseDTO(Long usuarioId, String nome, String login, PerfilUsuario perfil, StatusUsuario status, Boolean autenticado, String token) {
        this.usuarioId = usuarioId;
        this.nome = nome;
        this.login = login;
        this.perfil = perfil;
        this.status = status;
        this.autenticado = autenticado;
        this.token = token;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getNome() {
        return nome;
    }

    public String getLogin() {
        return login;
    }

    public PerfilUsuario getPerfil() {
        return perfil;
    }

    public StatusUsuario getStatus() {
        return status;
    }

    public Boolean getAutenticado() {
        return autenticado;
    }

    public String getToken() {
        return token;
    }
}