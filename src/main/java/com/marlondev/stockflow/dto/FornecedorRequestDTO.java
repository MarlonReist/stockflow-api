package com.marlondev.stockflow.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CNPJ;

public class FornecedorRequestDTO {

    @NotBlank(message = "Nome é obrigatório!")
    private String nome;

    @NotBlank(message = "CNPJ é obrigatório!")
    @CNPJ
    private String cnpj;

    public FornecedorRequestDTO(){
    }

    public FornecedorRequestDTO(String nome, String cnpj){
        this.nome = nome;
        this.cnpj = cnpj;
    }

    public String getNome(){
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getCnpj(){
        return cnpj;
    }

    public void setCnpj(String cnpj){
        this.cnpj = cnpj;
    }
}
