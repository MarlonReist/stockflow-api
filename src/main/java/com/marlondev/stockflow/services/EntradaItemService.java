package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.*;
import com.marlondev.stockflow.domain.enums.StatusEnum;
import com.marlondev.stockflow.dto.EntradaItemRequestDTO;
import com.marlondev.stockflow.dto.EntradaItemResponseDTO;
import com.marlondev.stockflow.repositories.*;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EntradaItemService {

    private final EntradaItemRepository entradaItemRepository;
    private final EntradaEstoqueRepository entradaEstoqueRepository;
    private final ProdutoRepository produtoRepository;
    private final AlmoxarifadoEstoqueRepository almoxarifadoEstoqueRepository;

    public EntradaItemService(EntradaItemRepository entradaItemRepository, EntradaEstoqueRepository entradaEstoqueRepository, ProdutoRepository produtoRepository, AlmoxarifadoEstoqueRepository almoxarifadoEstoqueRepository) {
        this.entradaItemRepository = entradaItemRepository;
        this.entradaEstoqueRepository = entradaEstoqueRepository;
        this.produtoRepository = produtoRepository;
        this.almoxarifadoEstoqueRepository = almoxarifadoEstoqueRepository;
    }

    @Transactional
    public EntradaItemResponseDTO salvar(EntradaItemRequestDTO dto) {
        EntradaEstoque entradaExistente = entradaEstoqueRepository.findById(dto.getEntradaEstoqueId())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getEntradaEstoqueId()));

        if (entradaExistente.getStatus() != StatusEnum.ABERTA) {
            throw new DatabaseException("Entrada não está aberta!");
        }

        Produto produtoExistente = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getProdutoId()));

        EntradaItem entradaItem = new EntradaItem();
        if (dto.getValorUnitario() == null) {
            entradaItem.setValorUnitario(produtoExistente.getPreco());
        } else {
            entradaItem.setValorUnitario(dto.getValorUnitario());
        }
        entradaItem.setEntradaEstoque(entradaExistente);
        entradaItem.setProduto(produtoExistente);
        entradaItem.setQuantidade(dto.getQuantidade());
        Optional<AlmoxarifadoEstoque> estoqueAlmoxarifadoEntrada = almoxarifadoEstoqueRepository.findByAlmoxarifadoIdAndProdutoId(entradaExistente.getAlmoxarifado().getId(), produtoExistente.getId());
        if(estoqueAlmoxarifadoEntrada.isPresent()){
            AlmoxarifadoEstoque estoqueExistente = estoqueAlmoxarifadoEntrada.get();
            int novaQuantidade = estoqueExistente.getQuantidade() + dto.getQuantidade();
            estoqueExistente.setQuantidade(novaQuantidade);
            almoxarifadoEstoqueRepository.save(estoqueExistente);
        } else {
            AlmoxarifadoEstoque novoEstoque = new AlmoxarifadoEstoque();
            novoEstoque.setAlmoxarifado(entradaExistente.getAlmoxarifado());
            novoEstoque.setProduto(produtoExistente);
            novoEstoque.setQuantidade(dto.getQuantidade());
            almoxarifadoEstoqueRepository.save(novoEstoque);
        }
        EntradaItem entradaItemSalva = entradaItemRepository.save(entradaItem);
        return new EntradaItemResponseDTO(entradaItemSalva);
    }

    public EntradaItemResponseDTO buscarPorId(Long id){
        EntradaItem entradaExistente = entradaItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return new EntradaItemResponseDTO(entradaExistente);
    }

    public List<EntradaItemResponseDTO> listarTodos(){
        List<EntradaItem> list = entradaItemRepository.findAll();
        return list.stream().map(EntradaItemResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public void deletarPorId(Long id){
        EntradaItem entradaExistente = entradaItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        if (entradaExistente.getEntradaEstoque().getStatus() != StatusEnum.ABERTA) {
            throw new DatabaseException("Entrada não está aberta!");
        }

        int devolucao = entradaExistente.getQuantidade();
        AlmoxarifadoEstoque estoqueAntigo = almoxarifadoEstoqueRepository.findByAlmoxarifadoIdAndProdutoId(entradaExistente.getEntradaEstoque().getAlmoxarifado().getId(),entradaExistente.getProduto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Não existe estoque desse produto nesse almoxarifado"));
        int saldoFinal = estoqueAntigo.getQuantidade() - devolucao;
        if (saldoFinal < 0){
            throw new DatabaseException("Quantidade do produto não pode ser negativa!");
        }
            estoqueAntigo.setQuantidade(saldoFinal);
            almoxarifadoEstoqueRepository.save(estoqueAntigo);
            entradaItemRepository.deleteById(entradaExistente.getId());
    }

    @Transactional
    public EntradaItemResponseDTO atualizarEntrada(Long id, EntradaItemRequestDTO dto){
        EntradaItem entradaExistente = entradaItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        if (entradaExistente.getEntradaEstoque().getStatus() != StatusEnum.ABERTA) {
            throw new DatabaseException("Entrada não está aberta!");
        }

        int devolucao = entradaExistente.getQuantidade();
        AlmoxarifadoEstoque estoqueAntigo = almoxarifadoEstoqueRepository.findByAlmoxarifadoIdAndProdutoId(entradaExistente.getEntradaEstoque().getAlmoxarifado().getId(),entradaExistente.getProduto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Não existe estoque desse produto nesse almoxarifado"));
        int saldoFinal = estoqueAntigo.getQuantidade() - devolucao;
        if (saldoFinal < 0){
            throw new DatabaseException("Quantidade do produto não pode ser negativa!");
        }
        estoqueAntigo.setQuantidade(saldoFinal);
        almoxarifadoEstoqueRepository.save(estoqueAntigo);

        if(!Objects.equals(dto.getEntradaEstoqueId(), entradaExistente.getEntradaEstoque().getId())) {
            throw new DatabaseException("Não é possível trocar a entrada!");
        }

        Produto produtoExistente = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getProdutoId()));

        if (dto.getValorUnitario() == null) {
            entradaExistente.setValorUnitario(produtoExistente.getPreco());
        } else {
            entradaExistente.setValorUnitario(dto.getValorUnitario());
        }
        entradaExistente.setQuantidade(dto.getQuantidade());
        entradaExistente.setProduto(produtoExistente);

        Optional<AlmoxarifadoEstoque> estoqueAlmoxarifadoEntrada = almoxarifadoEstoqueRepository.findByAlmoxarifadoIdAndProdutoId(entradaExistente.getEntradaEstoque().getAlmoxarifado().getId(), produtoExistente.getId());
        if(estoqueAlmoxarifadoEntrada.isPresent()){
            AlmoxarifadoEstoque estoqueExistente = estoqueAlmoxarifadoEntrada.get();
            int novaQuantidade = estoqueExistente.getQuantidade() + dto.getQuantidade();
            estoqueExistente.setQuantidade(novaQuantidade);
            almoxarifadoEstoqueRepository.save(estoqueExistente);
        } else {
            AlmoxarifadoEstoque novoEstoque = new AlmoxarifadoEstoque();
            novoEstoque.setAlmoxarifado(entradaExistente.getEntradaEstoque().getAlmoxarifado());
            novoEstoque.setProduto(produtoExistente);
            novoEstoque.setQuantidade(dto.getQuantidade());
            almoxarifadoEstoqueRepository.save(novoEstoque);
        }
        EntradaItem entradaItemSalva = entradaItemRepository.save(entradaExistente);
        return new EntradaItemResponseDTO(entradaItemSalva);
    }
}
