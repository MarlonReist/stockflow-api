package com.marlondev.stockflow.domain;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name = "almoxarifado")
@Entity
public class Almoxarifado implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String nome;

    @OneToMany(mappedBy = "almoxarifado")
    private final List<AlmoxarifadoEstoque> estoque = new ArrayList<>();

    public Almoxarifado(){
    }

    public Almoxarifado(Long id, String nome) {
        this.id = id;
        this.nome = nome;
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

    public void setNome (String nome) {
        this.nome = nome;
    }

    public List<AlmoxarifadoEstoque> getEstoque() {
        return estoque;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Almoxarifado that = (Almoxarifado) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
