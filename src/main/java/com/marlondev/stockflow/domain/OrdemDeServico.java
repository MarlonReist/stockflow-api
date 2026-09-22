package com.marlondev.stockflow.domain;

import com.marlondev.stockflow.domain.enums.StatusEnum;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name = "ordem_de_servico")
@Entity
public class OrdemDeServico implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dataAbertura;
    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "varchar(50)")
    private StatusEnum status;
    private String descricao;
    @ManyToOne
    @JoinColumn(name = "tipo_ordem_servico_id")
    private TipoOrdemServico tipoOrdemServico;
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    @ManyToOne
    @JoinColumn(name = "colaborador_id")
    private Colaborador colaborador;
    private LocalDateTime dataFechamento;

    private LocalDateTime dataAgendada;
    private LocalDateTime inicioAtendimento;
    private LocalDateTime fimAtendimento;

    @Column(length = 1000)
    private String observacaoConclusao;

    @OneToMany(mappedBy = "ordemDeServico")
    private final List<OrdemServicoItem> items = new ArrayList<>();


    public OrdemDeServico() {
    }

    public OrdemDeServico(Long id, LocalDate dataAbertura, StatusEnum status, String descricao,
                          Cliente cliente, Colaborador colaborador, LocalDateTime dataFechamento,
                          LocalDateTime dataAgendada, LocalDateTime inicioAtendimento,
                          LocalDateTime fimAtendimento, String observacaoConclusao) {
        this.id = id;
        this.dataAbertura = dataAbertura;
        this.status = status;
        this.descricao = descricao;
        this.cliente = cliente;
        this.colaborador = colaborador;
        this.dataFechamento = dataFechamento;
        this.dataAgendada = dataAgendada;
        this.inicioAtendimento = inicioAtendimento;
        this.fimAtendimento = fimAtendimento;
        this.observacaoConclusao = observacaoConclusao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDate dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoOrdemServico getTipoOrdemServico() {
        return tipoOrdemServico;
    }

    public void setTipoOrdemServico(TipoOrdemServico tipoOrdemServico) {
        this.tipoOrdemServico = tipoOrdemServico;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Colaborador getColaborador() {
        return colaborador;
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
    }

    public LocalDateTime getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(LocalDateTime dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public LocalDateTime getDataAgendada() {
        return dataAgendada;
    }

    public void setDataAgendada(LocalDateTime dataAgendada) {
        this.dataAgendada = dataAgendada;
    }

    public LocalDateTime getInicioAtendimento() {
        return inicioAtendimento;
    }

    public void setInicioAtendimento(LocalDateTime inicioAtendimento) {
        this.inicioAtendimento = inicioAtendimento;
    }

    public LocalDateTime getFimAtendimento() {
        return fimAtendimento;
    }

    public void setFimAtendimento(LocalDateTime fimAtendimento) {
        this.fimAtendimento = fimAtendimento;
    }

    public String getObservacaoConclusao() {
        return observacaoConclusao;
    }

    public void setObservacaoConclusao(String observacaoConclusao) {
        this.observacaoConclusao = observacaoConclusao;
    }

    public List<OrdemServicoItem> getItems() {
        return items;
    }

    public double getValorTotal() {
        double sum = 0.0;
        for (OrdemServicoItem item : items){
            sum += item.valorTotal();
        }
        return sum;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrdemDeServico that = (OrdemDeServico) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
