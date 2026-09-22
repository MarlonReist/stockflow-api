package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.Usuario;
import com.marlondev.stockflow.domain.enums.PerfilUsuario;
import com.marlondev.stockflow.domain.enums.StatusUsuario;

public class MeuPerfilResponseDTO {

    private Long id;
    private String nome;
    private String login;
    private PerfilUsuario perfil;
    private StatusUsuario status;
    private Long colaboradorId;
    private String colaboradorNome;

    public MeuPerfilResponseDTO() {
    }

    public MeuPerfilResponseDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.login = usuario.getLogin();
        this.perfil = usuario.getPerfil();
        this.status = usuario.getStatus();
        if (usuario.getColaborador() != null) {
            this.colaboradorId = usuario.getColaborador().getId();
            this.colaboradorNome = usuario.getColaborador().getNome();
        }
    }

    public Long getId() {
        return id;
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

    public Long getColaboradorId() {
        return colaboradorId;
    }

    public String getColaboradorNome() {
        return colaboradorNome;
    }
}