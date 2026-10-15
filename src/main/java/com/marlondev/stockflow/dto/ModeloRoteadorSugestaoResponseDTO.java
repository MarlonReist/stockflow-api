package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.ModeloRoteadorSugestao;

public class ModeloRoteadorSugestaoResponseDTO {
    private final String modelo;
    private final long frequencia;

    public ModeloRoteadorSugestaoResponseDTO(ModeloRoteadorSugestao sugestao) {
        modelo = sugestao.getModelo();
        frequencia = sugestao.getFrequencia();
    }

    public String getModelo() { return modelo; }
    public long getFrequencia() { return frequencia; }
}
