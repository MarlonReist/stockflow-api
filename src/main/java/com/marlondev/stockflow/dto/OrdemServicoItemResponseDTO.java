
package com.marlondev.stockflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.marlondev.stockflow.domain.OrdemServicoItem;

import java.io.Serial;
import java.io.Serializable;

public class OrdemServicoItemResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long osId;
    private Long produtoId;
    private String produtoNome;
    private Integer quantidade;
    private Double valorUnitario;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER, pattern = "#.00")
    private Double valorTotal;
    private Long almoxarifadoId;

    public OrdemServicoItemResponseDTO() {
    }

    public OrdemServicoItemResponseDTO(OrdemServicoItem ordemItem) {
        id = ordemItem.getId();
        osId = ordemItem.getOrdemDeServico().getId();
        produtoId = ordemItem.getProduto().getId();
        produtoNome = ordemItem.getProduto().getNome();
        quantidade = ordemItem.getQuantidade();
        valorUnitario = ordemItem.getValorUnitario();
        valorTotal = ordemItem.valorTotal();
        almoxarifadoId = ordemItem.getAlmoxarifado().getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOsId() {
        return osId;
    }

    public void setOsId(Long osId) {
        this.osId = osId;
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

    public Long getAlmoxarifadoId() {
        return almoxarifadoId;
    }

    public void setAlmoxarifadoId(Long almoxarifadoId) {
        this.almoxarifadoId = almoxarifadoId;
    }
}