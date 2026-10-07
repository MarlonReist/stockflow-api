package com.marlondev.stockflow.dto;

public class RelatorioTecnicoQuantidadePorTipoDTO {

    private Long tipoOrdemServicoId;
    private String tipoOrdemServicoNome;
    private Long quantidade;

    public RelatorioTecnicoQuantidadePorTipoDTO() {
    }

    public RelatorioTecnicoQuantidadePorTipoDTO(Long tipoOrdemServicoId, String tipoOrdemServicoNome, Long quantidade) {
        this.tipoOrdemServicoId = tipoOrdemServicoId;
        this.tipoOrdemServicoNome = tipoOrdemServicoNome;
        this.quantidade = quantidade;
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

    public Long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }
}