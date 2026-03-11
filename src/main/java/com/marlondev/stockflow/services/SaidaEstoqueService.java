package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.Almoxarifado;
import com.marlondev.stockflow.domain.SaidaEstoque;
import com.marlondev.stockflow.domain.enums.StatusEnum;
import com.marlondev.stockflow.dto.SaidaEstoqueRequestDTO;
import com.marlondev.stockflow.dto.SaidaEstoqueResponseDTO;
import com.marlondev.stockflow.repositories.AlmoxarifadoRepository;
import com.marlondev.stockflow.repositories.SaidaEstoqueRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaidaEstoqueService {

    private final SaidaEstoqueRepository saidaEstoqueRepository;
    private final AlmoxarifadoRepository almoxarifadoRepository;

    public SaidaEstoqueService(SaidaEstoqueRepository saidaEstoqueRepository, AlmoxarifadoRepository almoxarifadoRepository) {
        this.saidaEstoqueRepository = saidaEstoqueRepository;
        this.almoxarifadoRepository = almoxarifadoRepository;
    }

    @Transactional
    public SaidaEstoqueResponseDTO salvar (SaidaEstoqueRequestDTO dto){
        Almoxarifado almoxarifadoEncontrado = almoxarifadoRepository.findById(dto.getAlmoxarifadoId())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getAlmoxarifadoId()));

        SaidaEstoque saidaEstoque = new SaidaEstoque();
        saidaEstoque.setDataSaida(LocalDate.now());
        saidaEstoque.setAlmoxarifado(almoxarifadoEncontrado);
        saidaEstoque.setStatus(StatusEnum.ABERTA);
        SaidaEstoque saidaEstoqueSalva = saidaEstoqueRepository.save(saidaEstoque);
        return new SaidaEstoqueResponseDTO(saidaEstoqueSalva);
    }

    public SaidaEstoqueResponseDTO buscarPorId(Long id){
        SaidaEstoque saida = saidaEstoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return new SaidaEstoqueResponseDTO(saida);
    }

    @Transactional
    public void deletarPorId(Long id){
        SaidaEstoque saidaEstoqueEncontrada = saidaEstoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        if(saidaEstoqueEncontrada.getStatus() != StatusEnum.ABERTA){
            throw new DatabaseException("Saída não está aberta!");
        }
        saidaEstoqueRepository.deleteById(id);
    }

    public List<SaidaEstoqueResponseDTO> listarTodos() {
        List<SaidaEstoque> list = saidaEstoqueRepository.findAll();
        return list.stream().map(SaidaEstoqueResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public SaidaEstoqueResponseDTO finalizarSaida(Long id) {
        SaidaEstoque saidaExistente = saidaEstoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        if (saidaExistente.getStatus() != StatusEnum.ABERTA) {
            throw new DatabaseException("Saída não está aberta!");
        }
        saidaExistente.setStatus(StatusEnum.FINALIZADA);
        SaidaEstoque saidaSalva = saidaEstoqueRepository.save(saidaExistente);
        return new SaidaEstoqueResponseDTO(saidaSalva);
    }

    @Transactional
    public SaidaEstoqueResponseDTO cancelarSaida(Long id) {
        SaidaEstoque saidaExistente = saidaEstoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        if (saidaExistente.getStatus() != StatusEnum.ABERTA) {
            throw new DatabaseException("Saída não está aberta!");
        }
        saidaExistente.setStatus(StatusEnum.CANCELADA);
        SaidaEstoque saidaSalva = saidaEstoqueRepository.save(saidaExistente);
        return new SaidaEstoqueResponseDTO(saidaSalva);
    }
}
