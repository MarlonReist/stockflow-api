package com.marlondev.stockflow.dto;

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

    @Positive (message = "Quantidade deve ser maior que 0")
    @Min(value = 0, message = "Quantidade não pode ser negativa!")
    private Integer quantidade;

    public ProdutoRequestDTO(){
    }

    public ProdutoRequestDTO(String nome, Double preco, String categoria, Integer quantidade) {
        this.nome = nome;
        this.preco = preco;
        this.categoria = categoria;
        this.quantidade = quantidade;
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

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
