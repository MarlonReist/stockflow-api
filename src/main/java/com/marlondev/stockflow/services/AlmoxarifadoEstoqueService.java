package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.Almoxarifado;
import com.marlondev.stockflow.domain.AlmoxarifadoEstoque;
import com.marlondev.stockflow.domain.Produto;
import com.marlondev.stockflow.dto.AlmoxarifadoEstoqueRequestDTO;
import com.marlondev.stockflow.dto.AlmoxarifadoEstoqueResponseDTO;
import com.marlondev.stockflow.repositories.AlmoxarifadoEstoqueRepository;
import com.marlondev.stockflow.repositories.AlmoxarifadoRepository;
import com.marlondev.stockflow.repositories.ProdutoRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlmoxarifadoEstoqueService {

    private final AlmoxarifadoEstoqueRepository almoxarifadoEstoqueRepository;
    private final AlmoxarifadoRepository almoxarifadoRepository;
    private final ProdutoRepository produtoRepository;

    public AlmoxarifadoEstoqueService(AlmoxarifadoEstoqueRepository almoxarifadoEstoqueRepository, AlmoxarifadoRepository almoxarifadoRepository, ProdutoRepository produtoRepository) {
        this.almoxarifadoEstoqueRepository = almoxarifadoEstoqueRepository;
        this.almoxarifadoRepository = almoxarifadoRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public AlmoxarifadoEstoqueResponseDTO salvar(AlmoxarifadoEstoqueRequestDTO dto){
        Almoxarifado almoxarifadoEncontrado = almoxarifadoRepository.findById(dto.getAlmoxarifadoId())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getAlmoxarifadoId()));

        Produto produtoEncontrado = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getProdutoId()));

        boolean jaExiste = almoxarifadoEstoqueRepository.existsByAlmoxarifadoIdAndProdutoId( almoxarifadoEncontrado.getId(), produtoEncontrado.getId());

        if (jaExiste){
            throw new DatabaseException("O produto já existe nesse almoxarifado!");
        }

        AlmoxarifadoEstoque novoEstoque = new AlmoxarifadoEstoque();
        novoEstoque.setAlmoxarifado(almoxarifadoEncontrado);
        novoEstoque.setProduto(produtoEncontrado);
        novoEstoque.setQuantidade(dto.getQuantidade());

        AlmoxarifadoEstoque almoxarifadoSalvo = almoxarifadoEstoqueRepository.save(novoEstoque);
        return new AlmoxarifadoEstoqueResponseDTO(almoxarifadoSalvo);
    }

    public AlmoxarifadoEstoqueResponseDTO buscarPorId(Long id) {
        AlmoxarifadoEstoque estoque = almoxarifadoEstoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return new AlmoxarifadoEstoqueResponseDTO(estoque);
    }

    public void deletarEstoque(Long id){
        buscarPorId(id);
        almoxarifadoEstoqueRepository.deleteById(id);
    }

    public List<AlmoxarifadoEstoqueResponseDTO> listarTodos(){
        List<AlmoxarifadoEstoque> list = almoxarifadoEstoqueRepository.findAll();
        List<AlmoxarifadoEstoqueResponseDTO> listDto = list.stream().map(AlmoxarifadoEstoqueResponseDTO::new).collect(Collectors.toList());
        return listDto;
    }

    public AlmoxarifadoEstoqueResponseDTO atualizarEstoque(Long id, AlmoxarifadoEstoqueRequestDTO dto){
        AlmoxarifadoEstoque existente = almoxarifadoEstoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        existente.setQuantidade(dto.getQuantidade());
        AlmoxarifadoEstoque estoqueSalvo = almoxarifadoEstoqueRepository.save(existente);
        return new AlmoxarifadoEstoqueResponseDTO(estoqueSalvo);
    }
}
