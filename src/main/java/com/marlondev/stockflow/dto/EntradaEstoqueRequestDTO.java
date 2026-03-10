package com.marlondev.stockflow.dto;

import jakarta.validation.constraints.NotNull;

public class EntradaEstoqueRequestDTO {

    @NotNull(message = "Fornecedor é obrigatório!")
    private Long fornecedorId;
    @NotNull(message = "Almoxarifado é obrigatório!")
    private Long almoxarifadoId;

    public EntradaEstoqueRequestDTO(){
    }

    public EntradaEstoqueRequestDTO(Long fornecedorId, Long almoxarifadoId) {
        this.fornecedorId = fornecedorId;
        this.almoxarifadoId = almoxarifadoId;
    }

    public Long getFornecedorId() {
        return fornecedorId;
    }

    public void setFornecedorId(Long fornecedorId) {
        this.fornecedorId = fornecedorId;
    }

    public Long getAlmoxarifadoId() {
        return almoxarifadoId;
    }

    public void setAlmoxarifadoId(Long almoxarifadoId) {
        this.almoxarifadoId = almoxarifadoId;
    }
}
