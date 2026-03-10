package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.Almoxarifado;
import com.marlondev.stockflow.domain.EntradaEstoque;
import com.marlondev.stockflow.domain.Fornecedor;
import com.marlondev.stockflow.domain.enums.StatusEnum;
import com.marlondev.stockflow.dto.EntradaEstoqueRequestDTO;
import com.marlondev.stockflow.dto.EntradaEstoqueResponseDTO;
import com.marlondev.stockflow.repositories.AlmoxarifadoRepository;
import com.marlondev.stockflow.repositories.EntradaEstoqueRepository;
import com.marlondev.stockflow.repositories.FornecedorRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EntradaEstoqueService {

    private final EntradaEstoqueRepository entradaEstoqueRepository;
    private final FornecedorRepository fornecedorRepository;
    private final AlmoxarifadoRepository almoxarifadoRepository;

    public EntradaEstoqueService(EntradaEstoqueRepository entradaEstoqueRepository, FornecedorRepository fornecedorRepository, AlmoxarifadoRepository almoxarifadoRepository) {
        this.entradaEstoqueRepository = entradaEstoqueRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.almoxarifadoRepository = almoxarifadoRepository;
    }

    @Transactional
    public EntradaEstoqueResponseDTO salvar (EntradaEstoqueRequestDTO dto){
        Fornecedor fornecedorEncontrado = fornecedorRepository.findById(dto.getFornecedorId())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getFornecedorId()));

        Almoxarifado almoxarifadoEncontrado = almoxarifadoRepository.findById(dto.getAlmoxarifadoId())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getAlmoxarifadoId()));

        EntradaEstoque entradaEstoque = new EntradaEstoque();
        entradaEstoque.setDataEntrada(LocalDate.now());
        entradaEstoque.setFornecedor(fornecedorEncontrado);
        entradaEstoque.setAlmoxarifado(almoxarifadoEncontrado);
        entradaEstoque.setStatus(StatusEnum.ABERTA);
        EntradaEstoque entradaEstoqueSalva = entradaEstoqueRepository.save(entradaEstoque);
        return new EntradaEstoqueResponseDTO(entradaEstoqueSalva);
    }

    public EntradaEstoqueResponseDTO buscarPorId(Long id){
        EntradaEstoque buscarEntradaEstoque = entradaEstoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return new EntradaEstoqueResponseDTO(buscarEntradaEstoque);
    }

    @Transactional
    public void deletarPorId(Long id){
        EntradaEstoque entradaEstoqueEncontrada = entradaEstoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        if(entradaEstoqueEncontrada.getStatus() != StatusEnum.ABERTA){
            throw new DatabaseException("Entrada não está aberta!");
        }
        entradaEstoqueRepository.deleteById(id);
    }

    public List<EntradaEstoqueResponseDTO> listarTodos() {
        List<EntradaEstoque> list = entradaEstoqueRepository.findAll();
        return list.stream().map(EntradaEstoqueResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public EntradaEstoqueResponseDTO finalizarEntrada(Long id) {
        EntradaEstoque entradaExistente = entradaEstoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        if (entradaExistente.getStatus() != StatusEnum.ABERTA) {
            throw new DatabaseException("Entrada não está aberta!");
        }
        entradaExistente.setStatus(StatusEnum.FINALIZADA);
        EntradaEstoque entradaSalva = entradaEstoqueRepository.save(entradaExistente);
        return new EntradaEstoqueResponseDTO(entradaSalva);
    }

    @Transactional
    public EntradaEstoqueResponseDTO cancelarEntrada(Long id) {
        EntradaEstoque entradaExistente = entradaEstoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        if (entradaExistente.getStatus() != StatusEnum.ABERTA) {
            throw new DatabaseException("Entrada não está aberta!");
        }
        entradaExistente.setStatus(StatusEnum.CANCELADA);
        EntradaEstoque entradaSalva = entradaEstoqueRepository.save(entradaExistente);
        return new EntradaEstoqueResponseDTO(entradaSalva);
    }
}
