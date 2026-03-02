/*
package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.OrdemServicoItem;

import java.io.Serial;
import java.io.Serializable;

public class OrdemServicoItemResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private OrdemDeServicoResponseDTO os;
    private ProdutoResponseDTO produto;
    private Integer quantidade;
    private Double valorUnitario;

    public OrdemServicoItemResponseDTO(){
    }

    public OrdemServicoItemResponseDTO(OrdemServicoItem ordemItem){
        id = ordemItem.getId();
        os = new OrdemDeServicoResponseDTO(ordemItem.getOrdemDeServico());
        produto = new ProdutoResponseDTO(ordemItem.getProduto());
        quantidade = ordemItem.getQuantidade();
        valorUnitario = ordemItem.getValorUnitario();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrdemDeServicoResponseDTO getOs() {
        return os;
    }

    public void setOrdemDeServico(OrdemDeServicoResponseDTO os) {
        this.os = os;
    }

    public ProdutoResponseDTO getProduto() {
        return produto;
    }

    public void setProduto(ProdutoResponseDTO produto) {
        this.produto = produto;
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
*/