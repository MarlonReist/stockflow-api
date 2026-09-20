package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.Almoxarifado;
import com.marlondev.stockflow.domain.AlmoxarifadoEstoque;
import com.marlondev.stockflow.domain.ConferenciaEstoque;
import com.marlondev.stockflow.domain.ConferenciaEstoqueItem;
import com.marlondev.stockflow.domain.Usuario;
import com.marlondev.stockflow.domain.enums.StatusEnum;
import com.marlondev.stockflow.dto.ConferenciaEstoqueItemRequestDTO;
import com.marlondev.stockflow.dto.ConferenciaEstoqueItemResponseDTO;
import com.marlondev.stockflow.dto.ConferenciaEstoqueRequestDTO;
import com.marlondev.stockflow.dto.ConferenciaEstoqueResponseDTO;
import com.marlondev.stockflow.repositories.AlmoxarifadoEstoqueRepository;
import com.marlondev.stockflow.repositories.AlmoxarifadoRepository;
import com.marlondev.stockflow.repositories.ConferenciaEstoqueItemRepository;
import com.marlondev.stockflow.repositories.ConferenciaEstoqueRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConferenciaEstoqueService {

    private final ConferenciaEstoqueRepository conferenciaRepository;
    private final ConferenciaEstoqueItemRepository conferenciaItemRepository;
    private final AlmoxarifadoRepository almoxarifadoRepository;
    private final AlmoxarifadoEstoqueRepository almoxarifadoEstoqueRepository;

    public ConferenciaEstoqueService(ConferenciaEstoqueRepository conferenciaRepository,
                                     ConferenciaEstoqueItemRepository conferenciaItemRepository,
                                     AlmoxarifadoRepository almoxarifadoRepository,
                                     AlmoxarifadoEstoqueRepository almoxarifadoEstoqueRepository) {
        this.conferenciaRepository = conferenciaRepository;
        this.conferenciaItemRepository = conferenciaItemRepository;
        this.almoxarifadoRepository = almoxarifadoRepository;
        this.almoxarifadoEstoqueRepository = almoxarifadoEstoqueRepository;
    }

    @Transactional
    public ConferenciaEstoqueResponseDTO iniciar(ConferenciaEstoqueRequestDTO dto, Usuario usuarioResponsavel) {
        Almoxarifado almoxarifado = almoxarifadoRepository.findById(dto.getAlmoxarifadoId())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getAlmoxarifadoId()));

        ConferenciaEstoque conferencia = new ConferenciaEstoque();
        conferencia.setAlmoxarifado(almoxarifado);
        conferencia.setUsuarioResponsavel(usuarioResponsavel);
        conferencia.setDataHoraInicio(LocalDateTime.now());
        conferencia.setStatus(StatusEnum.ABERTA);

        ConferenciaEstoque conferenciaSalva = conferenciaRepository.save(conferencia);

        List<AlmoxarifadoEstoque> saldos = almoxarifadoEstoqueRepository
                .findByAlmoxarifadoIdOrderByProdutoNomeAsc(almoxarifado.getId());

        for (AlmoxarifadoEstoque saldo : saldos) {
            ConferenciaEstoqueItem item = new ConferenciaEstoqueItem();
            item.setConferencia(conferenciaSalva);
            item.setProduto(saldo.getProduto());
            item.setQuantidadeEsperada(saldo.getQuantidade());
            item.setQuantidadeContada(null);
            conferenciaItemRepository.save(item);
            conferenciaSalva.getItens().add(item);
        }

        return new ConferenciaEstoqueResponseDTO(conferenciaSalva);
    }

    public ConferenciaEstoqueResponseDTO buscarPorId(Long id) {
        ConferenciaEstoque conferencia = conferenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        return new ConferenciaEstoqueResponseDTO(conferencia);
    }

    @Transactional
    public ConferenciaEstoqueItemResponseDTO informarQuantidadeContada(Long itemId, ConferenciaEstoqueItemRequestDTO dto) {
        ConferenciaEstoqueItem item = conferenciaItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException(itemId));

        if (item.getConferencia().getStatus() != StatusEnum.ABERTA) {
            throw new DatabaseException("Conferência não está aberta!");
        }

        item.setQuantidadeContada(dto.getQuantidadeContada());
        ConferenciaEstoqueItem itemSalvo = conferenciaItemRepository.save(item);

        return new ConferenciaEstoqueItemResponseDTO(itemSalvo);
    }

    @Transactional
    public ConferenciaEstoqueResponseDTO finalizar(Long id) {
        ConferenciaEstoque conferencia = conferenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        if (conferencia.getStatus() != StatusEnum.ABERTA) {
            throw new DatabaseException("Conferência não está aberta!");
        }

        boolean existeItemSemContagem = conferencia.getItens()
                .stream()
                .anyMatch(item -> item.getQuantidadeContada() == null);

        if (existeItemSemContagem) {
            throw new DatabaseException("Não é possível finalizar conferência com itens sem contagem!");
        }

        conferencia.setDataHoraFinalizacao(LocalDateTime.now());
        conferencia.setStatus(StatusEnum.FINALIZADA);

        ConferenciaEstoque conferenciaSalva = conferenciaRepository.save(conferencia);
        return new ConferenciaEstoqueResponseDTO(conferenciaSalva);
    }

    public List<ConferenciaEstoqueResponseDTO> listarTodos() {
        List<ConferenciaEstoque> conferencias = conferenciaRepository.findAll();

        return conferencias.stream()
                .map(ConferenciaEstoqueResponseDTO::new)
                .collect(Collectors.toList());
    }
}