package com.marlondev.stockflow.domain;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Table (name = "entrada_item",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"entrada_estoque_id", "produto_id"})
    })
@Entity
public class EntradaItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "entrada_estoque_id", nullable = false)
    private EntradaEstoque entradaEstoque;
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;
    private Integer quantidade;
    private Double valorUnitario;

    public EntradaItem(){
    }

    public EntradaItem(Long id, EntradaEstoque entradaEstoque, Produto produto, Integer quantidade, Double valorUnitario) {
        this.id = id;
        this.entradaEstoque = entradaEstoque;
        this.produto = produto;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EntradaEstoque getEntradaEstoque() {
        return entradaEstoque;
    }

    public void setEntradaEstoque(EntradaEstoque entradaEstoque) {
        this.entradaEstoque = entradaEstoque;
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

    public Double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(Double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EntradaItem that = (EntradaItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
