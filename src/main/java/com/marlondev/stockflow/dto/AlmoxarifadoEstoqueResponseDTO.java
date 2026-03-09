package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.AlmoxarifadoEstoque;

import java.io.Serial;
import java.io.Serializable;

public class AlmoxarifadoEstoqueResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long almoxarifadoId;
    private String almoxarifadoNome;
    private Long produtoId;
    private String produtoNome;
    private Integer quantidade;

    public AlmoxarifadoEstoqueResponseDTO(){
    }

    public AlmoxarifadoEstoqueResponseDTO(AlmoxarifadoEstoque almoxarifadoEstoque){
        id = almoxarifadoEstoque.getId();
        almoxarifadoId = almoxarifadoEstoque.getAlmoxarifado().getId();
        almoxarifadoNome = almoxarifadoEstoque.getAlmoxarifado().getNome();
        produtoId = almoxarifadoEstoque.getProduto().getId();
        produtoNome = almoxarifadoEstoque.getProduto().getNome();
        quantidade = almoxarifadoEstoque.getQuantidade();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlmoxarifadoId() {
        return almoxarifadoId;
    }

    public void setAlmoxarifadoId(Long almoxarifadoId) {
        this.almoxarifadoId = almoxarifadoId;
    }

    public String getAlmoxarifadoNome() {
        return almoxarifadoNome;
    }

    public void setAlmoxarifadoNome(String almoxarifadoNome) {
        this.almoxarifadoNome = almoxarifadoNome;
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
