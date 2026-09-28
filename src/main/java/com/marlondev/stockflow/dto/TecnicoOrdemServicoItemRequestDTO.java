package com.marlondev.stockflow.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TecnicoOrdemServicoItemRequestDTO {

    @NotNull(message = "Id do produto é obrigatório!")
    private Long produtoId;

    @NotNull(message = "Almoxarifado é obrigatório!")
    private Long almoxarifadoId;

    @NotNull(message = "Quantidade é obrigatória!")
    @Positive(message = "Quantidade deve ser maior que 0!")
    private Integer quantidade;

    public TecnicoOrdemServicoItemRequestDTO() {
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public Long getAlmoxarifadoId() {
        return almoxarifadoId;
    }

    public void setAlmoxarifadoId(Long almoxarifadoId) {
        this.almoxarifadoId = almoxarifadoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}