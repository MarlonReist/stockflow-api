package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.Cliente;
import com.marlondev.stockflow.domain.Colaborador;
import com.marlondev.stockflow.domain.OrdemDeServico;
import com.marlondev.stockflow.domain.enums.StatusEnum;
import com.marlondev.stockflow.repositories.ClienteRepository;
import com.marlondev.stockflow.repositories.ColaboradorRepository;
import com.marlondev.stockflow.repositories.OrdemDeServicoRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrdemDeServicoService {

    private final OrdemDeServicoRepository ordemDeServicoRepository;
    private final ClienteRepository clienteRepository;
    private final ColaboradorRepository colaboradorRepository;

    public OrdemDeServicoService(OrdemDeServicoRepository ordemDeServicoRepository, ClienteRepository clienteRepository, ColaboradorRepository colaboradorRepository) {
        this.ordemDeServicoRepository = ordemDeServicoRepository;
        this.clienteRepository = clienteRepository;
        this.colaboradorRepository = colaboradorRepository;
    }

    public void salvarOs(OrdemDeServico ordemDeServico) {
        if (ordemDeServico.getCliente() == null || ordemDeServico.getCliente().getId() == null) {
            throw new DatabaseException("Cliente é obrigatório!");
        } else if (ordemDeServico.getColaborador() == null || ordemDeServico.getColaborador().getId() == null) {
            throw new DatabaseException("Colaborador é obrigatório!");
        } else {
            Cliente clienteExistente = clienteRepository.findById(ordemDeServico.getCliente().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(ordemDeServico.getCliente().getId()));

            Colaborador colaboradorExistente = colaboradorRepository.findById(ordemDeServico.getColaborador().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(ordemDeServico.getColaborador().getId()));

            ordemDeServico.setCliente(clienteExistente);
            ordemDeServico.setColaborador(colaboradorExistente);
            ordemDeServico.setDataAbertura(LocalDate.now());
            ordemDeServico.setStatus(StatusEnum.ABERTA);
            ordemDeServicoRepository.save(ordemDeServico);
        }
    }

    public OrdemDeServico buscarPorId(Long id) {
        return ordemDeServicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public void deletarOsPorId(Long id) {
        OrdemDeServico os = buscarPorId(id);
        if (os.getStatus() == StatusEnum.ABERTA) {
            ordemDeServicoRepository.delete(os);
        } else {
            throw new DatabaseException("Ordem de serviço não está aberta!");
        }
    }

    public List<OrdemDeServico> listarTodos() {
        return ordemDeServicoRepository.findAll();
    }


    public void atualizarDescricao(OrdemDeServico ordemDeServico) {
        OrdemDeServico osExistente = ordemDeServicoRepository.findById(ordemDeServico.getId())
                .orElseThrow(() -> new ResourceNotFoundException(ordemDeServico.getId()));

        if (osExistente.getStatus() == StatusEnum.ABERTA) {
            osExistente.setDescricao(ordemDeServico.getDescricao());
            ordemDeServicoRepository.save(osExistente);
        } else {
            throw new DatabaseException("Ordem de serviço não está aberta!");
        }
    }

    public void finalizarOs(OrdemDeServico ordemDeServico) {
        OrdemDeServico osExistente = ordemDeServicoRepository.findById(ordemDeServico.getId())
                .orElseThrow(() -> new ResourceNotFoundException(ordemDeServico.getId()));

        if (osExistente.getStatus() == StatusEnum.ABERTA) {
            osExistente.setStatus(StatusEnum.FINALIZADA);
            osExistente.setDataFechamento(LocalDate.now());
            ordemDeServicoRepository.save(osExistente);
        } else {
            throw new DatabaseException("Ordem de serviço não está aberta!");
        }
    }
    public void cancelarOs(OrdemDeServico ordemDeServico) {
        OrdemDeServico osExistente = ordemDeServicoRepository.findById(ordemDeServico.getId())
                .orElseThrow(() -> new ResourceNotFoundException(ordemDeServico.getId()));

        if (osExistente.getStatus() == StatusEnum.ABERTA) {
            osExistente.setStatus(StatusEnum.CANCELADA);
            osExistente.setDataFechamento(LocalDate.now());
            ordemDeServicoRepository.save(osExistente);
        } else {
            throw new DatabaseException("Ordem de serviço não está aberta!");
        }
    }
}
