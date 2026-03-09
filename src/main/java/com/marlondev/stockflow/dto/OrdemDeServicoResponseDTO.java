package com.marlondev.stockflow.dto;
import com.marlondev.stockflow.domain.OrdemDeServico;
import com.marlondev.stockflow.domain.enums.StatusEnum;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
public class OrdemDeServicoResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private LocalDate dataAbertura;
    private StatusEnum status;
    private String descricao;
    private Long clienteId;
    private String clienteNome;
    private Long colaboradorId;
    private String colaboradorNome;
    private LocalDate dataFechamento;
    private double valorTotal;
    
    public OrdemDeServicoResponseDTO(){
    }
    
    public OrdemDeServicoResponseDTO(OrdemDeServico os){
        id = os.getId();
        dataAbertura = os.getDataAbertura();
        status = os.getStatus();
        descricao = os.getDescricao();
        clienteId = os.getCliente().getId();
        clienteNome = os.getCliente().getNome();
        colaboradorId = os.getColaborador().getId();
        colaboradorNome = os.getColaborador().getNome();
        dataFechamento = os.getDataFechamento();
        valorTotal = os.getValorTotal();
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

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    public Long getColaboradorId() {
        return colaboradorId;
    }

    public void setColaboradorId(Long colaboradorId) {
        this.colaboradorId = colaboradorId;
    }

    public String getColaboradorNome() {
        return colaboradorNome;
    }

    public void setColaboradorNome(String colaboradorNome) {
        this.colaboradorNome = colaboradorNome;
    }

    public LocalDate getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(LocalDate dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }
}