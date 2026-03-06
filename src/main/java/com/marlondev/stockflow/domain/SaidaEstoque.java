package com.marlondev.stockflow.domain;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name = "saida_estoque")
@Entity
public class SaidaEstoque implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dataSaida;
    @ManyToOne
    @JoinColumn(name = "almoxarifado_id", nullable = false)
    private Almoxarifado almoxarifado;

    @OneToMany(mappedBy = "saidaEstoque")
    private final List<SaidaItem> itens = new ArrayList<>();

    public SaidaEstoque(){
    }

    public SaidaEstoque(Long id, LocalDate dataSaida, Almoxarifado almoxarifado) {
        this.id = id;
        this.dataSaida = dataSaida;
        this.almoxarifado = almoxarifado;
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

    public Almoxarifado getAlmoxarifado() {
        return almoxarifado;
    }

    public void setAlmoxarifado(Almoxarifado almoxarifado) {
        this.almoxarifado = almoxarifado;
    }

    public List<SaidaItem> getItens() {
        return itens;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SaidaEstoque that = (SaidaEstoque) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

