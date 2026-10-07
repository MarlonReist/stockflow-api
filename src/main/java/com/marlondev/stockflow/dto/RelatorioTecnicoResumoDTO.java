package com.marlondev.stockflow.dto;

public class RelatorioTecnicoResumoDTO {

    private Long totalOsRealizadas;
    private Long totalOsFinalizadas;
    private Long totalOsCanceladas;
    private Long totalAguardandoConferencia;
    private Long tempoMedioAtendimentoSegundos;
    private Long quantidadeTecnicosComAtendimento;

    public RelatorioTecnicoResumoDTO() {
    }

    public RelatorioTecnicoResumoDTO(Long totalOsRealizadas,
                                     Long totalOsFinalizadas,
                                     Long totalOsCanceladas,
                                     Long totalAguardandoConferencia,
                                     Long tempoMedioAtendimentoSegundos,
                                     Long quantidadeTecnicosComAtendimento) {
        this.totalOsRealizadas = totalOsRealizadas;
        this.totalOsFinalizadas = totalOsFinalizadas;
        this.totalOsCanceladas = totalOsCanceladas;
        this.totalAguardandoConferencia = totalAguardandoConferencia;
        this.tempoMedioAtendimentoSegundos = tempoMedioAtendimentoSegundos;
        this.quantidadeTecnicosComAtendimento = quantidadeTecnicosComAtendimento;
    }

    public Long getTotalOsRealizadas() {
        return totalOsRealizadas;
    }

    public void setTotalOsRealizadas(Long totalOsRealizadas) {
        this.totalOsRealizadas = totalOsRealizadas;
    }

    public Long getTotalOsFinalizadas() {
        return totalOsFinalizadas;
    }

    public void setTotalOsFinalizadas(Long totalOsFinalizadas) {
        this.totalOsFinalizadas = totalOsFinalizadas;
    }

    public Long getTotalOsCanceladas() {
        return totalOsCanceladas;
    }

    public void setTotalOsCanceladas(Long totalOsCanceladas) {
        this.totalOsCanceladas = totalOsCanceladas;
    }

    public Long getTotalAguardandoConferencia() {
        return totalAguardandoConferencia;
    }

    public void setTotalAguardandoConferencia(Long totalAguardandoConferencia) {
        this.totalAguardandoConferencia = totalAguardandoConferencia;
    }

    public Long getTempoMedioAtendimentoSegundos() {
        return tempoMedioAtendimentoSegundos;
    }

    public void setTempoMedioAtendimentoSegundos(Long tempoMedioAtendimentoSegundos) {
        this.tempoMedioAtendimentoSegundos = tempoMedioAtendimentoSegundos;
    }

    public Long getQuantidadeTecnicosComAtendimento() {
        return quantidadeTecnicosComAtendimento;
    }

    public void setQuantidadeTecnicosComAtendimento(Long quantidadeTecnicosComAtendimento) {
        this.quantidadeTecnicosComAtendimento = quantidadeTecnicosComAtendimento;
    }
}
