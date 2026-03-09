package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.Usuario;
import com.marlondev.stockflow.domain.enums.PerfilUsuario;

import java.io.Serial;
import java.io.Serializable;

public class UsuarioResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String login;
    private PerfilUsuario perfil;

    public UsuarioResponseDTO(){
    }

    public UsuarioResponseDTO(Usuario usuario){
        id = usuario.getId();
        login = usuario.getLogin();
        perfil = usuario.getPerfil();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
