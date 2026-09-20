package com.marlondev.stockflow.domain;

import com.marlondev.stockflow.domain.enums.TipoAjusteEstoque;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Table(name = "ajuste_estoque")
@Entity
public class AjusteEstoque implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    private TipoAjusteEstoque tipo;

    @ManyToOne
    @JoinColumn(name = "almoxarifado_id", nullable = false)
    private Almoxarifado almoxarifado;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    private Integer quantidade;

    @Column(nullable = false)
    private String motivo;

    @ManyToOne
    @JoinColumn(name = "usuario_responsavel_id", nullable = false)
    private Usuario usuarioResponsavel;

    @ManyToOne
    @JoinColumn(name = "conferencia_estoque_id")
    private ConferenciaEstoque conferenciaEstoque;

    @ManyToOne
    @JoinColumn(name = "conferencia_estoque_item_id")
    private ConferenciaEstoqueItem conferenciaEstoqueItem;

    public AjusteEstoque() {
    }

    public AjusteEstoque(Long id, LocalDateTime dataHora, TipoAjusteEstoque tipo, Almoxarifado almoxarifado,
                         Produto produto, Integer quantidade, String motivo, Usuario usuarioResponsavel,
                         ConferenciaEstoque conferenciaEstoque, ConferenciaEstoqueItem conferenciaEstoqueItem) {
        this.id = id;
        this.dataHora = dataHora;
        this.tipo = tipo;
        this.almoxarifado = almoxarifado;
        this.produto = produto;
        this.quantidade = quantidade;
        this.motivo = motivo;
        this.usuarioResponsavel = usuarioResponsavel;
        this.conferenciaEstoque = conferenciaEstoque;
        this.conferenciaEstoqueItem = conferenciaEstoqueItem;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public TipoAjusteEstoque getTipo() {
        return tipo;
    }

    public Almoxarifado getAlmoxarifado() {
        return almoxarifado;
    }

    public Produto getProduto() {
        return produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public String getMotivo() {
        return motivo;
    }

    public Usuario getUsuarioResponsavel() {
        return usuarioResponsavel;
    }

    public ConferenciaEstoque getConferenciaEstoque() {
        return conferenciaEstoque;
    }

    public ConferenciaEstoqueItem getConferenciaEstoqueItem() {
        return conferenciaEstoqueItem;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public void setTipo(TipoAjusteEstoque tipo) {
        this.tipo = tipo;
    }

    public void setAlmoxarifado(Almoxarifado almoxarifado) {
        this.almoxarifado = almoxarifado;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public void setUsuarioResponsavel(Usuario usuarioResponsavel) {
        this.usuarioResponsavel = usuarioResponsavel;
    }

    public void setConferenciaEstoque(ConferenciaEstoque conferenciaEstoque) {
        this.conferenciaEstoque = conferenciaEstoque;
    }

    public void setConferenciaEstoqueItem(ConferenciaEstoqueItem conferenciaEstoqueItem) {
        this.conferenciaEstoqueItem = conferenciaEstoqueItem;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AjusteEstoque that = (AjusteEstoque) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}