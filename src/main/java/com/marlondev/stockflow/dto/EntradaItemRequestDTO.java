package com.marlondev.stockflow.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class EntradaItemRequestDTO {
    @NotNull(message = "Entrada não pode ser nula!")
    private Long entradaEstoqueId;
    @NotNull(message = "Produto não pode ser nulo!")
    private Long produtoId;
    @Positive
    @NotNull(message = "Quantidade não pode ser nula!")
    private Integer quantidade;
    @Positive
    private Double valorUnitario;

    public EntradaItemRequestDTO(){
    }

    public EntradaItemRequestDTO(Long entradaEstoqueId, Long produtoId, Integer quantidade, Double valorUnitario) {
        this.entradaEstoqueId = entradaEstoqueId;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
    }

    public Long getEntradaEstoqueId() {
        return entradaEstoqueId;
    }

    public void setEntradaEstoqueId(Long entradaEstoqueId) {
        this.entradaEstoqueId = entradaEstoqueId;
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

    public Double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(Double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }
}
