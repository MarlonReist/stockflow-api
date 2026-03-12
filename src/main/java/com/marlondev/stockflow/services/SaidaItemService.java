package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.*;
import com.marlondev.stockflow.domain.enums.StatusEnum;
import com.marlondev.stockflow.dto.SaidaItemRequestDTO;
import com.marlondev.stockflow.dto.SaidaItemResponseDTO;
import com.marlondev.stockflow.repositories.AlmoxarifadoEstoqueRepository;
import com.marlondev.stockflow.repositories.ProdutoRepository;
import com.marlondev.stockflow.repositories.SaidaEstoqueRepository;
import com.marlondev.stockflow.repositories.SaidaItemRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SaidaItemService {

    private final SaidaItemRepository saidaItemRepository;
    private final SaidaEstoqueRepository saidaEstoqueRepository;
    private final ProdutoRepository produtoRepository;
    private final AlmoxarifadoEstoqueRepository almoxarifadoEstoqueRepository;

    public SaidaItemService(SaidaItemRepository saidaItemRepository, SaidaEstoqueRepository saidaEstoqueRepository, ProdutoRepository produtoRepository, AlmoxarifadoEstoqueRepository almoxarifadoEstoqueRepository) {
        this.saidaItemRepository = saidaItemRepository;
        this.saidaEstoqueRepository = saidaEstoqueRepository;
        this.produtoRepository = produtoRepository;
        this.almoxarifadoEstoqueRepository = almoxarifadoEstoqueRepository;
    }

    @Transactional
    public SaidaItemResponseDTO salvar(SaidaItemRequestDTO dto){
        SaidaEstoque saidaExistente = saidaEstoqueRepository.findById(dto.getSaidaEstoqueId())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getSaidaEstoqueId()));

        if (saidaExistente.getStatus() != StatusEnum.ABERTA){
            throw new DatabaseException("Saída não está aberta!");
        }

        Produto produtoExistente = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getProdutoId()));

        SaidaItem saidaItem = new SaidaItem();
        saidaItem.setSaidaEstoque(saidaExistente);
        saidaItem.setProduto(produtoExistente);
        saidaItem.setQuantidade(dto.getQuantidade());
        saidaItem.setValorUnitario(produtoExistente.getPreco());

        Optional<AlmoxarifadoEstoque> estoqueAlmoxarifadoSaida = almoxarifadoEstoqueRepository.findByAlmoxarifadoIdAndProdutoId(saidaExistente.getAlmoxarifado().getId(), produtoExistente.getId());

        if(estoqueAlmoxarifadoSaida.isPresent()){
        AlmoxarifadoEstoque estoqueExistente = estoqueAlmoxarifadoSaida.get();

            if (dto.getQuantidade() > estoqueExistente.getQuantidade()) {
                throw new DatabaseException("Quantidade insuficiente em estoque!");
            }

        int novaQuantidade = estoqueExistente.getQuantidade() - dto.getQuantidade();
        estoqueExistente.setQuantidade(novaQuantidade);
        almoxarifadoEstoqueRepository.save(estoqueExistente);
    } else {
            throw new DatabaseException("Não existe estoque desse produto nesse almoxarifado!");
    }
        SaidaItem saidaItemSalva = saidaItemRepository.save(saidaItem);
        return new SaidaItemResponseDTO(saidaItemSalva);
    }

    public SaidaItemResponseDTO buscarPorId(Long id){
        SaidaItem saidaExistente = saidaItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return new SaidaItemResponseDTO(saidaExistente);
    }

    public List<SaidaItemResponseDTO> listarTodos(){
        List<SaidaItem> list = saidaItemRepository.findAll();
        return list.stream().map(SaidaItemResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public void deletarPorId(Long id){
        SaidaItem saidaExistente = saidaItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        if (saidaExistente.getSaidaEstoque().getStatus() != StatusEnum.ABERTA){
            throw new DatabaseException("Saída não está aberta!");
        }
        int devolucao = saidaExistente.getQuantidade();
        AlmoxarifadoEstoque estoqueAntigo = almoxarifadoEstoqueRepository.findByAlmoxarifadoIdAndProdutoId(saidaExistente.getSaidaEstoque().getAlmoxarifado().getId(),saidaExistente.getProduto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Não existe estoque desse produto nesse almoxarifado"));
        int saldoFinal = estoqueAntigo.getQuantidade() + devolucao;

        estoqueAntigo.setQuantidade(saldoFinal);
        almoxarifadoEstoqueRepository.save(estoqueAntigo);
        saidaItemRepository.deleteById(saidaExistente.getId());
    }

    @Transactional
    public SaidaItemResponseDTO atualizarSaidaItem(Long id, SaidaItemRequestDTO dto){
        SaidaItem saidaExistente = saidaItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        if (saidaExistente.getSaidaEstoque().getStatus() != StatusEnum.ABERTA) {
            throw new DatabaseException("Saída não está aberta!");
        }

        int devolucao = saidaExistente.getQuantidade();
        AlmoxarifadoEstoque estoqueAntigo = almoxarifadoEstoqueRepository.findByAlmoxarifadoIdAndProdutoId(saidaExistente.getSaidaEstoque().getAlmoxarifado().getId(),saidaExistente.getProduto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Não existe estoque desse produto nesse almoxarifado"));
        int saldoFinal = estoqueAntigo.getQuantidade() + devolucao;
        estoqueAntigo.setQuantidade(saldoFinal);
        almoxarifadoEstoqueRepository.save(estoqueAntigo);

        if(!Objects.equals(dto.getSaidaEstoqueId(), saidaExistente.getSaidaEstoque().getId())) {
            throw new DatabaseException("Não é possível trocar a saída!");
        }

        Produto produtoExistente = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getProdutoId()));

        saidaExistente.setProduto(produtoExistente);
        saidaExistente.setQuantidade(dto.getQuantidade());
        saidaExistente.setValorUnitario(produtoExistente.getPreco());

        Optional<AlmoxarifadoEstoque> estoqueAlmoxarifadoSaida = almoxarifadoEstoqueRepository.findByAlmoxarifadoIdAndProdutoId(saidaExistente.getSaidaEstoque().getAlmoxarifado().getId(), produtoExistente.getId());

        if(estoqueAlmoxarifadoSaida.isPresent()){
            AlmoxarifadoEstoque estoqueExistente = estoqueAlmoxarifadoSaida.get();

            if (dto.getQuantidade() > estoqueExistente.getQuantidade()) {
                throw new DatabaseException("Quantidade insuficiente em estoque!");
            }

            int novaQuantidade = estoqueExistente.getQuantidade() - dto.getQuantidade();
            estoqueExistente.setQuantidade(novaQuantidade);
            almoxarifadoEstoqueRepository.save(estoqueExistente);
        } else {
            throw new DatabaseException("Não existe estoque desse produto nesse almoxarifado!");
        }
        SaidaItem saidaItemSalva = saidaItemRepository.save(saidaExistente);
        return new SaidaItemResponseDTO(saidaItemSalva);
    }
}
