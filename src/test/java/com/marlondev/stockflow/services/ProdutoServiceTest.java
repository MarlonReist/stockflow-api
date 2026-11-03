package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.Produto;
import com.marlondev.stockflow.repositories.CategoriaRepository;
import com.marlondev.stockflow.repositories.MovimentacaoEstoqueRepository;
import com.marlondev.stockflow.repositories.ProdutoRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    private ProdutoService service;

    @BeforeEach
    void setUp() {
        service = new ProdutoService(produtoRepository, categoriaRepository, movimentacaoEstoqueRepository);
    }

    @Test
    void produtoComMovimentacoesNaoPodeSerExcluido() {
        Produto produto = new Produto();
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(movimentacaoEstoqueRepository.existsByProdutoId(1L)).thenReturn(true);

        DatabaseException exception = assertThrows(DatabaseException.class, () -> service.deletarProdutoPorId(1L));

        assertEquals("N\u00e3o \u00e9 poss\u00edvel excluir este produto porque ele possui movimenta\u00e7\u00f5es registradas.", exception.getMessage());
        verify(produtoRepository, never()).deleteById(1L);
    }
}
