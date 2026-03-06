package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.Almoxarifado;
import com.marlondev.stockflow.dto.AlmoxarifadoRequestDTO;
import com.marlondev.stockflow.repositories.AlmoxarifadoRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlmoxarifadoService {

    private final AlmoxarifadoRepository almoxarifadoRepository;

    public AlmoxarifadoService(AlmoxarifadoRepository almoxarifadoRepository) {
        this.almoxarifadoRepository = almoxarifadoRepository;
    }

    public void salvarAlmoxarifado(AlmoxarifadoRequestDTO dto) {
        Almoxarifado almoxarifado = new Almoxarifado();
        almoxarifado.setNome(dto.getNome());
        almoxarifadoRepository.save(almoxarifado);
    }

    public Almoxarifado buscarPorId(Long id) {
        return almoxarifadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public void deletarAlmoxarifadoPorId(Long id) {
        buscarPorId(id);
        almoxarifadoRepository.deleteById(id);
    }

    public List<Almoxarifado> listarTodos() {
        return almoxarifadoRepository.findAll();
    }

    /*public void atualizarAlmoxarifado(@Valid Almoxarifado almoxarifado) {
        Almoxarifado existente = almoxarifadoRepository.findById(almoxarifado.getId())
                .orElseThrow(() -> new ResourceNotFoundException(almoxarifado.getId()));
        Almoxarifado outroAlmoxarifado = almoxarifadoRepository.findByNome(almoxarifado.getNome()).orElse(null);

        if (outroAlmoxarifado == null || outroAlmoxarifado.getId().equals(existente.getId())) {
            existente.setNome(almoxarifado.getNome());
            existente.setPreco(almoxarifado.getPreco());
            almoxarifadoRepository.save(existente);
        } else {
            throw new DatabaseException("Esse almoxarifado já existe");
        }
    }
*/
}
