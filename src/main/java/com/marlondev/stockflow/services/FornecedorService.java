package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.Fornecedor;
import com.marlondev.stockflow.dto.FornecedorRequestDTO;
import com.marlondev.stockflow.dto.FornecedorResponseDTO;
import com.marlondev.stockflow.repositories.FornecedorRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;

    public FornecedorService(FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    @Transactional
    public FornecedorResponseDTO salvarFornecedor(FornecedorRequestDTO dto){
        if (fornecedorRepository.findByCnpj(dto.getCnpj()).isPresent()) {
            throw new DatabaseException("Esse CNPJ já existe!");
        }
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome(dto.getNome());
        fornecedor.setCnpj(dto.getCnpj());
        Fornecedor fornecedorSalvo = fornecedorRepository.save(fornecedor);
        return new FornecedorResponseDTO(fornecedorSalvo);
    }

    public FornecedorResponseDTO buscarPorId(Long id){
        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return new FornecedorResponseDTO(fornecedor);
    }

    public void deletarFornecedorPorId(Long id){
        buscarPorId(id);
        fornecedorRepository.deleteById(id);
    }

    public List<FornecedorResponseDTO> listarTodos(){
        List<Fornecedor> list = fornecedorRepository.findAll();
        return list.stream().map(FornecedorResponseDTO::new).collect(Collectors.toList());
    }

    public FornecedorResponseDTO atualizarFornecedor(Long id, FornecedorRequestDTO dto){
        Fornecedor existente = fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        Fornecedor outroFornecedor = fornecedorRepository.findByCnpj(dto.getCnpj()).orElse(null);

        if (outroFornecedor == null || outroFornecedor.getId().equals(existente.getId())) {
            existente.setNome(dto.getNome());
            existente.setCnpj(dto.getCnpj());

            Fornecedor fornecedorSalvo = fornecedorRepository.save(existente);
            return new FornecedorResponseDTO(fornecedorSalvo);
        }
        throw new DatabaseException("Esse CNPJ já existe!");
    }
}
