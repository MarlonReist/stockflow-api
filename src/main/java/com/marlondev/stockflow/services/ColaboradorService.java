package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.Colaborador;
import com.marlondev.stockflow.dto.ColaboradorRequestDTO;
import com.marlondev.stockflow.dto.ColaboradorResponseDTO;
import com.marlondev.stockflow.repositories.ColaboradorRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ColaboradorService {

    private final ColaboradorRepository colaboradorRepository;


    public ColaboradorService(ColaboradorRepository colaboradorRepository) {
        this.colaboradorRepository = colaboradorRepository;
    }

    @Transactional
    public ColaboradorResponseDTO salvarColaborador(ColaboradorRequestDTO dto){
        if (colaboradorRepository.findByCpf(dto.getCpf()).isPresent()) {
            throw new DatabaseException("Esse CPF já existe!");
        }
        Colaborador colaborador = new Colaborador();
        colaborador.setNome(dto.getNome());
        colaborador.setCpf(dto.getCpf());
        colaborador.setCargo(dto.getCargo());
        colaborador.setTelefone(dto.getTelefone());
        Colaborador colaboradorSalvo = colaboradorRepository.save(colaborador);
        return new ColaboradorResponseDTO(colaboradorSalvo);
    }

    public ColaboradorResponseDTO buscarPorId(Long id){
        Colaborador colaboradorExiste = colaboradorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return new ColaboradorResponseDTO(colaboradorExiste);
    }

    public void deletarColaboradorPorId(Long id){
        buscarPorId(id);
        colaboradorRepository.deleteById(id);
    }

    public List<ColaboradorResponseDTO> listarTodos(){
        List<Colaborador> list = colaboradorRepository.findAll();
        List<ColaboradorResponseDTO> listDto = list.stream().map(ColaboradorResponseDTO::new).collect(Collectors.toList());
        return listDto;
    }

    @Transactional
    public ColaboradorResponseDTO atualizarColaborador(Long id, ColaboradorRequestDTO dto){
        Colaborador existente = colaboradorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        Colaborador outroColaborador = colaboradorRepository.findByCpf(dto.getCpf()).orElse(null);

        if (outroColaborador == null || outroColaborador.getId().equals(existente.getId())) {
            existente.setNome(dto.getNome());
            existente.setCpf(dto.getCpf());
            existente.setCargo(dto.getCargo());
            existente.setTelefone(dto.getTelefone());
            Colaborador colaboradorSalvo = colaboradorRepository.save(existente);
            return new ColaboradorResponseDTO(colaboradorSalvo);
        }
            throw new DatabaseException("Esse CPF já existe");
        }
}
