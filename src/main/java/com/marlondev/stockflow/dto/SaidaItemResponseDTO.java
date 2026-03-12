package com.marlondev.stockflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.marlondev.stockflow.domain.SaidaItem;

import java.io.Serial;
import java.io.Serializable;

public class SaidaItemResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long saidaEstoqueId;
    private Long produtoId;
    private String produtoNome;
    private Integer quantidade;
    private Double valorUnitario;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER, pattern = "#.00")
    private Double valorTotal;

    public SaidaItemResponseDTO(){
    }

    public SaidaItemResponseDTO(SaidaItem saidaItem){
        id = saidaItem.getId();
        saidaEstoqueId = saidaItem.getSaidaEstoque().getId();
        produtoId = saidaItem.getProduto().getId();
        produtoNome = saidaItem.getProduto().getNome();
        quantidade = saidaItem.getQuantidade();
        valorUnitario = saidaItem.getValorUnitario();
        valorTotal = Math.round((saidaItem.getQuantidade() * saidaItem.getValorUnitario()) * 100.0) / 100.0;;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
