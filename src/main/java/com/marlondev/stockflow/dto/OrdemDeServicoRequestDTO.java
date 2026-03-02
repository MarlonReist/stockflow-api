package com.marlondev.stockflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OrdemDeServicoRequestDTO {

    @NotBlank (message = "Descrição é obrigatória!")
    private String descricao;

    private Long clienteId;

    private Long colaboradorId;

    public OrdemDeServicoRequestDTO(){
    }

    public OrdemDeServicoRequestDTO(String descricao, Long clienteId, Long colaboradorId) {
        this.descricao = descricao;
        this.clienteId = clienteId;
        this.colaboradorId = colaboradorId;
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
}
