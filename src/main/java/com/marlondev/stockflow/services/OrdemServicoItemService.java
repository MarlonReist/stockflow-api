package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.*;
import com.marlondev.stockflow.domain.enums.StatusEnum;
import com.marlondev.stockflow.dto.OrdemServicoItemRequestDTO;
import com.marlondev.stockflow.dto.OrdemServicoItemResponseDTO;
import com.marlondev.stockflow.repositories.*;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrdemServicoItemService {

    private final OrdemServicoItemRepository ordemItemRepository;
    private final OrdemDeServicoRepository ordemDeServicoRepository;
    private final ProdutoRepository produtoRepository;
    private final AlmoxarifadoEstoqueRepository estoqueRepository;
    private final AlmoxarifadoRepository almoxarifadoRepository;

    public OrdemServicoItemService(OrdemServicoItemRepository ordemItemRepository, OrdemDeServicoRepository ordemDeServicoRepository, ProdutoRepository produtoRepository, AlmoxarifadoEstoqueRepository estoqueRepository, AlmoxarifadoRepository almoxarifadoRepository) {
        this.ordemItemRepository = ordemItemRepository;
        this.ordemDeServicoRepository = ordemDeServicoRepository;
        this.produtoRepository = produtoRepository;
        this.estoqueRepository = estoqueRepository;
        this.almoxarifadoRepository = almoxarifadoRepository;
    }

    @Transactional
    public OrdemServicoItemResponseDTO salvar(OrdemServicoItemRequestDTO dto) {
        OrdemDeServico osExistente = ordemDeServicoRepository.findById(dto.getOsId())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getOsId()));

        Almoxarifado almoxarifadoEncontrado = almoxarifadoRepository.findById(dto.getAlmoxarifadoId())
                .orElseThrow(() -> new ResourceNotFoundException("Esse Almoxarifado não existe!"));

        Produto produtoExistente = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getProdutoId()));

        AlmoxarifadoEstoque estoque = estoqueRepository.findByAlmoxarifadoIdAndProdutoId(almoxarifadoEncontrado.getId(),produtoExistente.getId())
                .orElseThrow(() -> new DatabaseException("Não existe estoque desse produto nesse almoxarifado"));


        if (osExistente.getStatus() != StatusEnum.ABERTA) {
            throw new DatabaseException("Ordem de serviço não está aberta!");
        }

        if (dto.getQuantidade() > estoque.getQuantidade()) {
            throw new DatabaseException("Quantidade insuficiente em estoque!");
        }
            OrdemServicoItem ordemItem = new OrdemServicoItem();
            ordemItem.setQuantidade(dto.getQuantidade());
            ordemItem.setValorUnitario(produtoExistente.getPreco());
            ordemItem.setAlmoxarifado(almoxarifadoEncontrado);
            ordemItem.setOrdemDeServico(osExistente);
            ordemItem.setProduto(produtoExistente);
            estoque.setQuantidade(estoque.getQuantidade() - dto.getQuantidade());
            estoqueRepository.save(estoque);
            OrdemServicoItem ordemItemSalvo = ordemItemRepository.save(ordemItem);
            return new OrdemServicoItemResponseDTO(ordemItemSalvo);
    }

    public OrdemServicoItemResponseDTO buscarPorId(Long id) {
        OrdemServicoItem ordemItem = ordemItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        return new OrdemServicoItemResponseDTO(ordemItem);
    }

    @Transactional
    public void deletarOrdemServicoItemPorId(Long id) {
        OrdemServicoItem osItem = ordemItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        if (osItem.getOrdemDeServico().getStatus() != StatusEnum.ABERTA) {
            throw new DatabaseException("Ordem de Serviço não está aberta!");
        }

        Integer devolucao = osItem.getQuantidade();
        AlmoxarifadoEstoque estoqueAntigo = estoqueRepository.findByAlmoxarifadoIdAndProdutoId(osItem.getAlmoxarifado().getId(),osItem.getProduto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Não existe estoque desse produto nesse almoxarifado"));
        estoqueAntigo.setQuantidade(estoqueAntigo.getQuantidade() + devolucao);

        estoqueRepository.save(estoqueAntigo);
        ordemItemRepository.delete(osItem);
    }

    public List<OrdemServicoItemResponseDTO> listarTodos() {
        List<OrdemServicoItem> list = ordemItemRepository.findAll();
        List<OrdemServicoItemResponseDTO> listDto = list.stream().map(OrdemServicoItemResponseDTO::new).collect(Collectors.toList());
        return listDto;
    }

    @Transactional
    public OrdemServicoItemResponseDTO atualizarOrdemItem(Long id, OrdemServicoItemRequestDTO dto) {
        OrdemServicoItem itemAntigo = ordemItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        if (itemAntigo.getOrdemDeServico().getStatus() != StatusEnum.ABERTA) {
            throw new DatabaseException("Ordem de serviço não está aberta!");
        }

        Integer devolucao = itemAntigo.getQuantidade();
        AlmoxarifadoEstoque estoqueAntigo = estoqueRepository.findByAlmoxarifadoIdAndProdutoId(itemAntigo.getAlmoxarifado().getId(),itemAntigo.getProduto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Não existe estoque desse produto nesse almoxarifado"));
        estoqueAntigo.setQuantidade(estoqueAntigo.getQuantidade() + devolucao);

        if(!Objects.equals(dto.getOsId(), itemAntigo.getOrdemDeServico().getId())) {
            throw new DatabaseException("Não é possível trocar a Ordem de serviço");
        }
        Produto produtoNovo = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getProdutoId()));

        Almoxarifado almoxarifadoNovo = almoxarifadoRepository.findById(dto.getAlmoxarifadoId())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getAlmoxarifadoId()));

        AlmoxarifadoEstoque estoqueNovo = estoqueRepository.findByAlmoxarifadoIdAndProdutoId(almoxarifadoNovo.getId(),produtoNovo.getId())
                .orElseThrow(() -> new DatabaseException("Não existe estoque desse produto nesse almoxarifado"));

        if (estoqueNovo.getQuantidade() < dto.getQuantidade()) {
            throw new DatabaseException("Quantidade insuficiente em estoque!");
        }

        itemAntigo.setQuantidade(dto.getQuantidade());
        itemAntigo.setValorUnitario(produtoNovo.getPreco());
        itemAntigo.setAlmoxarifado(almoxarifadoNovo);
        itemAntigo.setProduto(produtoNovo);
        estoqueNovo.setQuantidade(estoqueNovo.getQuantidade() - dto.getQuantidade());
        estoqueRepository.save(estoqueAntigo);
        estoqueRepository.save(estoqueNovo);
        OrdemServicoItem itemAtualizado = ordemItemRepository.save(itemAntigo);
        return new OrdemServicoItemResponseDTO(itemAtualizado);
    }
}