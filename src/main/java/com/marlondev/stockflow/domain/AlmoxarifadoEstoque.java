package com.marlondev.stockflow.domain;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Table (name = "almoxarifado_estoque",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"almoxarifado_id", "produto_id"})
        })
@Entity
public class AlmoxarifadoEstoque implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "almoxarifado_id", nullable = false)
    private Almoxarifado almoxarifado;
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;
    private Integer quantidade;

    public AlmoxarifadoEstoque(){
    }

    public AlmoxarifadoEstoque(Long id, Almoxarifado almoxarifado, Produto produto, Integer quantidade) {
        this.id = id;
        this.almoxarifado = almoxarifado;
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Almoxarifado getAlmoxarifado() {
        return almoxarifado;
    }

    public void setAlmoxarifado(Almoxarifado almoxarifado) {
        this.almoxarifado = almoxarifado;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AlmoxarifadoEstoque that = (AlmoxarifadoEstoque) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
