package com.marlondev.stockflow.domain;

import com.marlondev.stockflow.domain.enums.StatusEnum;
import jakarta.persistence.*;
import org.springframework.context.annotation.Lazy;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
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
    private StatusEnum status;
    private String descricao;
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    @ManyToOne
    @JoinColumn(name = "colaborador_id", nullable = false)
    private Colaborador colaborador;
    private LocalDate dataFechamento;
    @OneToMany(mappedBy = "ordemDeServico")
    private final List<OrdemServicoItem> items = new ArrayList<>();


    public OrdemDeServico() {
    }

    public OrdemDeServico(Long id, LocalDate dataAbertura, StatusEnum status, String descricao, Cliente cliente, Colaborador colaborador, LocalDate dataFechamento) {
        this.id = id;
        this.dataAbertura = dataAbertura;
        this.status = status;
        this.descricao = descricao;
        this.cliente = cliente;
        this.colaborador = colaborador;
        this.dataFechamento = dataFechamento;
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

    public LocalDate getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(LocalDate dataFechamento) {
        this.dataFechamento = dataFechamento;
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
