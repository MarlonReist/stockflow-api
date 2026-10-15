package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.enums.PropriedadeRoteadorEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RoteadorPrincipalRequestDTO {
    @NotBlank(message = "Modelo do roteador é obrigatório!")
    @Size(max = 150, message = "Modelo do roteador deve ter no máximo 150 caracteres!")
    private String modelo;
    @NotNull(message = "Propriedade do roteador é obrigatória!")
    private PropriedadeRoteadorEnum propriedade;

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public PropriedadeRoteadorEnum getPropriedade() { return propriedade; }
    public void setPropriedade(PropriedadeRoteadorEnum propriedade) { this.propriedade = propriedade; }
}
