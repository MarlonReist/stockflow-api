package com.marlondev.stockflow.dto;

public class EsqueciSenhaResponseDTO {

    private Boolean solicitado;
    private String mensagem;
    private String token;

    public EsqueciSenhaResponseDTO(Boolean solicitado, String mensagem, String token) {
        this.solicitado = solicitado;
        this.mensagem = mensagem;
        this.token = token;
    }

    public Boolean getSolicitado() {
        return solicitado;
    }

    public String getMensagem() {
        return mensagem;
    }

    public String getToken() {
        return token;
    }
}
