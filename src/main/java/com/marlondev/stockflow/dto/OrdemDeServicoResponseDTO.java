package com.marlondev.stockflow.dto;
import com.marlondev.stockflow.domain.OrdemDeServico;
import com.marlondev.stockflow.domain.enums.StatusEnum;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private Long ajudanteId;
    private String ajudanteNome;
    private LocalDateTime dataFechamento;
    private double valorTotal;
    private Long tipoOrdemServicoId;
    private String tipoOrdemServicoNome;
    private LocalDateTime dataAgendada;
    private LocalDateTime inicioAtendimento;
    private LocalDateTime fimAtendimento;
    private String observacaoConclusao;
    
    public OrdemDeServicoResponseDTO(){
    }
    
    public OrdemDeServicoResponseDTO(OrdemDeServico os){
        id = os.getId();
        dataAbertura = os.getDataAbertura();
        status = os.getStatus();
        descricao = os.getDescricao();
        clienteId = os.getCliente().getId();
        clienteNome = os.getCliente().getNome();
        if (os.getColaborador() != null) {
            colaboradorId = os.getColaborador().getId();
            colaboradorNome = os.getColaborador().getNome();
        }
        if (os.getAjudante() != null) {
            ajudanteId = os.getAjudante().getId();
            ajudanteNome = os.getAjudante().getNome();
        }
        dataFechamento = os.getDataFechamento();
        valorTotal = os.getValorTotal();
        if (os.getTipoOrdemServico() != null) {
            tipoOrdemServicoId = os.getTipoOrdemServico().getId();
            tipoOrdemServicoNome = os.getTipoOrdemServico().getNome();
        }
        dataAgendada = os.getDataAgendada();
        inicioAtendimento = os.getInicioAtendimento();
        fimAtendimento = os.getFimAtendimento();
        observacaoConclusao = os.getObservacaoConclusao();
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

    public Long getAjudanteId() {
        return ajudanteId;
    }

    public void setAjudanteId(Long ajudanteId) {
        this.ajudanteId = ajudanteId;
    }

    public String getAjudanteNome() {
        return ajudanteNome;
    }

    public void setAjudanteNome(String ajudanteNome) {
        this.ajudanteNome = ajudanteNome;
    }

    public LocalDateTime getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(LocalDateTime dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Long getTipoOrdemServicoId() {
        return tipoOrdemServicoId;
    }

    public void setTipoOrdemServicoId(Long tipoOrdemServicoId) {
        this.tipoOrdemServicoId = tipoOrdemServicoId;
    }

    public String getTipoOrdemServicoNome() {
        return tipoOrdemServicoNome;
    }

    public void setTipoOrdemServicoNome(String tipoOrdemServicoNome) {
        this.tipoOrdemServicoNome = tipoOrdemServicoNome;
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
}
