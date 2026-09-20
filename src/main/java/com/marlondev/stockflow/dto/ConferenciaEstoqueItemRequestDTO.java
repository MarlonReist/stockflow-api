package com.marlondev.stockflow.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class ConferenciaEstoqueItemRequestDTO {

    @NotNull(message = "Quantidade contada é obrigatória!")
    @PositiveOrZero(message = "Quantidade contada não pode ser negativa!")
    private Integer quantidadeContada;

    public ConferenciaEstoqueItemRequestDTO() {
    }

    public ConferenciaEstoqueItemRequestDTO(Integer quantidadeContada) {
        this.quantidadeContada = quantidadeContada;
    }

    public Integer getQuantidadeContada() {
        return quantidadeContada;
    }

    public void setQuantidadeContada(Integer quantidadeContada) {
        this.quantidadeContada = quantidadeContada;
    }
}