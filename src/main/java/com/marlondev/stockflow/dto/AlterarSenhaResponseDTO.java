package com.marlondev.stockflow.dto;

public class AlterarSenhaResponseDTO {

    private Boolean sucesso;
    private String mensagem;

    public AlterarSenhaResponseDTO() {
    }

    public AlterarSenhaResponseDTO(Boolean sucesso, String mensagem) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
    }

    public Boolean getSucesso() {
        return sucesso;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setSucesso(Boolean sucesso) {
        this.sucesso = sucesso;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}