package com.marlondev.stockflow.dto;
import com.marlondev.stockflow.domain.OrdemDeServico;
import com.marlondev.stockflow.domain.enums.StatusEnum;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
public class OrdemDeServicoResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private StatusEnum status;
    private String descricao;
    private ClienteResponseDTO cliente;
    private ColaboradorResponseDTO colaborador;
    private Double valorTotal;
    private LocalDate dataAbertura;
    private LocalDate dataFechamento;

    public OrdemDeServicoResponseDTO(){
    }

    public OrdemDeServicoResponseDTO(OrdemDeServico os){
        id = os.getId();
        status = os.getStatus();
        descricao = os.getDescricao();
        cliente = new ClienteResponseDTO(os.getCliente());
        colaborador = new ColaboradorResponseDTO(os.getColaborador());
        dataAbertura = os.getDataAbertura();
        dataFechamento = os.getDataFechamento();
        //valorTotal = os.getValorTotal();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public ClienteResponseDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteResponseDTO cliente) {
        this.cliente = cliente;
    }

    public ColaboradorResponseDTO getColaborador() {
        return colaborador;
    }

    public void setColaborador(ColaboradorResponseDTO colaborador) {
        this.colaborador = colaborador;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }
}