package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.Cliente;
import com.marlondev.stockflow.domain.enums.TipoPessoaEnum;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class ClienteResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private TipoPessoaEnum tipoPessoa;
    private String cnpj;
    private String responsavelContato;
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
        tipoPessoa = cliente.getTipoPessoa();
        nome = cliente.getNome();
        cpf = cliente.getCpf();
        cnpj = cliente.getCnpj();
        telefone = cliente.getTelefone();
        email = cliente.getEmail();
        dataCadastro = cliente.getDataCadastro();
        endereco = cliente.getEndereco();
        responsavelContato = cliente.getResponsavelContato();
    }

    public Long getId() {
        return id;
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

    public String getResponsavelContato() {
        return responsavelContato;
    }

    public void setResponsavelContato(String responsavelContato) {
        this.responsavelContato = responsavelContato;
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
