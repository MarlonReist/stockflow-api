package com.marlondev.stockflow.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TransferenciaItemRequestDTO {

    @NotNull(message = "Id da transferência é obrigatório!")
    private Long transferenciaId;
    @NotNull(message = "Id do produto é obrigatório!")
    private Long produtoId;
    @NotNull(message = "Quantidade é obrigatória!")
    @Positive(message = "Quantidade precisa ser maior que 0!")
    private Integer quantidade;

    public TransferenciaItemRequestDTO(){
    }

    public TransferenciaItemRequestDTO(Long transferenciaId, Long produtoId, Integer quantidade) {
        this.transferenciaId = transferenciaId;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
    }

    public Long getTransferenciaId() {
        return transferenciaId;
    }

    public void setTransferenciaId(Long transferenciaId) {
        this.transferenciaId = transferenciaId;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
