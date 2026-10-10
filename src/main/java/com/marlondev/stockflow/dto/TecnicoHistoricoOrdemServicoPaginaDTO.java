package com.marlondev.stockflow.dto;

import java.util.List;

public class TecnicoHistoricoOrdemServicoPaginaDTO {

    private List<TecnicoHistoricoOrdemServicoResumoDTO> content;
    private Long totalElements;
    private Integer totalPages;
    private Integer page;
    private Integer size;

    public TecnicoHistoricoOrdemServicoPaginaDTO() {
    }

    public TecnicoHistoricoOrdemServicoPaginaDTO(
            List<TecnicoHistoricoOrdemServicoResumoDTO> content,
            Long totalElements,
            Integer totalPages,
            Integer page,
            Integer size
    ) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.page = page;
        this.size = size;
    }

    public List<TecnicoHistoricoOrdemServicoResumoDTO> getContent() {
        return content;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getSize() {
        return size;
    }
}
