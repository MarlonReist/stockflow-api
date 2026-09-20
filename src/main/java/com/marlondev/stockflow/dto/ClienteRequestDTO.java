package com.marlondev.stockflow.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import com.marlondev.stockflow.domain.enums.TipoPessoaEnum;
import jakarta.validation.constraints.NotNull;

public class ClienteRequestDTO {
    @NotNull(message = "Tipo de pessoa é obrigatório!")
    private TipoPessoaEnum tipoPessoa;

    @NotBlank(message = "Nome é obrigatório!")
    private String nome;

    private String cpf;

    private String cnpj;

    @NotBlank(message = "Telefone é obrigatório!")
    private String telefone;

    @NotBlank(message = "Email é obrigatório!")
    @Email(message =  "Formato de email inválido!")
    private String email;

    @NotBlank (message = "Endereço é obrigatório!")
    private String endereco;

    private String responsavelContato;

    public ClienteRequestDTO(){
    }

    public ClienteRequestDTO(String nome, String cpf, String telefone, String email, String endereco) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
    }

    public TipoPessoaEnum getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(TipoPessoaEnum tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco (String endereco) {
        this.endereco = endereco;
    }

    public String getResponsavelContato() {
        return responsavelContato;
    }

    public void setResponsavelContato(String responsavelContato) {
        this.responsavelContato = responsavelContato;
    }
}
