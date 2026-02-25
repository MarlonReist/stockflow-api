package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.Colaborador;
import com.marlondev.stockflow.repositories.ColaboradorRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColaboradorService {

    private final ColaboradorRepository colaboradorRepository;


    public ColaboradorService(ColaboradorRepository colaboradorRepository) {
        this.colaboradorRepository = colaboradorRepository;
    }

    public void salvarColaborador(Colaborador colaborador){
        if (colaboradorRepository.findByCpf(colaborador.getCpf()).isEmpty()) {
            colaboradorRepository.save(colaborador);
        }
        else {
            throw new DatabaseException("Esse colaborador já existe!");
        }
    }

    public Colaborador buscarPorId(Long id){
        return colaboradorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public void deletarColaboradorPorId(Long id){
        buscarPorId(id);
        colaboradorRepository.deleteById(id);
    }

    public List<Colaborador> listarTodos(){
        return colaboradorRepository.findAll();
    }

    public void atualizarColaborador(Colaborador colaborador){
        Colaborador existente = colaboradorRepository.findById(colaborador.getId())
                .orElseThrow(() -> new ResourceNotFoundException(colaborador.getId()));
        Colaborador outroColaborador = colaboradorRepository.findByCpf(colaborador.getCpf()).orElse(null);

        if (outroColaborador == null || outroColaborador.getId().equals(existente.getId())) {
            existente.setNome(colaborador.getNome());
            existente.setCpf(colaborador.getCpf());
            existente.setCargo(colaborador.getCargo());
            existente.setTelefone(colaborador.getTelefone());
            colaboradorRepository.save(existente);
        } else {
            throw new DatabaseException("Esse colaborador já existe");
        }
        }
    }
