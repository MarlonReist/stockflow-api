package com.marlondev.stockflow.dto;

import java.util.List;

public class RelatorioTecnicoAtendimentoPaginaDTO {

    private List<RelatorioTecnicoAtendimentoDTO> content;
    private Long totalElements;
    private Integer totalPages;
    private Integer page;
    private Integer size;

    public RelatorioTecnicoAtendimentoPaginaDTO() {
    }

    public RelatorioTecnicoAtendimentoPaginaDTO(List<RelatorioTecnicoAtendimentoDTO> content,
                                                Long totalElements,
                                                Integer totalPages,
                                                Integer page,
                                                Integer size) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.page = page;
        this.size = size;
    }

    public List<RelatorioTecnicoAtendimentoDTO> getContent() {
        return content;
    }

    public void setContent(List<RelatorioTecnicoAtendimentoDTO> content) {
        this.content = content;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
