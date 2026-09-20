package com.marlondev.stockflow.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

public class EntradaEstoqueRequestDTO {

    @NotNull(message = "Fornecedor é obrigatório!")
    private Long fornecedorId;
    @NotNull(message = "Almoxarifado é obrigatório!")
    private Long almoxarifadoId;
    private String numeroNotaFiscal;
    private LocalDate dataNotaFiscal;
    private LocalDate dataRecebimento;

    @PositiveOrZero(message = "Valor total da nota fiscal não pode ser negativo")
    private Double valorTotalNotaFiscal;

    public EntradaEstoqueRequestDTO(){
    }

    public EntradaEstoqueRequestDTO(Long fornecedorId, Long almoxarifadoId, String numeroNotaFiscal,
                                    LocalDate dataNotaFiscal, LocalDate dataRecebimento,
                                    Double valorTotalNotaFiscal) {
        this.fornecedorId = fornecedorId;
        this.almoxarifadoId = almoxarifadoId;
        this.numeroNotaFiscal = numeroNotaFiscal;
        this.dataNotaFiscal = dataNotaFiscal;
        this.dataRecebimento = dataRecebimento;
        this.valorTotalNotaFiscal = valorTotalNotaFiscal;
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

    public String getNumeroNotaFiscal() {
        return numeroNotaFiscal;
    }

    public void setNumeroNotaFiscal(String numeroNotaFiscal) {
        this.numeroNotaFiscal = numeroNotaFiscal;
    }

    public LocalDate getDataNotaFiscal() {
        return dataNotaFiscal;
    }

    public void setDataNotaFiscal(LocalDate dataNotaFiscal) {
        this.dataNotaFiscal = dataNotaFiscal;
    }

    public LocalDate getDataRecebimento() {
        return dataRecebimento;
    }

    public void setDataRecebimento(LocalDate dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
    }

    public Double getValorTotalNotaFiscal() {
        return valorTotalNotaFiscal;
    }

    public void setValorTotalNotaFiscal(Double valorTotalNotaFiscal) {
        this.valorTotalNotaFiscal = valorTotalNotaFiscal;
    }
}
