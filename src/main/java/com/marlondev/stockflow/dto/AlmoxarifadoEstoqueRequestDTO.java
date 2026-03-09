package com.marlondev.stockflow.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AlmoxarifadoEstoqueRequestDTO {

    @NotNull(message = "Almoxarifado é obrigatório!")
    private Long almoxarifadoId;
    @NotNull(message = "Produto é obrigatório!")
    private Long produtoId;
    @NotNull(message = "Quantidade é obrigatória!")
    @Positive(message = "Quantidade precisa ser maior que 0!")
    private Integer quantidade;

    public AlmoxarifadoEstoqueRequestDTO(){
    }

    public AlmoxarifadoEstoqueRequestDTO(Long almoxarifadoId, Long produtoId, Integer quantidade) {
        this.almoxarifadoId = almoxarifadoId;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
    }

    public Long getAlmoxarifadoId() {
        return almoxarifadoId;
    }

    public void setAlmoxarifadoId(Long almoxarifadoId) {
        this.almoxarifadoId = almoxarifadoId;
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
