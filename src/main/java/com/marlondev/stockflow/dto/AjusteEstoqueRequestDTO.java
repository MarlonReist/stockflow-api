package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.enums.TipoAjusteEstoque;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AjusteEstoqueRequestDTO {

    @NotNull(message = "Tipo de ajuste é obrigatório!")
    private TipoAjusteEstoque tipo;

    @NotNull(message = "Almoxarifado é obrigatório!")
    private Long almoxarifadoId;

    @NotNull(message = "Produto é obrigatório!")
    private Long produtoId;

    @NotNull(message = "Quantidade é obrigatória!")
    @Positive(message = "Quantidade deve ser maior que zero!")
    private Integer quantidade;

    @NotBlank(message = "Motivo é obrigatório!")
    private String motivo;

    public AjusteEstoqueRequestDTO() {
    }

    public AjusteEstoqueRequestDTO(TipoAjusteEstoque tipo, Long almoxarifadoId, Long produtoId,
                                   Integer quantidade, String motivo) {
        this.tipo = tipo;
        this.almoxarifadoId = almoxarifadoId;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.motivo = motivo;
    }

    public TipoAjusteEstoque getTipo() {
        return tipo;
    }

    public Long getAlmoxarifadoId() {
        return almoxarifadoId;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setTipo(TipoAjusteEstoque tipo) {
        this.tipo = tipo;
    }

    public void setAlmoxarifadoId(Long almoxarifadoId) {
        this.almoxarifadoId = almoxarifadoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}