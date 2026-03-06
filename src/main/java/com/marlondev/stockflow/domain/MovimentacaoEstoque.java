package com.marlondev.stockflow.domain;

import com.marlondev.stockflow.domain.enums.TipoMovimentacao;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Table(name = "movimentacao_estoque")
@Entity
public class MovimentacaoEstoque implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dataMovimentacao;
    @Enumerated(value = EnumType.STRING)
    private TipoMovimentacao tipo;
    @ManyToOne
    @JoinColumn(name = "almoxarifado_id", nullable = false)
    private Almoxarifado almoxarifado;
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;
    @Column(nullable = false)
    private Integer quantidade;

    @ManyToOne
    @JoinColumn(name = "entrada_estoque_id")
    private EntradaEstoque entradaEstoque;
    @ManyToOne
    @JoinColumn(name = "saida_estoque_id")
    private SaidaEstoque saidaEstoque;
    @ManyToOne
    @JoinColumn(name = "transferencia_almoxarifado_id")
    private TransferenciaAlmoxarifado transferenciaAlmoxarifado;
    @ManyToOne
    @JoinColumn(name = "ordem_de_servico_id")
    private OrdemDeServico ordemDeServico;

    public MovimentacaoEstoque(){
    }

    public MovimentacaoEstoque(Long id, LocalDate dataMovimentacao, TipoMovimentacao tipo, Almoxarifado almoxarifado, Produto produto, Integer quantidade, EntradaEstoque entradaEstoque, SaidaEstoque saidaEstoque, TransferenciaAlmoxarifado transferenciaAlmoxarifado, OrdemDeServico ordemDeServico) {
        this.id = id;
        this.dataMovimentacao = dataMovimentacao;
        this.tipo = tipo;
        this.almoxarifado = almoxarifado;
        this.produto = produto;
        this.quantidade = quantidade;
        this.entradaEstoque = entradaEstoque;
        this.saidaEstoque = saidaEstoque;
        this.transferenciaAlmoxarifado = transferenciaAlmoxarifado;
        this.ordemDeServico = ordemDeServico;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataMovimentacao() {
        return dataMovimentacao;
    }

    public void setDataMovimentacao(LocalDate dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimentacao tipo) {
        this.tipo = tipo;
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

    public EntradaEstoque getEntradaEstoque() {
        return entradaEstoque;
    }

    public void setEntradaEstoque(EntradaEstoque entradaEstoque) {
        this.entradaEstoque = entradaEstoque;
    }

    public SaidaEstoque getSaidaEstoque() {
        return saidaEstoque;
    }

    public void setSaidaEstoque(SaidaEstoque saidaEstoque) {
        this.saidaEstoque = saidaEstoque;
    }

    public TransferenciaAlmoxarifado getTransferenciaAlmoxarifado() {
        return transferenciaAlmoxarifado;
    }

    public void setTransferenciaAlmoxarifado(TransferenciaAlmoxarifado transferenciaAlmoxarifado) {
        this.transferenciaAlmoxarifado = transferenciaAlmoxarifado;
    }

    public OrdemDeServico getOrdemDeServico() {
        return ordemDeServico;
    }

    public void setOrdemDeServico(OrdemDeServico ordemDeServico) {
        this.ordemDeServico = ordemDeServico;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MovimentacaoEstoque that = (MovimentacaoEstoque) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
