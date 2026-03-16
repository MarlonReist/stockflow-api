package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.Almoxarifado;
import com.marlondev.stockflow.domain.TransferenciaAlmoxarifado;
import com.marlondev.stockflow.dto.TransferenciaAlmoxarifadoRequestDTO;
import com.marlondev.stockflow.dto.TransferenciaAlmoxarifadoResponseDTO;
import com.marlondev.stockflow.repositories.AlmoxarifadoRepository;
import com.marlondev.stockflow.repositories.TransferenciaAlmoxarifadoRepository;
import com.marlondev.stockflow.repositories.TransferenciaItemRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransferenciaAlmoxarifadoService {

    public final TransferenciaAlmoxarifadoRepository transferenciaAlmoxarifadoRepository;
    public final AlmoxarifadoRepository almoxarifadoRepository;
    public final TransferenciaItemRepository transferenciaRepository;

    public TransferenciaAlmoxarifadoService(TransferenciaAlmoxarifadoRepository transferenciaAlmoxarifadoRepository, AlmoxarifadoRepository almoxarifadoRepository, TransferenciaItemRepository transferenciaRepository) {
        this.transferenciaAlmoxarifadoRepository = transferenciaAlmoxarifadoRepository;
        this.almoxarifadoRepository = almoxarifadoRepository;
        this.transferenciaRepository = transferenciaRepository;
    }

    @Transactional
    public TransferenciaAlmoxarifadoResponseDTO salvar(TransferenciaAlmoxarifadoRequestDTO dto){
        Almoxarifado almoxarifadoOrigem = almoxarifadoRepository.findById(dto.getAlmoxarifadoOrigemId())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getAlmoxarifadoOrigemId()));

        Almoxarifado almoxarifadoDestino = almoxarifadoRepository.findById(dto.getAlmoxarifadoDestinoId())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getAlmoxarifadoDestinoId()));

        if (almoxarifadoOrigem.getId().equals(almoxarifadoDestino.getId())){
            throw new DatabaseException("Almoxarifado Origem e Destino não podem ser o mesmo!");
        }

        TransferenciaAlmoxarifado transfAlmox = new TransferenciaAlmoxarifado();
        transfAlmox.setDataTransferencia(LocalDate.now());
        transfAlmox.setAlmoxarifadoOrigem(almoxarifadoOrigem);
        transfAlmox.setAlmoxarifadoDestino(almoxarifadoDestino);
        transfAlmox.setObservacao(dto.getObservacao());
        TransferenciaAlmoxarifado transfSalva = transferenciaAlmoxarifadoRepository.save(transfAlmox);
        return new TransferenciaAlmoxarifadoResponseDTO(transfSalva);
    }

    public TransferenciaAlmoxarifadoResponseDTO buscarPorId(Long id){
        TransferenciaAlmoxarifado buscarTransf = transferenciaAlmoxarifadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return new TransferenciaAlmoxarifadoResponseDTO(buscarTransf);
    }

    @Transactional
    public void deletarPorId(Long id){
        TransferenciaAlmoxarifado buscarTransf = transferenciaAlmoxarifadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        if(transferenciaRepository.existsByTransferenciaId(id)){
            throw new DatabaseException("Não é possível excluir uma transferência com produtos!");
        }
        transferenciaAlmoxarifadoRepository.deleteById(id);
    }

    public List<TransferenciaAlmoxarifadoResponseDTO> listarTodos() {
        List<TransferenciaAlmoxarifado> list = transferenciaAlmoxarifadoRepository.findAll();
        return list.stream().map(TransferenciaAlmoxarifadoResponseDTO::new).collect(Collectors.toList());
    }
}
