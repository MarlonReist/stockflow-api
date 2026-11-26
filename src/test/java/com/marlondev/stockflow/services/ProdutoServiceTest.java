package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.Categoria;
import com.marlondev.stockflow.domain.Produto;
import com.marlondev.stockflow.domain.enums.UnidadeMedida;
import com.marlondev.stockflow.dto.ProdutoRequestDTO;
import com.marlondev.stockflow.dto.ProdutoResponseDTO;
import com.marlondev.stockflow.repositories.CategoriaRepository;
import com.marlondev.stockflow.repositories.MovimentacaoEstoqueRepository;
import com.marlondev.stockflow.repositories.ProdutoRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
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

    private Produto criarProdutoValido(Long id) {
        Categoria categoria = new Categoria();
        categoria.setId(10L);
        categoria.setNome("Roteadores");

        Produto produto = new Produto();
        produto.setId(id);
        produto.setNome("Roteador AX12");
        produto.setPreco(250.0);
        produto.setUnidadeMedida(UnidadeMedida.UNIDADES);
        produto.setEstoqueMinimo(5);
        produto.setCategoria(categoria);
        return produto;
    }

    @Test
    void produtoComMovimentacoesNaoPodeSerExcluido() {
        Produto produto = criarProdutoValido(1L);
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(movimentacaoEstoqueRepository.existsByProdutoId(1L)).thenReturn(true);

        DatabaseException exception = assertThrows(DatabaseException.class, () -> service.deletarProdutoPorId(1L));

        assertEquals("N\u00e3o \u00e9 poss\u00edvel excluir este produto porque ele possui movimenta\u00e7\u00f5es registradas.", exception.getMessage());
        verify(produtoRepository, never()).deleteById(1L);
    }

    @Test
    void produtoSemMovimentacoesDeveSerExcluidoComSucesso() {
        Produto produto = criarProdutoValido(1L);
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(movimentacaoEstoqueRepository.existsByProdutoId(1L)).thenReturn(false);

        service.deletarProdutoPorId(1L);

        verify(produtoRepository).deleteById(1L);
    }

    @Test
    void excluirProdutoInexistenteDeveLancarResourceNotFound() {
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.deletarProdutoPorId(99L));
        verify(produtoRepository, never()).deleteById(99L);
    }

    @Test
    void salvarProdutoNovoComSucesso() {
        Categoria categoria = new Categoria();
        categoria.setId(10L);
        categoria.setNome("Roteadores");

        ProdutoRequestDTO dto = new ProdutoRequestDTO();
        dto.setNome("ONU Bridge");
        dto.setPreco(120.0);
        dto.setUnidadeMedida(UnidadeMedida.UNIDADES);
        dto.setCategoriaId(10L);
        dto.setEstoqueMinimo(3);

        Produto salvo = criarProdutoValido(2L);
        salvo.setNome("ONU Bridge");

        when(produtoRepository.findByNome("ONU Bridge")).thenReturn(Optional.empty());
        when(categoriaRepository.findById(10L)).thenReturn(Optional.of(categoria));
        when(produtoRepository.save(any(Produto.class))).thenReturn(salvo);

        ProdutoResponseDTO response = service.salvarProduto(dto);

        assertEquals(2L, response.getId());
        assertEquals("ONU Bridge", response.getNome());
        assertEquals("Roteadores", response.getCategoriaNome());
    }

    @Test
    void salvarProdutoComNomeDuplicadoDeveLancarDatabaseException() {
        ProdutoRequestDTO dto = new ProdutoRequestDTO();
        dto.setNome("Roteador AX12");
        dto.setCategoriaId(10L);

        when(produtoRepository.findByNome("Roteador AX12")).thenReturn(Optional.of(criarProdutoValido(1L)));

        DatabaseException exception = assertThrows(DatabaseException.class, () -> service.salvarProduto(dto));

        assertEquals("Esse produto j\u00e1 existe!", exception.getMessage());
        verify(produtoRepository, never()).save(any(Produto.class));
    }
}
