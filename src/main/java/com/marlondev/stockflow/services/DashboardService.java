package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.MovimentacaoEstoque;
import com.marlondev.stockflow.domain.enums.StatusEnum;
import com.marlondev.stockflow.dto.DashboardMovimentacaoRecenteDTO;
import com.marlondev.stockflow.dto.DashboardOsPorStatusDTO;
import com.marlondev.stockflow.dto.DashboardResumoDTO;
import com.marlondev.stockflow.repositories.AlmoxarifadoRepository;
import com.marlondev.stockflow.repositories.MovimentacaoEstoqueRepository;
import com.marlondev.stockflow.repositories.OrdemDeServicoRepository;
import com.marlondev.stockflow.repositories.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final ProdutoRepository produtoRepository;
    private final AlmoxarifadoRepository almoxarifadoRepository;
    private final OrdemDeServicoRepository ordemDeServicoRepository;
    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    public DashboardService(ProdutoRepository produtoRepository,
                            AlmoxarifadoRepository almoxarifadoRepository,
                            OrdemDeServicoRepository ordemDeServicoRepository,
                            MovimentacaoEstoqueRepository movimentacaoEstoqueRepository) {
        this.produtoRepository = produtoRepository;
        this.almoxarifadoRepository = almoxarifadoRepository;
        this.ordemDeServicoRepository = ordemDeServicoRepository;
        this.movimentacaoEstoqueRepository = movimentacaoEstoqueRepository;
    }

    public DashboardResumoDTO buscarResumo() {
        YearMonth mesAtual = YearMonth.now();
        LocalDate primeiroDiaDoMes = mesAtual.atDay(1);
        LocalDate ultimoDiaDoMes = mesAtual.atEndOfMonth();

        Long totalProdutos = produtoRepository.count();
        Long almoxarifadosAtivos = almoxarifadoRepository.count();
        Long osAbertas = ordemDeServicoRepository.countByStatus(StatusEnum.ABERTA);
        Long movimentacoesNoMes = movimentacaoEstoqueRepository.countByDataMovimentacaoBetween(
                primeiroDiaDoMes,
                ultimoDiaDoMes
        );

        return new DashboardResumoDTO(totalProdutos, almoxarifadosAtivos, osAbertas, movimentacoesNoMes);
    }

    public List<DashboardMovimentacaoRecenteDTO> buscarMovimentacoesRecentes() {
        return movimentacaoEstoqueRepository.findTop5ByOrderByDataMovimentacaoDescIdDesc()
                .stream()
                .map(this::toMovimentacaoRecenteDTO)
                .collect(Collectors.toList());
    }

    public List<DashboardOsPorStatusDTO> buscarOrdensDeServicoPorStatus() {
        return List.of(StatusEnum.values())
                .stream()
                .map(status -> new DashboardOsPorStatusDTO(
                        status,
                        ordemDeServicoRepository.countByStatus(status)
                ))
                .collect(Collectors.toList());
    }

    private DashboardMovimentacaoRecenteDTO toMovimentacaoRecenteDTO(MovimentacaoEstoque movimentacao) {
        return new DashboardMovimentacaoRecenteDTO(
                movimentacao.getId(),
                movimentacao.getDataMovimentacao(),
                movimentacao.getTipo(),
                movimentacao.getProduto().getNome(),
                movimentacao.getAlmoxarifado().getNome(),
                movimentacao.getQuantidade(),
                origem(movimentacao),
                idOrigem(movimentacao)
        );
    }

    private String origem(MovimentacaoEstoque movimentacao) {
        if (movimentacao.getEntradaEstoque() != null) {
            return "Entrada";
        }
        if (movimentacao.getSaidaEstoque() != null) {
            return "Sa\u00edda";
        }
        if (movimentacao.getTransferenciaAlmoxarifado() != null) {
            return "Transfer\u00eancia";
        }
        if (movimentacao.getOrdemDeServico() != null) {
            return "Ordem de Servi\u00e7o";
        }
        return "";
    }

    private Long idOrigem(MovimentacaoEstoque movimentacao) {
        if (movimentacao.getEntradaEstoque() != null) {
            return movimentacao.getEntradaEstoque().getId();
        }
        if (movimentacao.getSaidaEstoque() != null) {
            return movimentacao.getSaidaEstoque().getId();
        }
        if (movimentacao.getTransferenciaAlmoxarifado() != null) {
            return movimentacao.getTransferenciaAlmoxarifado().getId();
        }
        if (movimentacao.getOrdemDeServico() != null) {
            return movimentacao.getOrdemDeServico().getId();
        }
        return null;
    }
}
