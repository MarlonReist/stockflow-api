package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.Produto;
import com.marlondev.stockflow.repositories.ProdutoRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public void salvarProduto(Produto produto) {
        if(produtoRepository.findByNome(produto.getNome()).isEmpty()) {
            produtoRepository.save(produto);
        } else {
            throw new DatabaseException("Esse produto já existe");
        }
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public void deletarProdutoPorId(Long id) {
        buscarPorId(id);
        produtoRepository.deleteById(id);
    }

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public void atualizarProduto(Produto produto) {
        Produto existente = produtoRepository.findById(produto.getId())
                .orElseThrow(() -> new ResourceNotFoundException(produto.getId()));
        Produto outroProduto = produtoRepository.findByNome(produto.getNome()).orElse(null);

        if (outroProduto == null || outroProduto.getId().equals(existente.getId())) {
            existente.setNome(produto.getNome());
            existente.setPreco(produto.getPreco());
            produtoRepository.save(existente);
        } else {
            throw new DatabaseException("Esse produto já existe");
        }
    }

}
