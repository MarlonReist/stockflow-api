package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.ConferenciaEstoque;
import com.marlondev.stockflow.domain.enums.StatusEnum;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ConferenciaEstoqueResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long almoxarifadoId;
    private String almoxarifadoNome;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFinalizacao;
    private Long usuarioResponsavelId;
    private String usuarioResponsavelNome;
    private StatusEnum status;
    private Integer totalProdutosConferidos;
    private Integer totalSemDivergencia;
    private Integer totalComDivergencia;
    private List<ConferenciaEstoqueItemResponseDTO> itens;

    public ConferenciaEstoqueResponseDTO() {
    }

    public ConferenciaEstoqueResponseDTO(ConferenciaEstoque conferencia) {
        id = conferencia.getId();
        almoxarifadoId = conferencia.getAlmoxarifado().getId();
        almoxarifadoNome = conferencia.getAlmoxarifado().getNome();
        dataHoraInicio = conferencia.getDataHoraInicio();
        dataHoraFinalizacao = conferencia.getDataHoraFinalizacao();
        usuarioResponsavelId = conferencia.getUsuarioResponsavel().getId();
        usuarioResponsavelNome = conferencia.getUsuarioResponsavel().getNome();
        status = conferencia.getStatus();

        totalProdutosConferidos = conferencia.getItens().size();
        totalComDivergencia = (int) conferencia.getItens()
                .stream()
                .filter(item -> item.possuiDivergencia())
                .count();
        totalSemDivergencia = (int) conferencia.getItens()
                .stream()
                .filter(item -> item.getQuantidadeContada() != null && !item.possuiDivergencia())
                .count();

        itens = conferencia.getItens()
                .stream()
                .map(ConferenciaEstoqueItemResponseDTO::new)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public Long getAlmoxarifadoId() {
        return almoxarifadoId;
    }

    public String getAlmoxarifadoNome() {
        return almoxarifadoNome;
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public LocalDateTime getDataHoraFinalizacao() {
        return dataHoraFinalizacao;
    }

    public Long getUsuarioResponsavelId() {
        return usuarioResponsavelId;
    }

    public String getUsuarioResponsavelNome() {
        return usuarioResponsavelNome;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public Integer getTotalProdutosConferidos() {
        return totalProdutosConferidos;
    }

    public Integer getTotalSemDivergencia() {
        return totalSemDivergencia;
    }

    public Integer getTotalComDivergencia() {
        return totalComDivergencia;
    }

    public List<ConferenciaEstoqueItemResponseDTO> getItens() {
        return itens;
    }
}