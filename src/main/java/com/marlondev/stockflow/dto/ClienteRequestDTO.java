package com.marlondev.stockflow.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public class ClienteRequestDTO {
    @NotBlank(message = "Nome é obrigatório!")
    private String nome;

    @NotBlank(message = "CPF é obrigatório!")
    @CPF (message =  "CPF é inválido!")
    private String cpf;

    @NotBlank(message = "Telefone é obrigatório!")
    private String telefone;

    @NotBlank(message = "Email é obrigatório!")
    @Email(message =  "Formato de email inválido!")
    private String email;

    public ClienteRequestDTO(){
    }

    public ClienteRequestDTO(String nome, String cpf, String telefone, String email) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
