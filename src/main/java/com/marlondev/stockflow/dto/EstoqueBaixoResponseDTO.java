package com.marlondev.stockflow.dto;

import java.io.Serial;
import java.io.Serializable;

public class EstoqueBaixoResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long produtoId;
    private String produtoNome;
    private Integer estoqueMinimo;
    private Integer quantidadeAtual;
    private Long almoxarifadoId;
    private String almoxarifadoNome;

    public EstoqueBaixoResponseDTO() {
    }

    public EstoqueBaixoResponseDTO(Long produtoId, String produtoNome, Integer estoqueMinimo,
                                   Integer quantidadeAtual, Long almoxarifadoId, String almoxarifadoNome) {
        this.produtoId = produtoId;
        this.produtoNome = produtoNome;
        this.estoqueMinimo = estoqueMinimo;
        this.quantidadeAtual = quantidadeAtual;
        this.almoxarifadoId = almoxarifadoId;
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

    public Integer getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(Integer estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public Integer getQuantidadeAtual() {
        return quantidadeAtual;
    }

    public void setQuantidadeAtual(Integer quantidadeAtual) {
        this.quantidadeAtual = quantidadeAtual;
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
}