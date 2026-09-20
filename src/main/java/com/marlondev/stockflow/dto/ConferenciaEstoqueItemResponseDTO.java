package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.ConferenciaEstoqueItem;

import java.io.Serial;
import java.io.Serializable;

public class ConferenciaEstoqueItemResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long produtoId;
    private String produtoNome;
    private Integer quantidadeEsperada;
    private Integer quantidadeContada;
    private Integer divergencia;
    private boolean possuiDivergencia;

    public ConferenciaEstoqueItemResponseDTO() {
    }

    public ConferenciaEstoqueItemResponseDTO(ConferenciaEstoqueItem item) {
        id = item.getId();
        produtoId = item.getProduto().getId();
        produtoNome = item.getProduto().getNome();
        quantidadeEsperada = item.getQuantidadeEsperada();
        quantidadeContada = item.getQuantidadeContada();
        divergencia = item.divergencia();
        possuiDivergencia = item.possuiDivergencia();
    }

    public Long getId() {
        return id;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public Integer getQuantidadeEsperada() {
        return quantidadeEsperada;
    }

    public Integer getQuantidadeContada() {
        return quantidadeContada;
    }

    public Integer getDivergencia() {
        return divergencia;
    }

    public boolean isPossuiDivergencia() {
        return possuiDivergencia;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    public void setQuantidadeEsperada(Integer quantidadeEsperada) {
        this.quantidadeEsperada = quantidadeEsperada;
    }

    public void setQuantidadeContada(Integer quantidadeContada) {
        this.quantidadeContada = quantidadeContada;
    }

    public void setDivergencia(Integer divergencia) {
        this.divergencia = divergencia;
    }

    public void setPossuiDivergencia(boolean possuiDivergencia) {
        this.possuiDivergencia = possuiDivergencia;
    }
}