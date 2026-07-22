package com.marlondev.stockflow.dto;

public class DashboardResumoDTO {

    private Long totalProdutos;
    private Long almoxarifadosAtivos;
    private Long osAbertas;
    private Long movimentacoesNoMes;

    public DashboardResumoDTO() {
    }

    public DashboardResumoDTO(Long totalProdutos, Long almoxarifadosAtivos, Long osAbertas, Long movimentacoesNoMes) {
        this.totalProdutos = totalProdutos;
        this.almoxarifadosAtivos = almoxarifadosAtivos;
        this.osAbertas = osAbertas;
        this.movimentacoesNoMes = movimentacoesNoMes;
    }

    public Long getTotalProdutos() {
        return totalProdutos;
    }

    public void setTotalProdutos(Long totalProdutos) {
        this.totalProdutos = totalProdutos;
    }

    public Long getAlmoxarifadosAtivos() {
        return almoxarifadosAtivos;
    }

    public void setAlmoxarifadosAtivos(Long almoxarifadosAtivos) {
        this.almoxarifadosAtivos = almoxarifadosAtivos;
    }

    public Long getOsAbertas() {
        return osAbertas;
    }

    public void setOsAbertas(Long osAbertas) {
        this.osAbertas = osAbertas;
    }

    public Long getMovimentacoesNoMes() {
        return movimentacoesNoMes;
    }

    public void setMovimentacoesNoMes(Long movimentacoesNoMes) {
        this.movimentacoesNoMes = movimentacoesNoMes;
    }
}
