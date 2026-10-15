package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.RoteadorPrincipalHistorico;
import com.marlondev.stockflow.domain.enums.PropriedadeRoteadorEnum;

import java.time.LocalDateTime;

public class RoteadorPrincipalHistoricoResponseDTO {
    private final Long id;
    private final String modeloAnterior;
    private final PropriedadeRoteadorEnum propriedadeAnterior;
    private final String modeloNovo;
    private final PropriedadeRoteadorEnum propriedadeNova;
    private final LocalDateTime dataAlteracao;
    private final Long usuarioId;
    private final String usuarioNome;
    private final Long ordemServicoId;

    public RoteadorPrincipalHistoricoResponseDTO(RoteadorPrincipalHistorico historico) {
        id = historico.getId();
        modeloAnterior = historico.getModeloAnterior();
        propriedadeAnterior = historico.getPropriedadeAnterior();
        modeloNovo = historico.getModeloNovo();
        propriedadeNova = historico.getPropriedadeNova();
        dataAlteracao = historico.getDataAlteracao();
        usuarioId = historico.getUsuario() == null ? null : historico.getUsuario().getId();
        usuarioNome = historico.getUsuario() == null ? null : historico.getUsuario().getNome();
        ordemServicoId = historico.getOrdemDeServico() == null ? null : historico.getOrdemDeServico().getId();
    }

    public Long getId() { return id; }
    public String getModeloAnterior() { return modeloAnterior; }
    public PropriedadeRoteadorEnum getPropriedadeAnterior() { return propriedadeAnterior; }
    public String getModeloNovo() { return modeloNovo; }
    public PropriedadeRoteadorEnum getPropriedadeNova() { return propriedadeNova; }
    public LocalDateTime getDataAlteracao() { return dataAlteracao; }
    public Long getUsuarioId() { return usuarioId; }
    public String getUsuarioNome() { return usuarioNome; }
    public Long getOrdemServicoId() { return ordemServicoId; }
}
