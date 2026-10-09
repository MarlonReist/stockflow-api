package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.enums.StatusEnum;

import java.time.Duration;
import java.time.LocalDateTime;

public class RelatorioTecnicoAtendimentoDTO {

    private Long ordemServicoId;
    private String clienteNome;
    private Long tipoOrdemServicoId;
    private String tipoOrdemServicoNome;
    private Long tecnicoResponsavelId;
    private String tecnicoResponsavelNome;
    private Long tecnicoAjudanteId;
    private String tecnicoAjudanteNome;
    private LocalDateTime inicioAtendimento;
    private LocalDateTime fimAtendimento;
    private Long duracaoAtendimentoSegundos;
    private StatusEnum statusAtual;
    private String participacaoDoTecnico;

    public RelatorioTecnicoAtendimentoDTO() {
    }

    public RelatorioTecnicoAtendimentoDTO(Long ordemServicoId,
                                          String clienteNome,
                                          Long tipoOrdemServicoId,
                                          String tipoOrdemServicoNome,
                                          Long tecnicoResponsavelId,
                                          String tecnicoResponsavelNome,
                                          Long tecnicoAjudanteId,
                                          String tecnicoAjudanteNome,
                                          LocalDateTime inicioAtendimento,
                                          LocalDateTime fimAtendimento,
                                          StatusEnum statusAtual) {
        this.ordemServicoId = ordemServicoId;
        this.clienteNome = clienteNome;
        this.tipoOrdemServicoId = tipoOrdemServicoId;
        this.tipoOrdemServicoNome = tipoOrdemServicoNome;
        this.tecnicoResponsavelId = tecnicoResponsavelId;
        this.tecnicoResponsavelNome = tecnicoResponsavelNome;
        this.tecnicoAjudanteId = tecnicoAjudanteId;
        this.tecnicoAjudanteNome = tecnicoAjudanteNome;
        this.inicioAtendimento = inicioAtendimento;
        this.fimAtendimento = fimAtendimento;
        this.statusAtual = statusAtual;
        this.duracaoAtendimentoSegundos = calcularDuracaoAtendimentoSegundos(inicioAtendimento, fimAtendimento);
    }

    private Long calcularDuracaoAtendimentoSegundos(LocalDateTime inicioAtendimento, LocalDateTime fimAtendimento) {
        if (inicioAtendimento == null || fimAtendimento == null || fimAtendimento.isBefore(inicioAtendimento)) {
            return null;
        }
        return Duration.between(inicioAtendimento, fimAtendimento).getSeconds();
    }

    public Long getOrdemServicoId() {
        return ordemServicoId;
    }

    public void setOrdemServicoId(Long ordemServicoId) {
        this.ordemServicoId = ordemServicoId;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
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

    public Long getTecnicoResponsavelId() {
        return tecnicoResponsavelId;
    }

    public void setTecnicoResponsavelId(Long tecnicoResponsavelId) {
        this.tecnicoResponsavelId = tecnicoResponsavelId;
    }

    public String getTecnicoResponsavelNome() {
        return tecnicoResponsavelNome;
    }

    public void setTecnicoResponsavelNome(String tecnicoResponsavelNome) {
        this.tecnicoResponsavelNome = tecnicoResponsavelNome;
    }

    public Long getTecnicoAjudanteId() {
        return tecnicoAjudanteId;
    }

    public void setTecnicoAjudanteId(Long tecnicoAjudanteId) {
        this.tecnicoAjudanteId = tecnicoAjudanteId;
    }

    public String getTecnicoAjudanteNome() {
        return tecnicoAjudanteNome;
    }

    public void setTecnicoAjudanteNome(String tecnicoAjudanteNome) {
        this.tecnicoAjudanteNome = tecnicoAjudanteNome;
    }

    public LocalDateTime getInicioAtendimento() {
        return inicioAtendimento;
    }

    public void setInicioAtendimento(LocalDateTime inicioAtendimento) {
        this.inicioAtendimento = inicioAtendimento;
        this.duracaoAtendimentoSegundos = calcularDuracaoAtendimentoSegundos(inicioAtendimento, fimAtendimento);
    }

    public LocalDateTime getFimAtendimento() {
        return fimAtendimento;
    }

    public void setFimAtendimento(LocalDateTime fimAtendimento) {
        this.fimAtendimento = fimAtendimento;
        this.duracaoAtendimentoSegundos = calcularDuracaoAtendimentoSegundos(inicioAtendimento, fimAtendimento);
    }

    public Long getDuracaoAtendimentoSegundos() {
        return duracaoAtendimentoSegundos;
    }

    public void setDuracaoAtendimentoSegundos(Long duracaoAtendimentoSegundos) {
        this.duracaoAtendimentoSegundos = duracaoAtendimentoSegundos;
    }

    public StatusEnum getStatusAtual() {
        return statusAtual;
    }

    public void setStatusAtual(StatusEnum statusAtual) {
        this.statusAtual = statusAtual;
    }

    public String getParticipacaoDoTecnico() {
        return participacaoDoTecnico;
    }

    public void setParticipacaoDoTecnico(String participacaoDoTecnico) {
        this.participacaoDoTecnico = participacaoDoTecnico;
    }
}
