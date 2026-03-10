package com.marlondev.stockflow.domain;

import com.marlondev.stockflow.domain.enums.StatusEnum;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table (name = "entrada_estoque")
@Entity
public class EntradaEstoque implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn (name = "fornecedor_id", nullable = false)
    private Fornecedor fornecedor;
    @ManyToOne
    @JoinColumn (name = "almoxarifado_id", nullable = false)
    private Almoxarifado almoxarifado;
    private LocalDate dataEntrada;
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @OneToMany (mappedBy = "entradaEstoque")
    private final List<EntradaItem> itens = new ArrayList<>();

    public EntradaEstoque(){
    }

    public EntradaEstoque(Long id, Fornecedor fornecedor, LocalDate dataEntrada, Almoxarifado almoxarifado, StatusEnum status) {
        this.id = id;
        this.fornecedor = fornecedor;
        this.dataEntrada = dataEntrada;
        this.almoxarifado = almoxarifado;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public LocalDate getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDate dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public Almoxarifado getAlmoxarifado() {
        return almoxarifado;
    }

    public void setAlmoxarifado(Almoxarifado almoxarifado) {
        this.almoxarifado = almoxarifado;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public List<EntradaItem> getItens() {
        return itens;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EntradaEstoque that = (EntradaEstoque) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
