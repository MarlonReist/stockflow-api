package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.dto.DashboardResumoDTO;
import com.marlondev.stockflow.dto.DashboardMovimentacaoRecenteDTO;
import com.marlondev.stockflow.dto.DashboardOsPorStatusDTO;
import com.marlondev.stockflow.services.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping(value = "/resumo")
    public ResponseEntity<DashboardResumoDTO> buscarResumo() {
        DashboardResumoDTO resumo = dashboardService.buscarResumo();
        return ResponseEntity.ok().body(resumo);
    }

    @GetMapping(value = "/movimentacoes-recentes")
    public ResponseEntity<List<DashboardMovimentacaoRecenteDTO>> buscarMovimentacoesRecentes() {
        List<DashboardMovimentacaoRecenteDTO> movimentacoes = dashboardService.buscarMovimentacoesRecentes();
        return ResponseEntity.ok().body(movimentacoes);
    }

    @GetMapping(value = "/os-por-status")
    public ResponseEntity<List<DashboardOsPorStatusDTO>> buscarOrdensDeServicoPorStatus() {
        List<DashboardOsPorStatusDTO> ordensPorStatus = dashboardService.buscarOrdensDeServicoPorStatus();
        return ResponseEntity.ok().body(ordensPorStatus);
    }
}
