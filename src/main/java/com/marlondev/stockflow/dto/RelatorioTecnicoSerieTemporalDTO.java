package com.marlondev.stockflow.dto;

public class RelatorioTecnicoSerieTemporalDTO {

    private String periodo;
    private Long quantidade;

    public RelatorioTecnicoSerieTemporalDTO(){
    }

    public RelatorioTecnicoSerieTemporalDTO(String periodo, Long quantidade) {
        this.periodo = periodo;
        this.quantidade = quantidade;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public Long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }
}
