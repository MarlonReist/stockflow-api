package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.*;
import com.marlondev.stockflow.domain.enums.TipoMovimentacao;
import com.marlondev.stockflow.dto.MovimentacaoEstoqueResponseDTO;
import com.marlondev.stockflow.repositories.MovimentacaoEstoqueRepository;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovimentacaoEstoqueService {

    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    public MovimentacaoEstoqueService(MovimentacaoEstoqueRepository movimentacaoEstoqueRepository) {
        this.movimentacaoEstoqueRepository = movimentacaoEstoqueRepository;
    }

    public MovimentacaoEstoqueResponseDTO buscarPorId(Long id){
        MovimentacaoEstoque movimentacaoExiste = movimentacaoEstoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        return new MovimentacaoEstoqueResponseDTO(movimentacaoExiste);
    }

    public List<MovimentacaoEstoqueResponseDTO> listarTodos(){
        List<MovimentacaoEstoque> list = movimentacaoEstoqueRepository.findAll();
        return list.stream().map(MovimentacaoEstoqueResponseDTO::new).collect(Collectors.toList());
    }

    public void registrarEntrada(EntradaItem entradaItem) {
        MovimentacaoEstoque mov = new MovimentacaoEstoque();
        mov.setAlmoxarifado(entradaItem.getEntradaEstoque().getAlmoxarifado());
        mov.setProduto(entradaItem.getProduto());
        mov.setQuantidade(entradaItem.getQuantidade());
        mov.setDataMovimentacao(LocalDate.now());
        mov.setTipo(TipoMovimentacao.ENTRADA);
        mov.setEntradaEstoque(entradaItem.getEntradaEstoque());
        movimentacaoEstoqueRepository.save(mov);

    }

    public void registrarSaida(SaidaItem saidaItem){
        MovimentacaoEstoque mov = new MovimentacaoEstoque();
        mov.setAlmoxarifado(saidaItem.getSaidaEstoque().getAlmoxarifado());
        mov.setProduto(saidaItem.getProduto());
        mov.setQuantidade(saidaItem.getQuantidade());
        mov.setDataMovimentacao(LocalDate.now());
        mov.setTipo(TipoMovimentacao.SAIDA);
        mov.setSaidaEstoque(saidaItem.getSaidaEstoque());
        movimentacaoEstoqueRepository.save(mov);
    }

    public void registrarSaidaPorOrdemDeServico(OrdemServicoItem osItem){
        MovimentacaoEstoque mov = new MovimentacaoEstoque();
        mov.setAlmoxarifado(osItem.getAlmoxarifado());
        mov.setProduto(osItem.getProduto());
        mov.setQuantidade(osItem.getQuantidade());
        mov.setDataMovimentacao(LocalDate.now());
        mov.setTipo(TipoMovimentacao.SAIDA);
        mov.setOrdemDeServico(osItem.getOrdemDeServico());
        movimentacaoEstoqueRepository.save(mov);
    }

    public void registrarEntradaTransferencia(TransferenciaItem transferenciaItem) {
        MovimentacaoEstoque mov = new MovimentacaoEstoque();
        mov.setAlmoxarifado(transferenciaItem.getTransferencia().getAlmoxarifadoDestino());
        mov.setProduto(transferenciaItem.getProduto());
        mov.setQuantidade(transferenciaItem.getQuantidade());
        mov.setDataMovimentacao(LocalDate.now());
        mov.setTipo(TipoMovimentacao.ENTRADA);
        mov.setTransferenciaAlmoxarifado(transferenciaItem.getTransferencia());
        movimentacaoEstoqueRepository.save(mov);
    }

    public void registrarSaidaTransferencia(TransferenciaItem transferenciaItem) {
        MovimentacaoEstoque mov = new MovimentacaoEstoque();
        mov.setAlmoxarifado(transferenciaItem.getTransferencia().getAlmoxarifadoOrigem());
        mov.setProduto(transferenciaItem.getProduto());
        mov.setQuantidade(transferenciaItem.getQuantidade());
        mov.setDataMovimentacao(LocalDate.now());
        mov.setTipo(TipoMovimentacao.SAIDA);
        mov.setTransferenciaAlmoxarifado(transferenciaItem.getTransferencia());
        movimentacaoEstoqueRepository.save(mov);
    }
}