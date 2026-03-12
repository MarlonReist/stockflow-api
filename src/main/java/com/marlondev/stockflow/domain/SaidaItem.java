package com.marlondev.stockflow.domain;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Table (name = "saida_item")
@Entity
public class SaidaItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "saida_estoque_id", nullable = false)
    private SaidaEstoque saidaEstoque;
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;
    private Integer quantidade;
    private Double valorUnitario;

    public SaidaItem(){
    }

    public SaidaItem(Long id, SaidaEstoque saidaEstoque, Produto produto, Integer quantidade, Double valorUnitario) {
        this.id = id;
        this.saidaEstoque = saidaEstoque;
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

    public SaidaEstoque getSaidaEstoque() {
        return saidaEstoque;
    }

    public void setSaidaEstoque(SaidaEstoque saidaEstoque) {
        this.saidaEstoque = saidaEstoque;
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
        SaidaItem that = (SaidaItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
