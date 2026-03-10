package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.Categoria;
import com.marlondev.stockflow.dto.CategoriaRequestDTO;
import com.marlondev.stockflow.dto.CategoriaResponseDTO;
import com.marlondev.stockflow.repositories.CategoriaRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public CategoriaResponseDTO salvar(CategoriaRequestDTO dto) {
        if (categoriaRepository.findByNome(dto.getNome()).isPresent()) {
            throw new DatabaseException("Essa Categoria já existe!");
        }
        Categoria categoria = new Categoria();
        categoria.setNome(dto.getNome());
        Categoria categoriaSalva = categoriaRepository.save(categoria);
        return new CategoriaResponseDTO(categoriaSalva);
    }

    public CategoriaResponseDTO buscarPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return new CategoriaResponseDTO(categoria);
    }

    public void deletarCategoriaPorId(Long id) {
        buscarPorId(id);
        categoriaRepository.deleteById(id);
    }

    public List<CategoriaResponseDTO> listarTodos() {
        List<Categoria> list = categoriaRepository.findAll();
        return list.stream().map(CategoriaResponseDTO::new).collect(Collectors.toList());
    }

    public CategoriaResponseDTO atualizarCategoria(Long id, CategoriaRequestDTO dto) {
        Categoria existente = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        Categoria outraCategoria = categoriaRepository.findByNome(dto.getNome()).orElse(null);
        if (outraCategoria == null || outraCategoria.getId().equals(existente.getId())) {
            existente.setNome(dto.getNome());
            Categoria categoriaSalva = categoriaRepository.save(existente);
            return new CategoriaResponseDTO(categoriaSalva);
        }
        throw new DatabaseException("Essa categoria já existe");
    }
}
