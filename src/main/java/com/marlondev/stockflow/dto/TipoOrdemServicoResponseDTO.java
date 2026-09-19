package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.TipoOrdemServico;

import java.io.Serial;
import java.io.Serializable;

public class TipoOrdemServicoResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String nome;
    private Boolean ativo;

    public TipoOrdemServicoResponseDTO() {
    }

    public TipoOrdemServicoResponseDTO(TipoOrdemServico tipo) {
        id = tipo.getId();
        nome = tipo.getNome();
        ativo = tipo.getAtivo();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}