package com.marlondev.stockflow.dto;

import com.marlondev.stockflow.domain.OrdemServicoAnexo;

import java.time.LocalDateTime;

public class OrdemServicoAnexoResponseDTO {

    private Long id;
    private Long osId;
    private String nomeOriginal;
    private String contentType;
    private Long tamanho;
    private LocalDateTime dataUpload;

    public OrdemServicoAnexoResponseDTO(OrdemServicoAnexo anexo) {
        id = anexo.getId();
        osId = anexo.getOrdemDeServico().getId();
        nomeOriginal = anexo.getNomeOriginal();
        contentType = anexo.getContentType();
        tamanho = anexo.getTamanho();
        dataUpload = anexo.getDataUpload();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOsId() {
        return osId;
    }

    public void setOsId(Long osId) {
        this.osId = osId;
    }

    public String getNomeOriginal() {
        return nomeOriginal;
    }

    public void setNomeOriginal(String nomeOriginal) {
        this.nomeOriginal = nomeOriginal;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getTamanho() {
        return tamanho;
    }

    public void setTamanho(Long tamanho) {
        this.tamanho = tamanho;
    }

    public LocalDateTime getDataUpload() {
        return dataUpload;
    }

    public void setDataUpload(LocalDateTime dataUpload) {
        this.dataUpload = dataUpload;
    }
}
