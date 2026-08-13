package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.Usuario;
import com.marlondev.stockflow.domain.enums.PerfilUsuario;
import com.marlondev.stockflow.domain.enums.StatusUsuario;

import java.io.Serial;
import java.io.Serializable;

public class UsuarioResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String nome;
    private String login;
    private PerfilUsuario perfil;
    private StatusUsuario status;
    private Boolean conviteExpirado;
    private Boolean podeReenviarConvite;
    private Long segundosParaReenviarConvite;

    public UsuarioResponseDTO(){
    }

    public UsuarioResponseDTO(Usuario usuario){
        id = usuario.getId();
        nome = usuario.getNome();
        login = usuario.getLogin();
        perfil = usuario.getPerfil();
        status = usuario.getStatus();
        conviteExpirado = false;
        podeReenviarConvite = false;
        segundosParaReenviarConvite = 0L;
    }

    public UsuarioResponseDTO(Usuario usuario, Boolean conviteExpirado, Boolean podeReenviarConvite, Long segundosParaReenviarConvite){
        id = usuario.getId();
        nome = usuario.getNome();
        login = usuario.getLogin();
        perfil = usuario.getPerfil();
        status = usuario.getStatus();
        this.conviteExpirado = conviteExpirado;
        this.podeReenviarConvite = podeReenviarConvite;
        this.segundosParaReenviarConvite = segundosParaReenviarConvite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public StatusUsuario getStatus() {
        return status;
    }

    public void setStatus(StatusUsuario status) {
        this.status = status;
    }

    public Boolean getConviteExpirado() {
        return conviteExpirado;
    }

    public void setConviteExpirado(Boolean conviteExpirado) {
        this.conviteExpirado = conviteExpirado;
    }

    public Boolean getPodeReenviarConvite() {
        return podeReenviarConvite;
    }

    public void setPodeReenviarConvite(Boolean podeReenviarConvite) {
        this.podeReenviarConvite = podeReenviarConvite;
    }

    public Long getSegundosParaReenviarConvite() {
        return segundosParaReenviarConvite;
    }

    public void setSegundosParaReenviarConvite(Long segundosParaReenviarConvite) {
        this.segundosParaReenviarConvite = segundosParaReenviarConvite;
    }
}
