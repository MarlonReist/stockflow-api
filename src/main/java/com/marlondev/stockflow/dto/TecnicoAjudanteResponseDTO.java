package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.Usuario;

public class TecnicoAjudanteResponseDTO {

    private Long colaboradorId;
    private String colaboradorNome;
    private Long usuarioId;
    private String login;

    public TecnicoAjudanteResponseDTO() {
    }

    public TecnicoAjudanteResponseDTO(Usuario usuario) {
        usuarioId = usuario.getId();
        login = usuario.getLogin();
        if (usuario.getColaborador() != null) {
            colaboradorId = usuario.getColaborador().getId();
            colaboradorNome = usuario.getColaborador().getNome();
        }
    }

    public Long getColaboradorId() {
        return colaboradorId;
    }

    public String getColaboradorNome() {
        return colaboradorNome;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getLogin() {
        return login;
    }
}
