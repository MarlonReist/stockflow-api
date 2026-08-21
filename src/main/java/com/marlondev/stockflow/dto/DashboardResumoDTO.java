package com.marlondev.stockflow.dto;

public class DashboardResumoDTO {

    private Long totalProdutos;
    private Long almoxarifadosAtivos;
    private Long osAbertas;
    private Long movimentacoesNoPeriodo;
    private Double valorTotalEntradasPeriodo;
    private Double valorTotalSaidasPeriodo;
    private Double custoTotalOrdensServicoPeriodo;

    public DashboardResumoDTO() {
    }

    public DashboardResumoDTO(Long totalProdutos, Long almoxarifadosAtivos, Long osAbertas, Long movimentacoesNoPeriodo, Double valorTotalEntradasPeriodo, Double valorTotalSaidasPeriodo, Double custoTotalOrdensServicoPeriodo) {
        this.totalProdutos = totalProdutos;
        this.almoxarifadosAtivos = almoxarifadosAtivos;
        this.osAbertas = osAbertas;
        this.movimentacoesNoPeriodo = movimentacoesNoPeriodo;
        this.valorTotalEntradasPeriodo = valorTotalEntradasPeriodo;
        this.valorTotalSaidasPeriodo = valorTotalSaidasPeriodo;
        this.custoTotalOrdensServicoPeriodo = custoTotalOrdensServicoPeriodo;
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

    public Long getMovimentacoesNoPeriodo() {
        return movimentacoesNoPeriodo;
    }

    public void setMovimentacoesNoPeriodo(Long movimentacoesNoPeriodo) {
        this.movimentacoesNoPeriodo = movimentacoesNoPeriodo;
    }

    public Double getValorTotalEntradasPeriodo() {
        return valorTotalEntradasPeriodo;
    }

    public void setValorTotalEntradasPeriodo(Double valorTotalEntradasPeriodo) {
        this.valorTotalEntradasPeriodo = valorTotalEntradasPeriodo;
    }

    public Double getValorTotalSaidasPeriodo() {
        return valorTotalSaidasPeriodo;
    }

    public void setValorTotalSaidasPeriodo(Double valorTotalSaidasPeriodo) {
        this.valorTotalSaidasPeriodo = valorTotalSaidasPeriodo;
    }

    public Double getCustoTotalOrdensServicoPeriodo() {
        return custoTotalOrdensServicoPeriodo;
    }

    public void setCustoTotalOrdensServicoPeriodo(Double custoTotalOrdensServicoPeriodo) {
        this.custoTotalOrdensServicoPeriodo = custoTotalOrdensServicoPeriodo;
    }
}
