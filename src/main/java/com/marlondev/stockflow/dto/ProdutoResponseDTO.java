package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.Categoria;
import com.marlondev.stockflow.domain.Produto;
import com.marlondev.stockflow.domain.enums.UnidadeMedida;

import java.io.Serial;
import java.io.Serializable;

public class ProdutoResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String nome;
    private Double preco;
    private Categoria categoria;
    private UnidadeMedida unidadeMedida;

    public ProdutoResponseDTO(){
    }

    public ProdutoResponseDTO(Produto produto) {
        id = produto.getId();
        nome = produto.getNome();
        preco = produto.getPreco();
        categoria = produto.getCategoria();
        unidadeMedida = produto.getUnidadeMedida();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public UnidadeMedida getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }
}
