package com.marlondev.stockflow.dto;

public class RelatorioTecnicoQuantidadePorTecnicoDTO {

    private Long tecnicoId;
    private String tecnicoNome;
    private Long quantidade;

    public RelatorioTecnicoQuantidadePorTecnicoDTO() {
    }

    public RelatorioTecnicoQuantidadePorTecnicoDTO(Long tecnicoId, String tecnicoNome, Long quantidade) {
        this.tecnicoId = tecnicoId;
        this.tecnicoNome = tecnicoNome;
        this.quantidade = quantidade;
    }

    public Long getTecnicoId() {
        return tecnicoId;
    }

    public void setTecnicoId(Long tecnicoId) {
        this.tecnicoId = tecnicoId;
    }

    public String getTecnicoNome() {
        return tecnicoNome;
    }

    public void setTecnicoNome(String tecnicoNome) {
        this.tecnicoNome = tecnicoNome;
    }

    public Long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }
}
