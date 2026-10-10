package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.OrdemDeServico;
import com.marlondev.stockflow.domain.enums.StatusEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TecnicoHistoricoOrdemServicoResumoDTO {

    private Long id;
    private String clienteNome;
    private Long tipoOrdemServicoId;
    private String tipoOrdemServicoNome;
    private LocalDate dataAbertura;
    private LocalDateTime dataAgendada;
    private LocalDateTime inicioAtendimento;
    private LocalDateTime fimAtendimento;
    private StatusEnum status;
    private Long tecnicoResponsavelId;
    private String tecnicoResponsavelNome;
    private Long ajudanteId;
    private String ajudanteNome;
    private String participacaoDoTecnico;

    public TecnicoHistoricoOrdemServicoResumoDTO() {
    }

    public TecnicoHistoricoOrdemServicoResumoDTO(OrdemDeServico os, Long tecnicoId) {
        id = os.getId();
        clienteNome = os.getCliente().getNome();
        dataAbertura = os.getDataAbertura();
        dataAgendada = os.getDataAgendada();
        inicioAtendimento = os.getInicioAtendimento();
        fimAtendimento = os.getFimAtendimento();
        status = os.getStatus();

        if (os.getTipoOrdemServico() != null) {
            tipoOrdemServicoId = os.getTipoOrdemServico().getId();
            tipoOrdemServicoNome = os.getTipoOrdemServico().getNome();
        }

        if (os.getColaborador() != null) {
            tecnicoResponsavelId = os.getColaborador().getId();
            tecnicoResponsavelNome = os.getColaborador().getNome();
        }

        if (os.getAjudante() != null) {
            ajudanteId = os.getAjudante().getId();
            ajudanteNome = os.getAjudante().getNome();
        }

        participacaoDoTecnico = resolverParticipacao(tecnicoId);
    }

    private String resolverParticipacao(Long tecnicoId) {
        boolean responsavel = tecnicoId != null && tecnicoId.equals(tecnicoResponsavelId);
        boolean ajudante = tecnicoId != null && tecnicoId.equals(ajudanteId);

        if (responsavel && ajudante) {
            return "RESPONSAVEL_E_AJUDANTE";
        }
        if (responsavel) {
            return "RESPONSAVEL";
        }
        if (ajudante) {
            return "AJUDANTE";
        }
        return null;
    }

    public Long getId() {
        return id;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public Long getTipoOrdemServicoId() {
        return tipoOrdemServicoId;
    }

    public String getTipoOrdemServicoNome() {
        return tipoOrdemServicoNome;
    }

    public LocalDate getDataAbertura() {
        return dataAbertura;
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

    public StatusEnum getStatus() {
        return status;
    }

    public Long getTecnicoResponsavelId() {
        return tecnicoResponsavelId;
    }

    public String getTecnicoResponsavelNome() {
        return tecnicoResponsavelNome;
    }

    public Long getAjudanteId() {
        return ajudanteId;
    }

    public String getAjudanteNome() {
        return ajudanteNome;
    }

    public String getParticipacaoDoTecnico() {
        return participacaoDoTecnico;
    }
}
