package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.ConferenciaEstoque;
import com.marlondev.stockflow.domain.ConferenciaEstoqueItem;
import com.marlondev.stockflow.domain.Produto;
import com.marlondev.stockflow.domain.enums.StatusEnum;
import com.marlondev.stockflow.dto.ConferenciaEstoqueItemRequestDTO;
import com.marlondev.stockflow.repositories.AlmoxarifadoEstoqueRepository;
import com.marlondev.stockflow.repositories.AlmoxarifadoRepository;
import com.marlondev.stockflow.repositories.ConferenciaEstoqueItemRepository;
import com.marlondev.stockflow.repositories.ConferenciaEstoqueRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConferenciaEstoqueServiceTest {

    @Mock
    private ConferenciaEstoqueRepository conferenciaRepository;

    @Mock
    private ConferenciaEstoqueItemRepository conferenciaItemRepository;

    @Mock
    private AlmoxarifadoRepository almoxarifadoRepository;

    @Mock
    private AlmoxarifadoEstoqueRepository almoxarifadoEstoqueRepository;

    private ConferenciaEstoqueService service;

    @BeforeEach
    void setUp() {
        service = new ConferenciaEstoqueService(
                conferenciaRepository,
                conferenciaItemRepository,
                almoxarifadoRepository,
                almoxarifadoEstoqueRepository
        );
    }

    @Test
    void conferenciaAbertaPermiteInformarContagem() {
        ConferenciaEstoqueItem item = criarItem(StatusEnum.ABERTA, null);
        ConferenciaEstoqueItemRequestDTO dto = new ConferenciaEstoqueItemRequestDTO(8);

        when(conferenciaItemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(conferenciaItemRepository.save(any(ConferenciaEstoqueItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.informarQuantidadeContada(1L, dto);

        assertEquals(8, item.getQuantidadeContada());
        verify(conferenciaItemRepository).save(item);
    }

    @Test
    void conferenciaFinalizadaNaoPermiteAlterarContagem() {
        ConferenciaEstoqueItem item = criarItem(StatusEnum.FINALIZADA, 5);
        ConferenciaEstoqueItemRequestDTO dto = new ConferenciaEstoqueItemRequestDTO(8);

        when(conferenciaItemRepository.findById(1L)).thenReturn(Optional.of(item));

        DatabaseException exception = assertThrows(DatabaseException.class,
                () -> service.informarQuantidadeContada(1L, dto));

        assertEquals("A conferência está finalizada e não pode mais ser alterada.", exception.getMessage());
        assertEquals(5, item.getQuantidadeContada());
        verify(conferenciaItemRepository, never()).save(any());
    }

    @Test
    void conferenciaFinalizadaNaoPodeSerFinalizadaNovamente() {
        ConferenciaEstoque conferencia = criarConferencia(StatusEnum.FINALIZADA);

        when(conferenciaRepository.findById(1L)).thenReturn(Optional.of(conferencia));

        DatabaseException exception = assertThrows(DatabaseException.class, () -> service.finalizar(1L));

        assertEquals("A conferência está finalizada e não pode mais ser alterada.", exception.getMessage());
        verify(conferenciaRepository, never()).save(any());
    }

    private ConferenciaEstoqueItem criarItem(StatusEnum status, Integer quantidadeContada) {
        ConferenciaEstoqueItem item = new ConferenciaEstoqueItem();
        item.setId(1L);
        item.setConferencia(criarConferencia(status));
        item.setProduto(criarProduto());
        item.setQuantidadeEsperada(10);
        item.setQuantidadeContada(quantidadeContada);
        return item;
    }

    private Produto criarProduto() {
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("ONU");
        return produto;
    }

    private ConferenciaEstoque criarConferencia(StatusEnum status) {
        ConferenciaEstoque conferencia = new ConferenciaEstoque();
        conferencia.setId(1L);
        conferencia.setStatus(status);
        return conferencia;
    }
}
