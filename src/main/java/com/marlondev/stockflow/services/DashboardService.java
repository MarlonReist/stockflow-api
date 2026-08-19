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
import com.marlondev.stockflow.services.exceptions.DatabaseException;
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

    private record PeriodoDashboard(LocalDate inicio, LocalDate fim) {
    }

    private PeriodoDashboard resolverPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null && dataFim == null) {
            YearMonth mesAtual = YearMonth.now();
            return new PeriodoDashboard(
                    mesAtual.atDay(1),
                    mesAtual.atEndOfMonth()
            );
        }

        if (dataInicio == null || dataFim == null) {
            throw new DatabaseException("Data inicial e data final devem ser informadas juntas!");
        }

        if (dataInicio.isAfter(dataFim)) {
            throw new DatabaseException("Data inicial não pode ser maior que a data final!");
        }

        return new PeriodoDashboard(dataInicio, dataFim);
    }

    public DashboardResumoDTO buscarResumo(LocalDate dataInicio, LocalDate dataFim) {
        PeriodoDashboard periodo = resolverPeriodo(dataInicio, dataFim);

        Long totalProdutos = produtoRepository.count();
        Long almoxarifadosAtivos = almoxarifadoRepository.count();
        Long osAbertas = ordemDeServicoRepository.countByStatusAndDataAberturaBetween(
                StatusEnum.ABERTA,
                periodo.inicio(),
                periodo.fim()
        );
        Long movimentacoesNoMes = movimentacaoEstoqueRepository.countByDataMovimentacaoBetween(
                periodo.inicio(),
                periodo.fim()
        );

        return new DashboardResumoDTO(totalProdutos, almoxarifadosAtivos, osAbertas, movimentacoesNoMes);
    }

    public List<DashboardMovimentacaoRecenteDTO> buscarMovimentacoesRecentes(
            LocalDate dataInicio,
            LocalDate dataFim
    ) {
        PeriodoDashboard periodo = resolverPeriodo(dataInicio, dataFim);

        return movimentacaoEstoqueRepository
                .findTop5ByDataMovimentacaoBetweenOrderByDataMovimentacaoDescIdDesc(
                        periodo.inicio(),
                        periodo.fim()
                )
                .stream()
                .map(this::toMovimentacaoRecenteDTO)
                .collect(Collectors.toList());
    }

    public List<DashboardOsPorStatusDTO> buscarOrdensDeServicoPorStatus(
            LocalDate dataInicio,
            LocalDate dataFim
    ) {
        PeriodoDashboard periodo = resolverPeriodo(dataInicio, dataFim);

        return List.of(StatusEnum.values())
                .stream()
                .map(status -> new DashboardOsPorStatusDTO(
                        status,
                        ordemDeServicoRepository.countByStatusAndDataAberturaBetween(
                                status,
                                periodo.inicio(),
                                periodo.fim()
                        )
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
