package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.EntradaEstoque;
import com.marlondev.stockflow.domain.enums.StatusEnum;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class EntradaEstoqueResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private LocalDate dataEntrada;
    private Long fornecedorId;
    private String fornecedorNome;
    private Long almoxarifadoId;
    private String almoxarifadoNome;
    private StatusEnum status;
    private Double valorTotal;
    private String numeroNotaFiscal;
    private LocalDate dataNotaFiscal;
    private LocalDate dataRecebimento;
    private Double valorTotalNotaFiscal;
    private Double totalCalculadoItens;
    private Double diferencaValor;

    public EntradaEstoqueResponseDTO(){
    }

    public EntradaEstoqueResponseDTO(EntradaEstoque entradaEstoque){
        id = entradaEstoque.getId();
        dataEntrada = entradaEstoque.getDataEntrada();
        fornecedorId = entradaEstoque.getFornecedor().getId();
        fornecedorNome = entradaEstoque.getFornecedor().getNome();
        almoxarifadoId = entradaEstoque.getAlmoxarifado().getId();
        almoxarifadoNome = entradaEstoque.getAlmoxarifado().getNome();
        status = entradaEstoque.getStatus();
        totalCalculadoItens = Math.round(
                entradaEstoque.getItens()
                        .stream()
                        .mapToDouble(item -> item.valorTotal())
                        .sum() * 100.0
        ) / 100.0;

        valorTotal = totalCalculadoItens;

        numeroNotaFiscal = entradaEstoque.getNumeroNotaFiscal();
        dataNotaFiscal = entradaEstoque.getDataNotaFiscal();
        dataRecebimento = entradaEstoque.getDataRecebimento();
        valorTotalNotaFiscal = entradaEstoque.getValorTotalNotaFiscal();

        if (valorTotalNotaFiscal != null) {
            diferencaValor = Math.round((valorTotalNotaFiscal - totalCalculadoItens) * 100.0) / 100.0;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDate dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public Long getFornecedorId() {
        return fornecedorId;
    }

    public void setFornecedorId(Long fornecedorId) {
        this.fornecedorId = fornecedorId;
    }

    public String getFornecedorNome() {
        return fornecedorNome;
    }

    public void setFornecedorNome(String fornecedorNome) {
        this.fornecedorNome = fornecedorNome;
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

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
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

    public Double getTotalCalculadoItens() {
        return totalCalculadoItens;
    }

    public void setTotalCalculadoItens(Double totalCalculadoItens) {
        this.totalCalculadoItens = totalCalculadoItens;
    }

    public Double getDiferencaValor() {
        return diferencaValor;
    }

    public void setDiferencaValor(Double diferencaValor) {
        this.diferencaValor = diferencaValor;
    }
}
