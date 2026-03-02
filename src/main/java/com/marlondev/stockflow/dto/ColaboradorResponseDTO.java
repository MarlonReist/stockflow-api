package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.Colaborador;

import java.io.Serial;
import java.io.Serializable;

public class ColaboradorResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String nome;
    private String cpf;
    private String cargo;
    private String telefone;

    public ColaboradorResponseDTO(){
    }

    public ColaboradorResponseDTO(Colaborador colaborador) {
        id = colaborador.getId();
        nome = colaborador.getNome();
        cpf = colaborador.getCpf();
        cargo = colaborador.getCargo();
        telefone = colaborador.getTelefone();
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
