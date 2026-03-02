/*package com.marlondev.stockflow.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OrdemServicoItemRequestDTO {

    @NotNull(message = "Id da ordem de serviço é obrigatório!")
    @Positive
    private Long osId;

    @NotNull(message = "Id do produto é obrigatório!")
    @Positive
    private Long produtoId;

    @NotNull(message = "Quantidade é obrigatória!")
    @Positive(message = "Quantidade deve ser maior que 0!")
    private Integer quantidade;

    @NotNull(message = "Valor Unitário é obrigatório!")
    @Positive (message = "Valor Unitário deve ser maior que 0!")
    private Double valorUnitario;

    public OrdemServicoItemRequestDTO() {
    }

    public OrdemServicoItemRequestDTO(Long osId, Long produtoId, Integer quantidade, Double valorUnitario) {
        this.osId = osId;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
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