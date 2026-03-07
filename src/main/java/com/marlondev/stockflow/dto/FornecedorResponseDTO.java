package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.Fornecedor;

import java.io.Serial;
import java.io.Serializable;

public class FornecedorResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String nome;
    private String cnpj;

    public FornecedorResponseDTO(){
    }

    public FornecedorResponseDTO(Fornecedor fornecedor){
        id = fornecedor.getId();
        nome = fornecedor.getNome();
        cnpj = fornecedor.getCnpj();
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

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}
