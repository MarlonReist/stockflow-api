package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.Cliente;
import com.marlondev.stockflow.domain.Colaborador;
import com.marlondev.stockflow.domain.OrdemDeServico;
import com.marlondev.stockflow.domain.enums.StatusEnum;
import com.marlondev.stockflow.dto.OrdemDeServicoRequestDTO;
import com.marlondev.stockflow.dto.OrdemDeServicoResponseDTO;
import com.marlondev.stockflow.repositories.ClienteRepository;
import com.marlondev.stockflow.repositories.ColaboradorRepository;
import com.marlondev.stockflow.repositories.OrdemDeServicoRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional
    public OrdemDeServicoResponseDTO salvar(OrdemDeServicoRequestDTO dto) {
        Cliente clienteEncontrado = clienteRepository.findById((dto.getClienteId()))
                .orElseThrow(() -> new ResourceNotFoundException(dto.getClienteId()));
        Colaborador colaboradorEncontrado = colaboradorRepository.findById(dto.getColaboradorId())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getColaboradorId()));
        OrdemDeServico os = new OrdemDeServico();
        os.setDescricao(dto.getDescricao());
        os.setCliente(clienteEncontrado);
        os.setColaborador(colaboradorEncontrado);
        os.setDataAbertura(LocalDate.now());
        os.setStatus(StatusEnum.ABERTA);
        OrdemDeServico osSalva = ordemDeServicoRepository.save(os);
        return new OrdemDeServicoResponseDTO(osSalva);
    }

    public OrdemDeServicoResponseDTO buscarPorId(Long id) {
        OrdemDeServico buscarOs = ordemDeServicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return new OrdemDeServicoResponseDTO(buscarOs);
    }

    public void deletarOsPorId(Long id) {
        OrdemDeServico osEncontrada = ordemDeServicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        if (osEncontrada.getStatus() != StatusEnum.ABERTA) {
            throw new DatabaseException("Ordem de serviço não está aberta!");
        }
        ordemDeServicoRepository.delete(osEncontrada);
    }

    public List<OrdemDeServicoResponseDTO> listarTodos() {
        List<OrdemDeServico> list = ordemDeServicoRepository.findAll();
        return list.stream().map(OrdemDeServicoResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public OrdemDeServicoResponseDTO atualizarDescricao(Long id, OrdemDeServicoRequestDTO dto) {
        OrdemDeServico osExistente = ordemDeServicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        if (osExistente.getStatus() != StatusEnum.ABERTA) {
            throw new DatabaseException("Ordem de serviço não está aberta!");
        }
        osExistente.setDescricao(dto.getDescricao());
        OrdemDeServico osSalva = ordemDeServicoRepository.save(osExistente);
        return new OrdemDeServicoResponseDTO(osSalva);
    }

    @Transactional
    public OrdemDeServicoResponseDTO finalizarOs(Long id) {
        OrdemDeServico osExistente = ordemDeServicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        if (osExistente.getStatus() != StatusEnum.ABERTA) {
            throw new DatabaseException("Ordem de serviço não está aberta!");
        }
        osExistente.setStatus(StatusEnum.FINALIZADA);
        osExistente.setDataFechamento(LocalDate.now());
        OrdemDeServico osSalva = ordemDeServicoRepository.save(osExistente);
        return new OrdemDeServicoResponseDTO(osSalva);
    }

    @Transactional
    public OrdemDeServicoResponseDTO cancelarOs(Long id) {
        OrdemDeServico osExistente = ordemDeServicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        if (osExistente.getStatus() != StatusEnum.ABERTA) {
            throw new DatabaseException("Ordem de serviço não está aberta!");
        }
        osExistente.setStatus(StatusEnum.CANCELADA);
        osExistente.setDataFechamento(LocalDate.now());
        OrdemDeServico osSalva = ordemDeServicoRepository.save(osExistente);
        return new OrdemDeServicoResponseDTO(osSalva);
    }
}
