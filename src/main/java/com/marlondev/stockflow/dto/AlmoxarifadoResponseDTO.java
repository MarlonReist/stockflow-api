package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.Almoxarifado;

import java.io.Serial;
import java.io.Serializable;

public class AlmoxarifadoResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String nome;
    private boolean principal;

    public AlmoxarifadoResponseDTO(){
    }

    public AlmoxarifadoResponseDTO(Almoxarifado almoxarifado){
        id = almoxarifado.getId();
        nome = almoxarifado.getNome();
        principal = almoxarifado.isPrincipal();
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

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }
}
