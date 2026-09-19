package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.TipoOrdemServico;
import com.marlondev.stockflow.dto.TipoOrdemServicoRequestDTO;
import com.marlondev.stockflow.dto.TipoOrdemServicoResponseDTO;
import com.marlondev.stockflow.repositories.TipoOrdemServicoRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TipoOrdemServicoService {

    private final TipoOrdemServicoRepository tipoOrdemServicoRepository;

    public TipoOrdemServicoService(TipoOrdemServicoRepository tipoOrdemServicoRepository) {
        this.tipoOrdemServicoRepository = tipoOrdemServicoRepository;
    }

    @Transactional
    public TipoOrdemServicoResponseDTO salvar(TipoOrdemServicoRequestDTO dto) {
        String nomeNormalizado = normalizarNome(dto.getNome());

        if (tipoOrdemServicoRepository.findByNomeIgnoreCase(nomeNormalizado).isPresent()) {
            throw new DatabaseException("Esse tipo de ordem de serviço já existe!");
        }

        TipoOrdemServico tipo = new TipoOrdemServico();
        tipo.setNome(nomeNormalizado);
        tipo.setAtivo(true);

        TipoOrdemServico tipoSalvo = tipoOrdemServicoRepository.save(tipo);
        return new TipoOrdemServicoResponseDTO(tipoSalvo);
    }

    public TipoOrdemServicoResponseDTO buscarPorId(Long id) {
        TipoOrdemServico tipo = tipoOrdemServicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        return new TipoOrdemServicoResponseDTO(tipo);
    }

    public List<TipoOrdemServicoResponseDTO> listarTodos() {
        List<TipoOrdemServico> list = tipoOrdemServicoRepository.findAll();

        return list.stream()
                .map(TipoOrdemServicoResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<TipoOrdemServicoResponseDTO> listarAtivos() {
        List<TipoOrdemServico> list = tipoOrdemServicoRepository.findByAtivoTrueOrderByNomeAsc();

        return list.stream()
                .map(TipoOrdemServicoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public TipoOrdemServicoResponseDTO atualizar(Long id, TipoOrdemServicoRequestDTO dto) {
        TipoOrdemServico existente = tipoOrdemServicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        String nomeNormalizado = normalizarNome(dto.getNome());

        TipoOrdemServico outroTipo = tipoOrdemServicoRepository.findByNomeIgnoreCase(nomeNormalizado)
                .orElse(null);

        if (outroTipo != null && !outroTipo.getId().equals(existente.getId())) {
            throw new DatabaseException("Esse tipo de ordem de serviço já existe!");
        }

        existente.setNome(nomeNormalizado);

        TipoOrdemServico tipoSalvo = tipoOrdemServicoRepository.save(existente);
        return new TipoOrdemServicoResponseDTO(tipoSalvo);
    }

    @Transactional
    public TipoOrdemServicoResponseDTO ativar(Long id) {
        TipoOrdemServico tipo = tipoOrdemServicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        tipo.setAtivo(true);

        TipoOrdemServico tipoSalvo = tipoOrdemServicoRepository.save(tipo);
        return new TipoOrdemServicoResponseDTO(tipoSalvo);
    }

    @Transactional
    public TipoOrdemServicoResponseDTO desativar(Long id) {
        TipoOrdemServico tipo = tipoOrdemServicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        tipo.setAtivo(false);

        TipoOrdemServico tipoSalvo = tipoOrdemServicoRepository.save(tipo);
        return new TipoOrdemServicoResponseDTO(tipoSalvo);
    }

    private String normalizarNome(String nome) {
        return nome.trim();
    }

}