package com.marlondev.stockflow.dto;

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
    private UnidadeMedida unidadeMedida;
    private Long categoriaId;
    private String categoriaNome;

    public ProdutoResponseDTO(){
    }

    public ProdutoResponseDTO(Produto produto) {
        id = produto.getId();
        nome = produto.getNome();
        preco = produto.getPreco();
        categoriaId = produto.getCategoria().getId();
        categoriaNome = produto.getCategoria().getNome();
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

    public UnidadeMedida getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getCategoriaNome() {
        return categoriaNome;
    }

    public void setCategoriaNome(String categoriaNome) {
        this.categoriaNome = categoriaNome;
    }
}
