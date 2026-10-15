package com.marlondev.stockflow.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "modelo_roteador_sugestao", uniqueConstraints = @UniqueConstraint(columnNames = "modelo_normalizado"))
public class ModeloRoteadorSugestao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String modelo;

    @Column(name = "modelo_normalizado", nullable = false, length = 150)
    private String modeloNormalizado;

    @Column(nullable = false)
    private long frequencia;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    public Long getId() { return id; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getModeloNormalizado() { return modeloNormalizado; }
    public void setModeloNormalizado(String modeloNormalizado) { this.modeloNormalizado = modeloNormalizado; }
    public long getFrequencia() { return frequencia; }
    public void setFrequencia(long frequencia) { this.frequencia = frequencia; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }
}
