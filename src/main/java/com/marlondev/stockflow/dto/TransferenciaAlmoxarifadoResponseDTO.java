package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.TransferenciaAlmoxarifado;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class TransferenciaAlmoxarifadoResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private LocalDate dataTransferencia;
    private Long almoxarifadoOrigemId;
    private String almoxarifadoOrigemNome;
    private Long almoxarifadoDestinoId;
    private String almoxarifadoDestinoNome;
    private String observacao;

    public TransferenciaAlmoxarifadoResponseDTO(){
    }

    public TransferenciaAlmoxarifadoResponseDTO(TransferenciaAlmoxarifado transf){
        id = transf.getId();
        dataTransferencia = transf.getDataTransferencia();
        almoxarifadoOrigemId = transf.getAlmoxarifadoOrigem().getId();
        almoxarifadoOrigemNome = transf.getAlmoxarifadoOrigem().getNome();
        almoxarifadoDestinoId = transf.getAlmoxarifadoDestino().getId();
        almoxarifadoDestinoNome = transf.getAlmoxarifadoDestino().getNome();
        observacao = transf.getObservacao();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataTransferencia() {
        return dataTransferencia;
    }

    public void setDataTransferencia(LocalDate dataTransferencia) {
        this.dataTransferencia = dataTransferencia;
    }

    public Long getAlmoxarifadoOrigemId() {
        return almoxarifadoOrigemId;
    }

    public void setAlmoxarifadoOrigemId(Long almoxarifadoOrigemId) {
        this.almoxarifadoOrigemId = almoxarifadoOrigemId;
    }

    public String getAlmoxarifadoOrigemNome() {
        return almoxarifadoOrigemNome;
    }

    public void setAlmoxarifadoOrigemNome(String almoxarifadoOrigemNome) {
        this.almoxarifadoOrigemNome = almoxarifadoOrigemNome;
    }

    public Long getAlmoxarifadoDestinoId() {
        return almoxarifadoDestinoId;
    }

    public void setAlmoxarifadoDestinoId(Long almoxarifadoDestinoId) {
        this.almoxarifadoDestinoId = almoxarifadoDestinoId;
    }

    public String getAlmoxarifadoDestinoNome() {
        return almoxarifadoDestinoNome;
    }

    public void setAlmoxarifadoDestinoNome(String almoxarifadoDestinoNome) {
        this.almoxarifadoDestinoNome = almoxarifadoDestinoNome;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
