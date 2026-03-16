package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.TransferenciaItem;

import java.io.Serial;
import java.io.Serializable;

public class TransferenciaItemResponseDTO implements Serializable {
    @Serial
    private static final Long serialVersionUID = 1L;

    private Long id;
    private Long transferenciaId;
    private Long produtoId;
    private String produtoNome;
    private Integer quantidade;

    public TransferenciaItemResponseDTO(){
    }

    public TransferenciaItemResponseDTO(TransferenciaItem transfItem){
        id = transfItem.getId();
        transferenciaId = transfItem.getTransferencia().getId();
        produtoId = transfItem.getProduto().getId();
        produtoNome = transfItem.getProduto().getNome();
        quantidade = transfItem.getQuantidade();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
