package com.marlondev.stockflow.domain;

import com.marlondev.stockflow.domain.enums.PropriedadeRoteadorEnum;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "roteador_principal_historico")
public class RoteadorPrincipalHistorico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    @Column(length = 150)
    private String modeloAnterior;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PropriedadeRoteadorEnum propriedadeAnterior;
    @Column(nullable = false, length = 150)
    private String modeloNovo;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PropriedadeRoteadorEnum propriedadeNova;
    @Column(nullable = false)
    private LocalDateTime dataAlteracao;
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    @ManyToOne
    @JoinColumn(name = "ordem_de_servico_id")
    private OrdemDeServico ordemDeServico;

    public Long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getModeloAnterior() {
        return modeloAnterior;
    }

    public void setModeloAnterior(String modeloAnterior) {
        this.modeloAnterior = modeloAnterior;
    }

    public PropriedadeRoteadorEnum getPropriedadeAnterior() {
        return propriedadeAnterior;
    }

    public void setPropriedadeAnterior(PropriedadeRoteadorEnum propriedadeAnterior) {
        this.propriedadeAnterior = propriedadeAnterior;
    }

    public String getModeloNovo() {
        return modeloNovo;
    }

    public void setModeloNovo(String modeloNovo) {
        this.modeloNovo = modeloNovo;
    }

    public PropriedadeRoteadorEnum getPropriedadeNova() {
        return propriedadeNova;
    }

    public void setPropriedadeNova(PropriedadeRoteadorEnum propriedadeNova) {
        this.propriedadeNova = propriedadeNova;
    }

    public LocalDateTime getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(LocalDateTime dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public OrdemDeServico getOrdemDeServico() {
        return ordemDeServico;
    }

    public void setOrdemDeServico(OrdemDeServico ordemDeServico) {
        this.ordemDeServico = ordemDeServico;
    }
}
