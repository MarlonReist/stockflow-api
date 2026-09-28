package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.OrdemDeServico;
import com.marlondev.stockflow.domain.enums.StatusEnum;

import java.time.LocalDateTime;

public class TecnicoOrdemServicoResumoDTO {

    private Long id;
    private StatusEnum status;
    private String clienteNome;
    private String clienteTelefone;
    private String clienteEndereco;
    private String tipoOrdemServicoNome;
    private LocalDateTime dataAgendada;
    private LocalDateTime inicioAtendimento;

    public TecnicoOrdemServicoResumoDTO() {
    }

    public TecnicoOrdemServicoResumoDTO(OrdemDeServico os) {
        id = os.getId();
        status = os.getStatus();
        clienteNome = os.getCliente().getNome();
        clienteTelefone = os.getCliente().getTelefone();
        clienteEndereco = os.getCliente().getEndereco();
        dataAgendada = os.getDataAgendada();
        inicioAtendimento = os.getInicioAtendimento();

        if (os.getTipoOrdemServico() != null) {
            tipoOrdemServicoNome = os.getTipoOrdemServico().getNome();
        }
    }

    public Long getId() {
        return id;
    }

    public StatusEnum getStatus() {
        return status;
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

    public String getTipoOrdemServicoNome() {
        return tipoOrdemServicoNome;
    }

    public LocalDateTime getDataAgendada() {
        return dataAgendada;
    }

    public LocalDateTime getInicioAtendimento() {
        return inicioAtendimento;
    }
}