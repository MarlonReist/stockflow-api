package com.marlondev.stockflow.domain;

import com.marlondev.stockflow.domain.enums.StatusEnum;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name = "conferencia_estoque")
@Entity
public class ConferenciaEstoque implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "almoxarifado_id", nullable = false)
    private Almoxarifado almoxarifado;

    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFinalizacao;

    @ManyToOne
    @JoinColumn(name = "usuario_responsavel_id", nullable = false)
    private Usuario usuarioResponsavel;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @OneToMany(mappedBy = "conferencia")
    private final List<ConferenciaEstoqueItem> itens = new ArrayList<>();

    public ConferenciaEstoque() {
    }

    public ConferenciaEstoque(Long id, Almoxarifado almoxarifado, LocalDateTime dataHoraInicio,
                              LocalDateTime dataHoraFinalizacao, Usuario usuarioResponsavel,
                              StatusEnum status) {
        this.id = id;
        this.almoxarifado = almoxarifado;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFinalizacao = dataHoraFinalizacao;
        this.usuarioResponsavel = usuarioResponsavel;
        this.status = status;
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

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public LocalDateTime getDataHoraFinalizacao() {
        return dataHoraFinalizacao;
    }

    public void setDataHoraFinalizacao(LocalDateTime dataHoraFinalizacao) {
        this.dataHoraFinalizacao = dataHoraFinalizacao;
    }

    public Usuario getUsuarioResponsavel() {
        return usuarioResponsavel;
    }

    public void setUsuarioResponsavel(Usuario usuarioResponsavel) {
        this.usuarioResponsavel = usuarioResponsavel;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public List<ConferenciaEstoqueItem> getItens() {
        return itens;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ConferenciaEstoque that = (ConferenciaEstoque) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}