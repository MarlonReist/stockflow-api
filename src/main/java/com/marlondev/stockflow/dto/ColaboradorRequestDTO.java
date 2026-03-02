package com.marlondev.stockflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

public class ColaboradorRequestDTO {
    @NotBlank(message = "Nome é obrigatório!")
    private String nome;

    @NotBlank(message = "Cpf é obrigatório!")
    @CPF(message = "Cpf inválido!")
    private String cpf;

    @NotBlank(message = "Cargo é obrigatório!")
    private String cargo;

    @NotBlank(message = "telefone é obrigatório!")
    private String telefone;

    public ColaboradorRequestDTO(){
    }

    public ColaboradorRequestDTO(String nome, String cpf, String cargo, String telefone) {
        this.nome = nome;
        this.cpf = cpf;
        this.cargo = cargo;
        this.telefone = telefone;
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
