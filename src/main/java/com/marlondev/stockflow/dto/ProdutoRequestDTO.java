package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.enums.UnidadeMedida;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ProdutoRequestDTO {
    @NotBlank (message = "Nome é obrigatório!")
    private String nome;

    @Positive (message = "Preço deve ser maior que 0")
    @NotNull (message = "Preço é obrigatório!")
    private Double preco;

    @NotBlank(message = "Categoria é obrigatória!")
    private String categoria;

    @NotNull(message = "Unidade de medida é obrigatória!")
    private UnidadeMedida unidadeMedida;

    public ProdutoRequestDTO(){
    }

    public ProdutoRequestDTO(String nome, Double preco, String categoria, UnidadeMedida unidadeMedida) {
        this.nome = nome;
        this.preco = preco;
        this.categoria = categoria;
        this.unidadeMedida = unidadeMedida;
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public UnidadeMedida getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }
}
