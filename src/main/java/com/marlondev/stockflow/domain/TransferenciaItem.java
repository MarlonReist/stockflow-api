package com.marlondev.stockflow.domain;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Table (name = "transferencia_item")
@Entity
public class TransferenciaItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "transferencia_id", nullable = false)
    private TransferenciaAlmoxarifado transferencia;
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;
    private Integer quantidade;

    public TransferenciaItem(){
    }

    public TransferenciaItem(Long id, TransferenciaAlmoxarifado transferencia, Produto produto, Integer quantidade) {
        this.id = id;
        this.transferencia = transferencia;
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransferenciaAlmoxarifado getTransferencia() {
        return transferencia;
    }

    public void setTransferencia(TransferenciaAlmoxarifado transferencia) {
        this.transferencia = transferencia;
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
        TransferenciaItem that = (TransferenciaItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
