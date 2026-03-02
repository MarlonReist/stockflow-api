/*package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.*;
import com.marlondev.stockflow.domain.enums.StatusEnum;
import com.marlondev.stockflow.dto.OrdemServicoItemRequestDTO;
import com.marlondev.stockflow.repositories.OrdemDeServicoRepository;
import com.marlondev.stockflow.repositories.OrdemServicoItemRepository;
import com.marlondev.stockflow.repositories.ProdutoRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class OrdemServicoItemService {

    private final OrdemServicoItemRepository ordemItemRepository;
    private final OrdemDeServicoRepository ordemDeServicoRepository;
    private final ProdutoRepository produtoRepository;

    public OrdemServicoItemService(OrdemServicoItemRepository ordemItemRepository, OrdemDeServicoRepository ordemDeServicoRepository, ProdutoRepository produtoRepository) {
        this.ordemItemRepository = ordemItemRepository;
        this.ordemDeServicoRepository = ordemDeServicoRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public OrdemServicoItem salvar(OrdemServicoItemRequestDTO dto) {
        OrdemServicoItem ordemItem = new OrdemServicoItem();

        OrdemDeServico osExistente = ordemDeServicoRepository.findById(dto.getOsId())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getOsId()));

        Produto produtoExistente = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getOsId()));

        if (osExistente.getStatus() != StatusEnum.ABERTA) {
            throw new DatabaseException("Ordem de serviço não está aberta!");
        }

        if (dto.getQuantidade() > produtoExistente.getQuantidade()) {
            throw new DatabaseException("Quantidade insuficiente em estoque!");
        }
            ordemItem.setQuantidade(dto.getQuantidade());
            ordemItem.setValorUnitario(produtoExistente.getPreco());
            ordemItem.setOrdemDeServico(osExistente);
            ordemItem.setProduto(produtoExistente);
            return ordemItemRepository.save(ordemItem);
    }

    public OrdemServicoItem buscarPorId(Long id) {
        return ordemItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public void deletarOrdemServicoItemPorId(Long id) {
        OrdemServicoItem os = ordemItemRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(id));

        if (os.getOrdemDeServico().getStatus() != StatusEnum.ABERTA) {
            throw new DatabaseException("Ordem de Serviço não está aberta!");
        }
            ordemItemRepository.delete(os);
    }

    public List<OrdemServicoItem> listarTodos() {
        return ordemItemRepository.findAll();
    }

     public void atualizarOrdemItem(OrdemServicoItemRequestDTO dto, Long id) {
        OrdemServicoItem itemExistente = buscarPorId(id);

        if (itemExistente.getOrdemDeServico().getStatus() != StatusEnum.ABERTA) {
            throw new DatabaseException("Ordem de Serviço não está aberta!");
        }

        if(!Objects.equals(dto.getOsId(), itemExistente.getOrdemDeServico().getId())) {
            throw new DatabaseException("Não é possível trocar a Ordem de serviço");
        }

        if(!Objects.equals(dto.getProdutoId(), itemExistente.getProduto().getId())) {
            throw new DatabaseException("Não é possível trocar o Produto!");
        }

         if (dto.getQuantidade() > itemExistente.getProduto().getQuantidade()) {
             throw new DatabaseException("Quantidade insuficiente em estoque!");
         }

         itemExistente.setQuantidade(dto.getQuantidade());
        ordemItemRepository.save(itemExistente);
    }
}
*/