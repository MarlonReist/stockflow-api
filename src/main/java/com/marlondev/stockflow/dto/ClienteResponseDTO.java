package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.Cliente;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class ClienteResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private LocalDate dataCadastro;
    private String endereco;

    public ClienteResponseDTO(){
    }

    public ClienteResponseDTO(Cliente cliente) {
        id = cliente.getId();
        nome = cliente.getNome();
        cpf = cliente.getCpf();
        telefone = cliente.getTelefone();
        email = cliente.getEmail();
        dataCadastro = cliente.getDataCadastro();
        endereco = cliente.getEndereco();
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

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
