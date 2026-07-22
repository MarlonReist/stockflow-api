package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.enums.TipoMovimentacao;

import java.time.LocalDate;

public class DashboardMovimentacaoRecenteDTO {

    private Long id;
    private LocalDate dataMovimentacao;
    private TipoMovimentacao tipo;
    private String produtoNome;
    private String almoxarifadoNome;
    private Integer quantidade;
    private String origem;
    private Long idOrigem;

    public DashboardMovimentacaoRecenteDTO() {
    }

    public DashboardMovimentacaoRecenteDTO(Long id,
                                           LocalDate dataMovimentacao,
                                           TipoMovimentacao tipo,
                                           String produtoNome,
                                           String almoxarifadoNome,
                                           Integer quantidade,
                                           String origem,
                                           Long idOrigem) {
        this.id = id;
        this.dataMovimentacao = dataMovimentacao;
        this.tipo = tipo;
        this.produtoNome = produtoNome;
        this.almoxarifadoNome = almoxarifadoNome;
        this.quantidade = quantidade;
        this.origem = origem;
        this.idOrigem = idOrigem;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataMovimentacao() {
        return dataMovimentacao;
    }

    public void setDataMovimentacao(LocalDate dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimentacao tipo) {
        this.tipo = tipo;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    public String getAlmoxarifadoNome() {
        return almoxarifadoNome;
    }

    public void setAlmoxarifadoNome(String almoxarifadoNome) {
        this.almoxarifadoNome = almoxarifadoNome;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public Long getIdOrigem() {
        return idOrigem;
    }

    public void setIdOrigem(Long idOrigem) {
        this.idOrigem = idOrigem;
    }
}
