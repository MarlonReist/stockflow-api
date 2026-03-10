package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.Categoria;
import com.marlondev.stockflow.domain.Produto;
import com.marlondev.stockflow.dto.ProdutoRequestDTO;
import com.marlondev.stockflow.dto.ProdutoResponseDTO;
import com.marlondev.stockflow.repositories.CategoriaRepository;
import com.marlondev.stockflow.repositories.ProdutoRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public ProdutoResponseDTO salvarProduto(ProdutoRequestDTO dto) {
        if (produtoRepository.findByNome(dto.getNome()).isPresent()) {
            throw new DatabaseException("Esse produto já existe!");
        }
        Categoria categoriaExiste = categoriaRepository.findById(dto.getCategoriaId())
        .orElseThrow(() -> new ResourceNotFoundException(dto.getCategoriaId()));

        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setPreco(dto.getPreco());
        produto.setUnidadeMedida(dto.getUnidadeMedida());
        produto.setCategoria(categoriaExiste);
        Produto produtoSalvo = produtoRepository.save(produto);
        return new ProdutoResponseDTO(produtoSalvo);
    }

    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return new ProdutoResponseDTO(produto);
    }

    public void deletarProdutoPorId(Long id) {
        buscarPorId(id);
        produtoRepository.deleteById(id);
    }

    public List<ProdutoResponseDTO> listarTodos() {
        List<Produto> list = produtoRepository.findAll();
        return list.stream().map(ProdutoResponseDTO::new).collect(Collectors.toList());
    }

    public ProdutoResponseDTO atualizarProduto(Long id, ProdutoRequestDTO dto) {
        Produto existente = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        Categoria categoriaExiste = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getCategoriaId()));

        Produto outroProduto = produtoRepository.findByNome(dto.getNome()).orElse(null);

        if (outroProduto == null || outroProduto.getId().equals(existente.getId())) {
            existente.setNome(dto.getNome());
            existente.setPreco(dto.getPreco());
            existente.setUnidadeMedida(dto.getUnidadeMedida());
            existente.setCategoria(categoriaExiste);
            Produto produtoSalvo = produtoRepository.save(existente);
            return new ProdutoResponseDTO(produtoSalvo);
        }
            throw new DatabaseException("Esse produto já existe");
        }
    }

