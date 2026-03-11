
package com.marlondev.stockflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.marlondev.stockflow.domain.EntradaItem;

import java.io.Serial;
import java.io.Serializable;

public class EntradaItemResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long entradaEstoqueId;
    private Long produtoId;
    private String produtoNome;
    private Integer quantidade;
    private Double valorUnitario;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER, pattern = "#.00")
    private Double valorTotal;

    public EntradaItemResponseDTO() {
    }

    public EntradaItemResponseDTO(EntradaItem entradaItem) {
        id = entradaItem.getId();
        entradaEstoqueId = entradaItem.getEntradaEstoque().getId();
        produtoId = entradaItem.getProduto().getId();
        produtoNome = entradaItem.getProduto().getNome();
        quantidade = entradaItem.getQuantidade();
        valorUnitario = entradaItem.getValorUnitario();
        this.valorTotal = Math.round(entradaItem.valorTotal() * 100.0) / 100.0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getProdutoNome() {
        return produtoNome;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
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

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }
}