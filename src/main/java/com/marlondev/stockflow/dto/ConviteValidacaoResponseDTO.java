package com.marlondev.stockflow.dto;

public class ConviteValidacaoResponseDTO {

    private Boolean valido;
    private String nome;
    private String login;

    public ConviteValidacaoResponseDTO() {
    }

    public ConviteValidacaoResponseDTO(Boolean valido, String nome, String login) {
        this.valido = valido;
        this.nome = nome;
        this.login = login;
    }

    public Boolean getValido() {
        return valido;
    }

    public void setValido(Boolean valido) {
        this.valido = valido;
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
}