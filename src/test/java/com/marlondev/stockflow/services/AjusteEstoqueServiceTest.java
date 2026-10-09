package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.AjusteEstoque;
import com.marlondev.stockflow.domain.Almoxarifado;
import com.marlondev.stockflow.domain.AlmoxarifadoEstoque;
import com.marlondev.stockflow.domain.ConferenciaEstoque;
import com.marlondev.stockflow.domain.ConferenciaEstoqueItem;
import com.marlondev.stockflow.domain.Produto;
import com.marlondev.stockflow.domain.Usuario;
import com.marlondev.stockflow.domain.enums.StatusEnum;
import com.marlondev.stockflow.dto.AjusteConferenciaRequestDTO;
import com.marlondev.stockflow.repositories.AjusteEstoqueRepository;
import com.marlondev.stockflow.repositories.AlmoxarifadoEstoqueRepository;
import com.marlondev.stockflow.repositories.AlmoxarifadoRepository;
import com.marlondev.stockflow.repositories.ConferenciaEstoqueItemRepository;
import com.marlondev.stockflow.repositories.ProdutoRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.lang.reflect.Method;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AjusteEstoqueServiceTest {

    @Mock
    private AjusteEstoqueRepository ajusteEstoqueRepository;

    @Mock
    private AlmoxarifadoRepository almoxarifadoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private AlmoxarifadoEstoqueRepository almoxarifadoEstoqueRepository;

    @Mock
    private MovimentacaoEstoqueService movimentacaoEstoqueService;

    @Mock
    private ConferenciaEstoqueItemRepository conferenciaEstoqueItemRepository;

    private AjusteEstoqueService service;

    @BeforeEach
    void setUp() {
        service = new AjusteEstoqueService(
                ajusteEstoqueRepository,
                almoxarifadoRepository,
                produtoRepository,
                almoxarifadoEstoqueRepository,
                movimentacaoEstoqueService,
                conferenciaEstoqueItemRepository
        );
    }

    @Test
    void conferenciaAbertaNaoPermiteGerarAjuste() {
        ConferenciaEstoqueItem item = criarItem(StatusEnum.ABERTA, 10, 8);

        when(conferenciaEstoqueItemRepository.findWithLockById(1L)).thenReturn(Optional.of(item));

        assertThrows(DatabaseException.class,
                () -> service.ajustarPorConferencia(1L, new AjusteConferenciaRequestDTO("Divergência"), new Usuario()));

        verify(ajusteEstoqueRepository, never()).saveAndFlush(any());
    }

    @Test
    void itemSemDivergenciaNaoPermiteGerarAjuste() {
        ConferenciaEstoqueItem item = criarItem(StatusEnum.FINALIZADA, 10, 10);

        when(conferenciaEstoqueItemRepository.findWithLockById(1L)).thenReturn(Optional.of(item));

        assertThrows(DatabaseException.class,
                () -> service.ajustarPorConferencia(1L, new AjusteConferenciaRequestDTO("Sem divergência"), new Usuario()));

        verify(ajusteEstoqueRepository, never()).saveAndFlush(any());
    }

    @Test
    void itemDivergenteDeConferenciaFinalizadaPermiteGerarUmAjuste() {
        ConferenciaEstoqueItem item = criarItem(StatusEnum.FINALIZADA, 10, 13);
        AlmoxarifadoEstoque estoque = criarEstoque(item, 10);

        configurarFluxoAjuste(item, estoque, false, false);
        when(ajusteEstoqueRepository.saveAndFlush(any(AjusteEstoque.class))).thenAnswer(invocation -> {
            AjusteEstoque ajuste = invocation.getArgument(0);
            ajuste.setId(100L);
            return ajuste;
        });

        service.ajustarPorConferencia(1L, new AjusteConferenciaRequestDTO("Sobra física"), new Usuario());

        assertEquals(13, estoque.getQuantidade());
        verify(almoxarifadoEstoqueRepository).save(estoque);
        verify(ajusteEstoqueRepository).saveAndFlush(any(AjusteEstoque.class));
        verify(movimentacaoEstoqueService).registrarAjuste(any(AjusteEstoque.class));
    }

    @Test
    void mesmoItemNaoPermiteGerarDoisAjustes() {
        ConferenciaEstoqueItem item = criarItem(StatusEnum.FINALIZADA, 10, 13);

        when(conferenciaEstoqueItemRepository.findWithLockById(1L)).thenReturn(Optional.of(item));
        when(ajusteEstoqueRepository.existsByConferenciaEstoqueItemId(1L)).thenReturn(true);

        DatabaseException exception = assertThrows(DatabaseException.class,
                () -> service.ajustarPorConferencia(1L, new AjusteConferenciaRequestDTO("Duplicado"), new Usuario()));

        assertEquals("Já existe ajuste gerado para este item da conferência.", exception.getMessage());
        verify(almoxarifadoEstoqueRepository, never()).save(any());
        verify(ajusteEstoqueRepository, never()).saveAndFlush(any());
    }

    @Test
    void violacaoDaConstraintUnicaRetornaMensagemDeNegocio() {
        ConferenciaEstoqueItem item = criarItem(StatusEnum.FINALIZADA, 10, 13);
        AlmoxarifadoEstoque estoque = criarEstoque(item, 10);

        configurarFluxoAjuste(item, estoque, false, false);
        when(ajusteEstoqueRepository.saveAndFlush(any(AjusteEstoque.class)))
                .thenThrow(new DataIntegrityViolationException("uk_ajuste_estoque_conferencia_item"));

        DatabaseException exception = assertThrows(DatabaseException.class,
                () -> service.ajustarPorConferencia(1L, new AjusteConferenciaRequestDTO("Corrida"), new Usuario()));

        assertEquals("Já existe ajuste gerado para este item da conferência.", exception.getMessage());
        verify(movimentacaoEstoqueService, never()).registrarAjuste(any());
    }

    @Test
    void saldoAlteradoDepoisDaAberturaBloqueiaAjuste() {
        ConferenciaEstoqueItem item = criarItem(StatusEnum.FINALIZADA, 10, 13);
        AlmoxarifadoEstoque estoque = criarEstoque(item, 11);

        configurarFluxoAjuste(item, estoque, false, true);

        DatabaseException exception = assertThrows(DatabaseException.class,
                () -> service.ajustarPorConferencia(1L, new AjusteConferenciaRequestDTO("Saldo mudou"), new Usuario()));

        assertTrue(exception.getMessage().contains("O saldo deste produto mudou após o início da conferência."));
        assertEquals(11, estoque.getQuantidade());
        verify(almoxarifadoEstoqueRepository, never()).save(any());
        verify(ajusteEstoqueRepository, never()).saveAndFlush(any());
    }

    @Test
    void falhaDuranteGeracaoPropagaExcecaoParaRollbackTransacional() throws NoSuchMethodException {
        ConferenciaEstoqueItem item = criarItem(StatusEnum.FINALIZADA, 10, 13);
        AlmoxarifadoEstoque estoque = criarEstoque(item, 10);
        RuntimeException falha = new RuntimeException("Falha ao registrar movimentação");

        configurarFluxoAjuste(item, estoque, false, false);
        when(ajusteEstoqueRepository.saveAndFlush(any(AjusteEstoque.class))).thenAnswer(invocation -> invocation.getArgument(0));
        org.mockito.Mockito.doThrow(falha).when(movimentacaoEstoqueService).registrarAjuste(any(AjusteEstoque.class));

        Method method = AjusteEstoqueService.class.getMethod(
                "ajustarPorConferencia",
                Long.class,
                AjusteConferenciaRequestDTO.class,
                Usuario.class
        );

        assertNotNull(method.getAnnotation(Transactional.class));
        assertThrows(RuntimeException.class,
                () -> service.ajustarPorConferencia(1L, new AjusteConferenciaRequestDTO("Falha"), new Usuario()));
    }

    private void configurarFluxoAjuste(ConferenciaEstoqueItem item, AlmoxarifadoEstoque estoque,
                                       boolean ajusteExistente, boolean bloquearAntesDeAplicarAjuste) {
        when(conferenciaEstoqueItemRepository.findWithLockById(1L)).thenReturn(Optional.of(item));
        when(ajusteEstoqueRepository.existsByConferenciaEstoqueItemId(1L)).thenReturn(ajusteExistente);
        when(almoxarifadoEstoqueRepository.findByAlmoxarifadoIdAndProdutoId(1L, 1L)).thenReturn(Optional.of(estoque));

        if (!ajusteExistente && !bloquearAntesDeAplicarAjuste) {
            when(almoxarifadoRepository.findById(1L)).thenReturn(Optional.of(item.getConferencia().getAlmoxarifado()));
            when(produtoRepository.findById(1L)).thenReturn(Optional.of(item.getProduto()));
        }
    }

    private ConferenciaEstoqueItem criarItem(StatusEnum status, Integer quantidadeEsperada, Integer quantidadeContada) {
        Almoxarifado almoxarifado = new Almoxarifado();
        almoxarifado.setId(1L);
        almoxarifado.setNome("Principal");

        ConferenciaEstoque conferencia = new ConferenciaEstoque();
        conferencia.setId(1L);
        conferencia.setStatus(status);
        conferencia.setAlmoxarifado(almoxarifado);

        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("ONU");

        ConferenciaEstoqueItem item = new ConferenciaEstoqueItem();
        item.setId(1L);
        item.setConferencia(conferencia);
        item.setProduto(produto);
        item.setQuantidadeEsperada(quantidadeEsperada);
        item.setQuantidadeContada(quantidadeContada);
        return item;
    }

    private AlmoxarifadoEstoque criarEstoque(ConferenciaEstoqueItem item, Integer quantidade) {
        AlmoxarifadoEstoque estoque = new AlmoxarifadoEstoque();
        estoque.setId(1L);
        estoque.setAlmoxarifado(item.getConferencia().getAlmoxarifado());
        estoque.setProduto(item.getProduto());
        estoque.setQuantidade(quantidade);
        return estoque;
    }
}
