package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.Categoria;

import java.io.Serial;
import java.io.Serializable;

public class CategoriaResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String nome;

    public CategoriaResponseDTO(){
    }

    public CategoriaResponseDTO(Categoria categoria){
        id = categoria.getId();
        nome = categoria.getNome();
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
}
