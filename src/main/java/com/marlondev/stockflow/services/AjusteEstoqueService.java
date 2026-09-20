package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.AjusteEstoque;
import com.marlondev.stockflow.domain.Almoxarifado;
import com.marlondev.stockflow.domain.AlmoxarifadoEstoque;
import com.marlondev.stockflow.domain.ConferenciaEstoque;
import com.marlondev.stockflow.domain.ConferenciaEstoqueItem;
import com.marlondev.stockflow.domain.Produto;
import com.marlondev.stockflow.domain.Usuario;
import com.marlondev.stockflow.domain.enums.StatusEnum;
import com.marlondev.stockflow.domain.enums.TipoAjusteEstoque;
import com.marlondev.stockflow.dto.AjusteConferenciaRequestDTO;
import com.marlondev.stockflow.dto.AjusteEstoqueRequestDTO;
import com.marlondev.stockflow.dto.AjusteEstoqueResponseDTO;
import com.marlondev.stockflow.repositories.*;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AjusteEstoqueService {

    private final AjusteEstoqueRepository ajusteEstoqueRepository;
    private final AlmoxarifadoRepository almoxarifadoRepository;
    private final ProdutoRepository produtoRepository;
    private final AlmoxarifadoEstoqueRepository almoxarifadoEstoqueRepository;
    private final MovimentacaoEstoqueService movimentacaoEstoqueService;
    private final ConferenciaEstoqueItemRepository conferenciaEstoqueItemRepository;

    public AjusteEstoqueService(AjusteEstoqueRepository ajusteEstoqueRepository,
                                AlmoxarifadoRepository almoxarifadoRepository,
                                ProdutoRepository produtoRepository,
                                AlmoxarifadoEstoqueRepository almoxarifadoEstoqueRepository,
                                MovimentacaoEstoqueService movimentacaoEstoqueService, ConferenciaEstoqueItemRepository conferenciaEstoqueItemRepository) {
        this.ajusteEstoqueRepository = ajusteEstoqueRepository;
        this.almoxarifadoRepository = almoxarifadoRepository;
        this.produtoRepository = produtoRepository;
        this.almoxarifadoEstoqueRepository = almoxarifadoEstoqueRepository;
        this.movimentacaoEstoqueService = movimentacaoEstoqueService;
        this.conferenciaEstoqueItemRepository = conferenciaEstoqueItemRepository;
    }

    @Transactional
    public AjusteEstoqueResponseDTO ajustarManual(AjusteEstoqueRequestDTO dto, Usuario usuarioResponsavel) {
        AjusteEstoque ajusteSalvo = aplicarAjuste(dto, usuarioResponsavel, null, null);
        return new AjusteEstoqueResponseDTO(ajusteSalvo);
    }

    private AjusteEstoque aplicarAjuste(AjusteEstoqueRequestDTO dto, Usuario usuarioResponsavel,
                                        ConferenciaEstoque conferenciaEstoque,
                                        ConferenciaEstoqueItem conferenciaEstoqueItem) {
        Almoxarifado almoxarifado = almoxarifadoRepository.findById(dto.getAlmoxarifadoId())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getAlmoxarifadoId()));

        Produto produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getProdutoId()));

        AlmoxarifadoEstoque estoque = almoxarifadoEstoqueRepository
                .findByAlmoxarifadoIdAndProdutoId(almoxarifado.getId(), produto.getId())
                .orElse(null);

        if (dto.getTipo() == TipoAjusteEstoque.AJUSTE_ENTRADA) {
            estoque = aplicarAjusteEntrada(almoxarifado, produto, estoque, dto.getQuantidade());
        } else if (dto.getTipo() == TipoAjusteEstoque.AJUSTE_SAIDA) {
            estoque = aplicarAjusteSaida(estoque, dto.getQuantidade());
        } else {
            throw new DatabaseException("Tipo de ajuste inválido!");
        }

        almoxarifadoEstoqueRepository.save(estoque);

        AjusteEstoque ajuste = new AjusteEstoque();
        ajuste.setDataHora(LocalDateTime.now());
        ajuste.setTipo(dto.getTipo());
        ajuste.setAlmoxarifado(almoxarifado);
        ajuste.setProduto(produto);
        ajuste.setQuantidade(dto.getQuantidade());
        ajuste.setMotivo(dto.getMotivo());
        ajuste.setUsuarioResponsavel(usuarioResponsavel);
        ajuste.setConferenciaEstoque(conferenciaEstoque);
        ajuste.setConferenciaEstoqueItem(conferenciaEstoqueItem);

        AjusteEstoque ajusteSalvo = ajusteEstoqueRepository.save(ajuste);
        movimentacaoEstoqueService.registrarAjuste(ajusteSalvo);

        return ajusteSalvo;
    }

    private AlmoxarifadoEstoque aplicarAjusteEntrada(Almoxarifado almoxarifado, Produto produto,
                                                     AlmoxarifadoEstoque estoque, Integer quantidade) {
        if (estoque == null) {
            estoque = new AlmoxarifadoEstoque();
            estoque.setAlmoxarifado(almoxarifado);
            estoque.setProduto(produto);
            estoque.setQuantidade(0);
        }

        estoque.setQuantidade(estoque.getQuantidade() + quantidade);
        return estoque;
    }

    private AlmoxarifadoEstoque aplicarAjusteSaida(AlmoxarifadoEstoque estoque, Integer quantidade) {
        if (estoque == null) {
            throw new DatabaseException("Não existe estoque desse produto nesse almoxarifado!");
        }

        if (quantidade > estoque.getQuantidade()) {
            throw new DatabaseException("Quantidade insuficiente em estoque!");
        }

        estoque.setQuantidade(estoque.getQuantidade() - quantidade);
        return estoque;
    }

    @Transactional
    public AjusteEstoqueResponseDTO ajustarPorConferencia(Long itemId, AjusteConferenciaRequestDTO dto,
                                                          Usuario usuarioResponsavel) {
        ConferenciaEstoqueItem item = conferenciaEstoqueItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException(itemId));

        if (item.getConferencia().getStatus() != StatusEnum.FINALIZADA) {
            throw new DatabaseException("Conferência precisa estar finalizada para gerar ajuste!");
        }

        Integer divergencia = item.divergencia();

        if (divergencia == null || divergencia == 0) {
            throw new DatabaseException("Item não possui divergência para ajuste!");
        }

        if (ajusteEstoqueRepository.existsByConferenciaEstoqueItemId(item.getId())) {
            throw new DatabaseException("Já existe ajuste gerado para este item da conferência!");
        }

        TipoAjusteEstoque tipo = divergencia > 0
                ? TipoAjusteEstoque.AJUSTE_ENTRADA
                : TipoAjusteEstoque.AJUSTE_SAIDA;

        AjusteEstoqueRequestDTO ajusteDto = new AjusteEstoqueRequestDTO(
                tipo,
                item.getConferencia().getAlmoxarifado().getId(),
                item.getProduto().getId(),
                Math.abs(divergencia),
                dto.getMotivo()
        );

        AjusteEstoque ajusteSalvo = aplicarAjuste(
                ajusteDto,
                usuarioResponsavel,
                item.getConferencia(),
                item
        );

        return new AjusteEstoqueResponseDTO(ajusteSalvo);
    }

    public AjusteEstoqueResponseDTO buscarPorId(Long id) {
        AjusteEstoque ajuste = ajusteEstoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        return new AjusteEstoqueResponseDTO(ajuste);
    }

    public List<AjusteEstoqueResponseDTO> listarTodos() {
        List<AjusteEstoque> ajustes = ajusteEstoqueRepository.findAll();

        return ajustes.stream()
                .map(AjusteEstoqueResponseDTO::new)
                .collect(Collectors.toList());
    }
}
