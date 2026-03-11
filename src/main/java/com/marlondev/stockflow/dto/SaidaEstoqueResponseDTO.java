package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.SaidaEstoque;
import com.marlondev.stockflow.domain.enums.StatusEnum;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class SaidaEstoqueResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private LocalDate dataSaida;
    private Long almoxarifadoId;
    private String almoxarifadoNome;
    private StatusEnum status;

    public SaidaEstoqueResponseDTO(){
    }

    public SaidaEstoqueResponseDTO(SaidaEstoque saidaEstoque){
        id = saidaEstoque.getId();
        dataSaida = saidaEstoque.getDataSaida();
        almoxarifadoId = saidaEstoque.getAlmoxarifado().getId();
        almoxarifadoNome = saidaEstoque.getAlmoxarifado().getNome();
        status = saidaEstoque.getStatus();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(LocalDate dataSaida) {
        this.dataSaida = dataSaida;
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
}
