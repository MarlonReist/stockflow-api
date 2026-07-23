package com.marlondev.stockflow.dto;

public class RecuperacaoSenhaValidacaoResponseDTO {

    private Boolean valido;
    private String nome;
    private String login;

    public RecuperacaoSenhaValidacaoResponseDTO(Boolean valido, String nome, String login) {
        this.valido = valido;
        this.nome = nome;
        this.login = login;
    }

    public Boolean getValido() {
        return valido;
    }

    public String getNome() {
        return nome;
    }

    public String getLogin() {
        return login;
    }
}
