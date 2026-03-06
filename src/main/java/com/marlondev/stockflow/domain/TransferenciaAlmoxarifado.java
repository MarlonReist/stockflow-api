package com.marlondev.stockflow.domain;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name = "transferencia_almoxarifado")
@Entity
public class TransferenciaAlmoxarifado implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dataTransferencia;
    @ManyToOne
    @JoinColumn(name = "almoxarifado_origem_id", nullable = false)
    private Almoxarifado almoxarifadoOrigem;
    @ManyToOne
    @JoinColumn(name = "almoxarifado_destino_id", nullable = false)
    private Almoxarifado almoxarifadoDestino;
    private String observacao;

    @OneToMany(mappedBy = "transferencia")
    private final List<TransferenciaItem> itens = new ArrayList<>();

    public TransferenciaAlmoxarifado() {
    }

    public TransferenciaAlmoxarifado(Long id, LocalDate dataTransferencia, Almoxarifado almoxarifadoOrigem, Almoxarifado almoxarifadoDestino, String observacao) {
        this.id = id;
        this.dataTransferencia = dataTransferencia;
        this.almoxarifadoOrigem = almoxarifadoOrigem;
        this.almoxarifadoDestino = almoxarifadoDestino;
        this.observacao = observacao;
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

    public Almoxarifado getAlmoxarifadoOrigem() {
        return almoxarifadoOrigem;
    }

    public void setAlmoxarifadoOrigem(Almoxarifado almoxarifadoOrigem) {
        this.almoxarifadoOrigem = almoxarifadoOrigem;
    }

    public Almoxarifado getAlmoxarifadoDestino() {
        return almoxarifadoDestino;
    }

    public void setAlmoxarifadoDestino(Almoxarifado almoxarifadoDestino) {
        this.almoxarifadoDestino = almoxarifadoDestino;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public List<TransferenciaItem> getItens() {
        return itens;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TransferenciaAlmoxarifado that = (TransferenciaAlmoxarifado) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
