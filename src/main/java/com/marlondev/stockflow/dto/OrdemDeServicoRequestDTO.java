package com.marlondev.stockflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OrdemDeServicoRequestDTO {
    @NotBlank(message = "Descrição é obrigatória!")
    private String descricao;
    @NotNull(message = "Cliente é obrigatório!")
    private Long clienteId;
    @NotNull(message = "Colaborador é obrigatório!")
    private Long colaboradorId;
    @NotNull(message = "Tipo de ordem de serviço é obrigatório!")
    private Long tipoOrdemServicoId;

    public OrdemDeServicoRequestDTO() {
    }

    public OrdemDeServicoRequestDTO(String descricao, Long clienteId, Long colaboradorId, Long tipoOrdemServicoId) {
        this.descricao = descricao;
        this.clienteId = clienteId;
        this.colaboradorId = colaboradorId;
        this.tipoOrdemServicoId = tipoOrdemServicoId;
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

    public Long getColaboradorId() {
        return colaboradorId;
    }

    public void setColaboradorId(Long colaboradorId) {
        this.colaboradorId = colaboradorId;
    }

    public Long getTipoOrdemServicoId() {
        return tipoOrdemServicoId;
    }

    public void setTipoOrdemServicoId(Long tipoOrdemServicoId) {
        this.tipoOrdemServicoId = tipoOrdemServicoId;
    }
}