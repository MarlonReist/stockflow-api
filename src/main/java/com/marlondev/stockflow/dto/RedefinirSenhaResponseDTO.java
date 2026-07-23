package com.marlondev.stockflow.dto;

public class RedefinirSenhaResponseDTO {

    private Boolean sucesso;
    private String mensagem;

    public RedefinirSenhaResponseDTO(Boolean sucesso, String mensagem) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
    }

    public Boolean getSucesso() {
        return sucesso;
    }

    public String getMensagem() {
        return mensagem;
    }
}
