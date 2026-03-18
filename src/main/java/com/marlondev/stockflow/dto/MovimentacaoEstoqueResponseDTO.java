package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.MovimentacaoEstoque;
import com.marlondev.stockflow.domain.enums.TipoMovimentacao;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class MovimentacaoEstoqueResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private LocalDate dataMovimentacao;
    private TipoMovimentacao tipo;
    private Long almoxarifadoId;
    private String almoxarifadoNome;
    private Long produtoId;
    private String produtoNome;
    private Integer quantidade;
    private Long entradaEstoqueId;
    private Long saidaEstoqueId;
    private Long transferenciaAlmoxarifadoId;
    private Long ordemDeServicoId;

    public MovimentacaoEstoqueResponseDTO(MovimentacaoEstoque movimentacaoEstoque){
        id = movimentacaoEstoque.getId();
        dataMovimentacao = movimentacaoEstoque.getDataMovimentacao();
        tipo = movimentacaoEstoque.getTipo();
        almoxarifadoId = movimentacaoEstoque.getAlmoxarifado().getId();
        almoxarifadoNome = movimentacaoEstoque.getAlmoxarifado().getNome();
        produtoId = movimentacaoEstoque.getProduto().getId();
        produtoNome = movimentacaoEstoque.getProduto().getNome();
        quantidade = movimentacaoEstoque.getQuantidade();
        if (movimentacaoEstoque.getEntradaEstoque() != null){
            entradaEstoqueId = movimentacaoEstoque.getEntradaEstoque().getId();
        }
        if (movimentacaoEstoque.getSaidaEstoque() != null) {
            saidaEstoqueId = movimentacaoEstoque.getSaidaEstoque().getId();
        }
        if (movimentacaoEstoque.getTransferenciaAlmoxarifado() != null) {
            transferenciaAlmoxarifadoId = movimentacaoEstoque.getTransferenciaAlmoxarifado().getId();
        }
        if (movimentacaoEstoque.getOrdemDeServico() != null) {
            ordemDeServicoId = movimentacaoEstoque.getOrdemDeServico().getId();
        }
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

    public Long getEntradaEstoqueId() {
        return entradaEstoqueId;
    }

    public void setEntradaEstoqueId(Long entradaEstoqueId) {
        this.entradaEstoqueId = entradaEstoqueId;
    }

    public Long getSaidaEstoqueId() {
        return saidaEstoqueId;
    }

    public void setSaidaEstoqueId(Long saidaEstoqueId) {
        this.saidaEstoqueId = saidaEstoqueId;
    }

    public Long getTransferenciaAlmoxarifadoId() {
        return transferenciaAlmoxarifadoId;
    }

    public void setTransferenciaAlmoxarifadoId(Long transferenciaAlmoxarifadoId) {
        this.transferenciaAlmoxarifadoId = transferenciaAlmoxarifadoId;
    }

    public Long getOrdemDeServicoId() {
        return ordemDeServicoId;
    }

    public void setOrdemDeServicoId(Long ordemDeServicoId) {
        this.ordemDeServicoId = ordemDeServicoId;
    }
}
