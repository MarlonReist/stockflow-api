package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.*;
import com.marlondev.stockflow.domain.enums.StatusEnum;
import com.marlondev.stockflow.dto.OrdemDeServicoRequestDTO;
import com.marlondev.stockflow.dto.OrdemDeServicoResponseDTO;
import com.marlondev.stockflow.repositories.ClienteRepository;
import com.marlondev.stockflow.repositories.ColaboradorRepository;
import com.marlondev.stockflow.repositories.OrdemDeServicoRepository;
import com.marlondev.stockflow.repositories.ProdutoRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrdemDeServicoService {

    private final OrdemDeServicoRepository ordemDeServicoRepository;
    private final ClienteRepository clienteRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final ProdutoRepository produtoRepository;

    public OrdemDeServicoService(OrdemDeServicoRepository ordemDeServicoRepository, ClienteRepository clienteRepository, ColaboradorRepository colaboradorRepository, ProdutoRepository produtoRepository) {
        this.ordemDeServicoRepository = ordemDeServicoRepository;
        this.clienteRepository = clienteRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.produtoRepository = produtoRepository;
    }

    public OrdemDeServico salvarOs(OrdemDeServicoRequestDTO dto) {
        OrdemDeServico os = new OrdemDeServico();
        os.setDescricao(dto.getDescricao());
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getClienteId()));
        Colaborador colaborador = colaboradorRepository.findById(dto.getColaboradorId())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getClienteId()));
        os.setCliente(cliente);
        os.setColaborador(colaborador);
        os.setDataAbertura(LocalDate.now());
        os.setStatus(StatusEnum.ABERTA);
        return ordemDeServicoRepository.save(os);
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


    public void atualizarDescricao(Long id, String novaDescricao) {
        OrdemDeServico osExistente = buscarPorId(id);

        if (osExistente.getStatus() == StatusEnum.ABERTA) {
            osExistente.setDescricao(novaDescricao);
            ordemDeServicoRepository.save(osExistente);
        } else {
            throw new DatabaseException("Ordem de serviço não está aberta!");
        }
    }

    @Transactional
    public void finalizarOs(Long id) {
        OrdemDeServico osExistente = buscarPorId(id);

        if (osExistente.getStatus() == StatusEnum.ABERTA) {
            osExistente.setStatus(StatusEnum.FINALIZADA);
            osExistente.setDataFechamento(LocalDate.now());
            ordemDeServicoRepository.save(osExistente);
        } else {
            throw new DatabaseException("Ordem de serviço não está aberta!");
        }
    }

    @Transactional
    public void cancelarOs(Long id) {
        OrdemDeServico osExistente = buscarPorId(id);

        if (osExistente.getStatus() == StatusEnum.ABERTA) {
            osExistente.setStatus(StatusEnum.CANCELADA);
            osExistente.setDataFechamento(LocalDate.now());
            ordemDeServicoRepository.save(osExistente);
        } else {
            throw new DatabaseException("Ordem de serviço não está aberta!");
        }
    }
}
