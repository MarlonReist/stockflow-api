package com.marlondev.stockflow.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class OrdemDeServicoAgendamentoRequestDTO {

    @NotNull(message = "Colaborador é obrigatório!")
    private Long colaboradorId;

    @NotNull(message = "Data agendada é obrigatória!")
    private LocalDateTime dataAgendada;

    public OrdemDeServicoAgendamentoRequestDTO() {
    }

    public Long getColaboradorId() {
        return colaboradorId;
    }

    public void setColaboradorId(Long colaboradorId) {
        this.colaboradorId = colaboradorId;
    }

    public LocalDateTime getDataAgendada() {
        return dataAgendada;
    }

    public void setDataAgendada(LocalDateTime dataAgendada) {
        this.dataAgendada = dataAgendada;
    }
}