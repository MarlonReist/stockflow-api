package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.AjusteEstoque;
import com.marlondev.stockflow.domain.enums.TipoAjusteEstoque;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class AjusteEstoqueResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private LocalDateTime dataHora;
    private TipoAjusteEstoque tipo;
    private Long almoxarifadoId;
    private String almoxarifadoNome;
    private Long produtoId;
    private String produtoNome;
    private Integer quantidade;
    private String motivo;
    private Long usuarioResponsavelId;
    private String usuarioResponsavelNome;
    private Long conferenciaEstoqueId;
    private Long conferenciaEstoqueItemId;

    public AjusteEstoqueResponseDTO() {
    }

    public AjusteEstoqueResponseDTO(AjusteEstoque ajuste) {
        id = ajuste.getId();
        dataHora = ajuste.getDataHora();
        tipo = ajuste.getTipo();
        almoxarifadoId = ajuste.getAlmoxarifado().getId();
        almoxarifadoNome = ajuste.getAlmoxarifado().getNome();
        produtoId = ajuste.getProduto().getId();
        produtoNome = ajuste.getProduto().getNome();
        quantidade = ajuste.getQuantidade();
        motivo = ajuste.getMotivo();
        usuarioResponsavelId = ajuste.getUsuarioResponsavel().getId();
        usuarioResponsavelNome = ajuste.getUsuarioResponsavel().getNome();

        if (ajuste.getConferenciaEstoque() != null) {
            conferenciaEstoqueId = ajuste.getConferenciaEstoque().getId();
        }

        if (ajuste.getConferenciaEstoqueItem() != null) {
            conferenciaEstoqueItemId = ajuste.getConferenciaEstoqueItem().getId();
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public TipoAjusteEstoque getTipo() {
        return tipo;
    }

    public Long getAlmoxarifadoId() {
        return almoxarifadoId;
    }

    public String getAlmoxarifadoNome() {
        return almoxarifadoNome;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public String getMotivo() {
        return motivo;
    }

    public Long getUsuarioResponsavelId() {
        return usuarioResponsavelId;
    }

    public String getUsuarioResponsavelNome() {
        return usuarioResponsavelNome;
    }

    public Long getConferenciaEstoqueId() {
        return conferenciaEstoqueId;
    }

    public Long getConferenciaEstoqueItemId() {
        return conferenciaEstoqueItemId;
    }
}