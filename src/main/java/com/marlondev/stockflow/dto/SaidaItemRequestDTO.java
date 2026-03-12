package com.marlondev.stockflow.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class SaidaItemRequestDTO {
    @NotNull(message = "Saída é obrigatória!")
    private Long saidaEstoqueId;
    @NotNull(message = "Produto é obrigatório!")
    private Long produtoId;
    @NotNull(message = "Quantidade é obrigatoria!")
    @Positive
    private Integer quantidade;

    public SaidaItemRequestDTO(){
    }

    public SaidaItemRequestDTO(Long saidaEstoqueId, Long produtoId, Integer quantidade) {
        this.saidaEstoqueId = saidaEstoqueId;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
    }

    public Long getSaidaEstoqueId() {
        return saidaEstoqueId;
    }

    public void setSaidaEstoqueId(Long saidaEstoqueId) {
        this.saidaEstoqueId = saidaEstoqueId;
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
