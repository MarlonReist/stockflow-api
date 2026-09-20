package com.marlondev.stockflow.domain;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Table(name = "conferencia_estoque_item")
@Entity
public class ConferenciaEstoqueItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conferencia_id", nullable = false)
    private ConferenciaEstoque conferencia;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    private Integer quantidadeEsperada;
    private Integer quantidadeContada;

    public ConferenciaEstoqueItem() {
    }

    public ConferenciaEstoqueItem(Long id, ConferenciaEstoque conferencia, Produto produto,
                                  Integer quantidadeEsperada, Integer quantidadeContada) {
        this.id = id;
        this.conferencia = conferencia;
        this.produto = produto;
        this.quantidadeEsperada = quantidadeEsperada;
        this.quantidadeContada = quantidadeContada;
    }

    public Integer divergencia() {
        if (quantidadeContada == null) {
            return null;
        }
        return quantidadeContada - quantidadeEsperada;
    }

    public boolean possuiDivergencia() {
        Integer divergencia = divergencia();
        return divergencia != null && divergencia != 0;
    }

    public Long getId() {
        return id;
    }

    public ConferenciaEstoque getConferencia() {
        return conferencia;
    }

    public void setConferencia(ConferenciaEstoque conferencia) {
        this.conferencia = conferencia;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Integer getQuantidadeEsperada() {
        return quantidadeEsperada;
    }

    public void setQuantidadeEsperada(Integer quantidadeEsperada) {
        this.quantidadeEsperada = quantidadeEsperada;
    }

    public Integer getQuantidadeContada() {
        return quantidadeContada;
    }

    public void setQuantidadeContada(Integer quantidadeContada) {
        this.quantidadeContada = quantidadeContada;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ConferenciaEstoqueItem that = (ConferenciaEstoqueItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}