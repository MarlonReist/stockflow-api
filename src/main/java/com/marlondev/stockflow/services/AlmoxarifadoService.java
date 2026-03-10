package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.Almoxarifado;
import com.marlondev.stockflow.dto.AlmoxarifadoRequestDTO;
import com.marlondev.stockflow.dto.AlmoxarifadoResponseDTO;
import com.marlondev.stockflow.repositories.AlmoxarifadoRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlmoxarifadoService {

    private final AlmoxarifadoRepository almoxarifadoRepository;

    public AlmoxarifadoService(AlmoxarifadoRepository almoxarifadoRepository) {
        this.almoxarifadoRepository = almoxarifadoRepository;
    }

    @Transactional
    public AlmoxarifadoResponseDTO salvar(AlmoxarifadoRequestDTO dto) {
        if (almoxarifadoRepository.findByNome(dto.getNome()).isPresent()) {
            throw new DatabaseException("Esse Almoxarifado já existe!");
        }
        Almoxarifado almoxarifado = new Almoxarifado();
        almoxarifado.setNome(dto.getNome());
        Almoxarifado almoxarifadoSalvo = almoxarifadoRepository.save(almoxarifado);
        return new AlmoxarifadoResponseDTO(almoxarifadoSalvo);
    }

    public AlmoxarifadoResponseDTO buscarPorId(Long id) {
        Almoxarifado almoxarifado = almoxarifadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return new AlmoxarifadoResponseDTO(almoxarifado);
    }

    public void deletarAlmoxarifadoPorId(Long id) {
        buscarPorId(id);
        almoxarifadoRepository.deleteById(id);
    }

    public List<AlmoxarifadoResponseDTO> listarTodos() {
        List<Almoxarifado> list = almoxarifadoRepository.findAll();
        return list.stream().map(AlmoxarifadoResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public AlmoxarifadoResponseDTO atualizarAlmoxarifado(Long id, AlmoxarifadoRequestDTO dto) {
        Almoxarifado existente = almoxarifadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        Almoxarifado outroAlmoxarifado = almoxarifadoRepository.findByNome(dto.getNome()).orElse(null);

        if (outroAlmoxarifado == null || outroAlmoxarifado.getId().equals(existente.getId())) {
            existente.setNome(dto.getNome());
            Almoxarifado almoxarifadoSalvo = almoxarifadoRepository.save(existente);
            return new AlmoxarifadoResponseDTO(almoxarifadoSalvo);
        }
            throw new DatabaseException("Esse almoxarifado já existe!");
        }
}
