package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.OrdemDeServico;
import com.marlondev.stockflow.domain.enums.StatusEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TecnicoOrdemServicoDetalheDTO {

    private Long id;
    private LocalDate dataAbertura;
    private StatusEnum status;
    private String descricao;
    private Long clienteId;
    private String clienteNome;
    private String clienteTelefone;
    private String clienteEndereco;
    private Long tipoOrdemServicoId;
    private String tipoOrdemServicoNome;
    private LocalDateTime dataAgendada;
    private LocalDateTime inicioAtendimento;
    private LocalDateTime fimAtendimento;
    private String observacaoConclusao;
    private List<OrdemServicoItemResponseDTO> produtosUtilizados;
    private List<OrdemServicoAnexoResponseDTO> anexos;

    public TecnicoOrdemServicoDetalheDTO() {
    }

    public TecnicoOrdemServicoDetalheDTO(
            OrdemDeServico os,
            List<OrdemServicoItemResponseDTO> produtosUtilizados,
            List<OrdemServicoAnexoResponseDTO> anexos
    ) {
        id = os.getId();
        dataAbertura = os.getDataAbertura();
        status = os.getStatus();
        descricao = os.getDescricao();
        clienteId = os.getCliente().getId();
        clienteNome = os.getCliente().getNome();
        clienteTelefone = os.getCliente().getTelefone();
        clienteEndereco = os.getCliente().getEndereco();
        dataAgendada = os.getDataAgendada();
        inicioAtendimento = os.getInicioAtendimento();
        fimAtendimento = os.getFimAtendimento();
        observacaoConclusao = os.getObservacaoConclusao();
        this.produtosUtilizados = produtosUtilizados;
        this.anexos = anexos;

        if (os.getTipoOrdemServico() != null) {
            tipoOrdemServicoId = os.getTipoOrdemServico().getId();
            tipoOrdemServicoNome = os.getTipoOrdemServico().getNome();
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDataAbertura() {
        return dataAbertura;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public String getDescricao() {
        return descricao;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public String getClienteTelefone() {
        return clienteTelefone;
    }

    public String getClienteEndereco() {
        return clienteEndereco;
    }

    public Long getTipoOrdemServicoId() {
        return tipoOrdemServicoId;
    }

    public String getTipoOrdemServicoNome() {
        return tipoOrdemServicoNome;
    }

    public LocalDateTime getDataAgendada() {
        return dataAgendada;
    }

    public LocalDateTime getInicioAtendimento() {
        return inicioAtendimento;
    }

    public LocalDateTime getFimAtendimento() {
        return fimAtendimento;
    }

    public String getObservacaoConclusao() {
        return observacaoConclusao;
    }

    public List<OrdemServicoItemResponseDTO> getProdutosUtilizados() {
        return produtosUtilizados;
    }

    public List<OrdemServicoAnexoResponseDTO> getAnexos() {
        return anexos;
    }
}