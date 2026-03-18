package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.*;
import com.marlondev.stockflow.dto.TransferenciaItemRequestDTO;
import com.marlondev.stockflow.dto.TransferenciaItemResponseDTO;
import com.marlondev.stockflow.repositories.*;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransferenciaItemService {

    public final TransferenciaItemRepository transferenciaRepository;
    public final TransferenciaAlmoxarifadoRepository transferenciaAlmoxarifadoRepository;
    public final AlmoxarifadoEstoqueRepository almoxarifadoEstoqueRepository;
    public final ProdutoRepository produtoRepository;
    private final MovimentacaoEstoqueService movimentacaoEstoqueService;

    public TransferenciaItemService(TransferenciaItemRepository transferenciaRepository, TransferenciaAlmoxarifadoRepository transferenciaAlmoxarifadoRepository, AlmoxarifadoEstoqueRepository almoxarifadoEstoqueRepository, ProdutoRepository produtoRepository, MovimentacaoEstoqueService movimentacaoEstoqueService) {
        this.transferenciaRepository = transferenciaRepository;
        this.transferenciaAlmoxarifadoRepository = transferenciaAlmoxarifadoRepository;
        this.almoxarifadoEstoqueRepository = almoxarifadoEstoqueRepository;
        this.produtoRepository = produtoRepository;
        this.movimentacaoEstoqueService = movimentacaoEstoqueService;
    }

    @Transactional
    public TransferenciaItemResponseDTO salvar(TransferenciaItemRequestDTO dto) {
        TransferenciaAlmoxarifado transferencia = transferenciaAlmoxarifadoRepository.findById(dto.getTransferenciaId())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getTransferenciaId()));

        Produto produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getProdutoId()));

        Almoxarifado almoxarifadoOrigem = transferencia.getAlmoxarifadoOrigem();
        Almoxarifado almoxarifadoDestino = transferencia.getAlmoxarifadoDestino();

        Optional<AlmoxarifadoEstoque> estoqueAlmoxarifadoOrigem = almoxarifadoEstoqueRepository.findByAlmoxarifadoIdAndProdutoId(almoxarifadoOrigem.getId(), produto.getId());
        if (estoqueAlmoxarifadoOrigem.isPresent()) {
            AlmoxarifadoEstoque estoqueExistente = estoqueAlmoxarifadoOrigem.get();

            if (dto.getQuantidade() > estoqueExistente.getQuantidade()) {
                throw new DatabaseException("Quantidade insuficiente em estoque!");
            }

            int novaQuantidade = estoqueExistente.getQuantidade() - dto.getQuantidade();
            estoqueExistente.setQuantidade(novaQuantidade);
            almoxarifadoEstoqueRepository.save(estoqueExistente);
        } else {
            throw new DatabaseException("Não existe estoque no almoxarifado de origem!");
        }

        Optional<AlmoxarifadoEstoque> estoqueAlmoxarifadoDestino = almoxarifadoEstoqueRepository.findByAlmoxarifadoIdAndProdutoId(almoxarifadoDestino.getId(), produto.getId());
        if (estoqueAlmoxarifadoDestino.isPresent()) {
            AlmoxarifadoEstoque estoqueExistente = estoqueAlmoxarifadoDestino.get();

            int novaQuantidade = estoqueExistente.getQuantidade() + dto.getQuantidade();
            estoqueExistente.setQuantidade(novaQuantidade);
            almoxarifadoEstoqueRepository.save(estoqueExistente);
        } else {
            AlmoxarifadoEstoque novoEstoque = new AlmoxarifadoEstoque();
            novoEstoque.setAlmoxarifado(almoxarifadoDestino);
            novoEstoque.setProduto(produto);
            novoEstoque.setQuantidade(dto.getQuantidade());
            almoxarifadoEstoqueRepository.save(novoEstoque);
        }

        TransferenciaItem transferenciaItem = new TransferenciaItem();
        transferenciaItem.setTransferencia(transferencia);
        transferenciaItem.setProduto(produto);
        transferenciaItem.setQuantidade(dto.getQuantidade());
        TransferenciaItem transferenciaSalva = transferenciaRepository.save(transferenciaItem);
        movimentacaoEstoqueService.registrarSaidaTransferencia(transferenciaSalva);
        movimentacaoEstoqueService.registrarEntradaTransferencia(transferenciaSalva);
        return new TransferenciaItemResponseDTO(transferenciaSalva);
    }

    public TransferenciaItemResponseDTO buscarPorId(Long id){
        TransferenciaItem transferenciaItemExiste = transferenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        return new TransferenciaItemResponseDTO(transferenciaItemExiste);
    }

    public List<TransferenciaItemResponseDTO> listarTodos(){
        List<TransferenciaItem> list = transferenciaRepository.findAll();
        return list.stream().map(TransferenciaItemResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public void deletarPorId(Long id) {
        TransferenciaItem transferenciaItemExiste = transferenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        Almoxarifado almoxarifadoOrigem = transferenciaItemExiste.getTransferencia().getAlmoxarifadoOrigem();
        Almoxarifado almoxarifadoDestino = transferenciaItemExiste.getTransferencia().getAlmoxarifadoDestino();

        Produto produto = transferenciaItemExiste.getProduto();
        Integer quantidade = transferenciaItemExiste.getQuantidade();

        Optional<AlmoxarifadoEstoque> estoqueAlmoxarifadoOrigem = almoxarifadoEstoqueRepository.findByAlmoxarifadoIdAndProdutoId(almoxarifadoOrigem.getId(), produto.getId());
        if (estoqueAlmoxarifadoOrigem.isPresent()) {
            AlmoxarifadoEstoque estoqueExistente = estoqueAlmoxarifadoOrigem.get();

            int novaQuantidade = estoqueExistente.getQuantidade() + quantidade;
            estoqueExistente.setQuantidade(novaQuantidade);
            almoxarifadoEstoqueRepository.save(estoqueExistente);
        } else {
            throw new DatabaseException("O estoque da origem não existe!");
        }

        Optional<AlmoxarifadoEstoque> estoqueAlmoxarifadoDestino = almoxarifadoEstoqueRepository.findByAlmoxarifadoIdAndProdutoId(almoxarifadoDestino.getId(), produto.getId());
        if (estoqueAlmoxarifadoDestino.isPresent()) {
            AlmoxarifadoEstoque estoqueExistente = estoqueAlmoxarifadoDestino.get();

            if (estoqueExistente.getQuantidade() < quantidade) {
                throw new DatabaseException("A quantidade em estoque, não pode ser negativa!");
            }

            int novaQuantidade = estoqueExistente.getQuantidade() - quantidade;

            estoqueExistente.setQuantidade(novaQuantidade);
            almoxarifadoEstoqueRepository.save(estoqueExistente);
        } else {
            throw new DatabaseException("O estoque de destino não existe!");
        }
        transferenciaRepository.deleteById(id);
    }

        @Transactional
        public TransferenciaItemResponseDTO atualizar(Long id, TransferenciaItemRequestDTO dto){
            TransferenciaItem transferenciaItemExiste = transferenciaRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(id));

            if (!dto.getTransferenciaId().equals(transferenciaItemExiste.getTransferencia().getId())){
                throw new DatabaseException("Não é permitido trocar a transferência!");
            }

            Produto produto = produtoRepository.findById(dto.getProdutoId())
                    .orElseThrow(() -> new ResourceNotFoundException(dto.getProdutoId()));

            Almoxarifado almoxarifadoOrigem = transferenciaItemExiste.getTransferencia().getAlmoxarifadoOrigem();
            Almoxarifado almoxarifadoDestino = transferenciaItemExiste.getTransferencia().getAlmoxarifadoDestino();

            Produto produtoAntigo = transferenciaItemExiste.getProduto();
            Integer quantidadeAntiga = transferenciaItemExiste.getQuantidade();

            Optional<AlmoxarifadoEstoque> estoqueAlmoxarifadoOrigem = almoxarifadoEstoqueRepository.findByAlmoxarifadoIdAndProdutoId(almoxarifadoOrigem.getId(), produtoAntigo.getId());
            if (estoqueAlmoxarifadoOrigem.isPresent()) {
                AlmoxarifadoEstoque estoqueExistente = estoqueAlmoxarifadoOrigem.get();

                int novaQuantidade = estoqueExistente.getQuantidade() + quantidadeAntiga;
                estoqueExistente.setQuantidade(novaQuantidade);
                almoxarifadoEstoqueRepository.save(estoqueExistente);
            } else {
                throw new DatabaseException("O estoque da origem não existe!");
            }

            Optional<AlmoxarifadoEstoque> estoqueAlmoxarifadoDestino = almoxarifadoEstoqueRepository.findByAlmoxarifadoIdAndProdutoId(almoxarifadoDestino.getId(), produtoAntigo.getId());
            if (estoqueAlmoxarifadoDestino.isPresent()) {
                AlmoxarifadoEstoque estoqueExistente = estoqueAlmoxarifadoDestino.get();

                if (estoqueExistente.getQuantidade() < quantidadeAntiga) {
                    throw new DatabaseException("A quantidade em estoque, não pode ser negativa!");
                }

                int novaQuantidade = estoqueExistente.getQuantidade() - quantidadeAntiga;

                estoqueExistente.setQuantidade(novaQuantidade);
                almoxarifadoEstoqueRepository.save(estoqueExistente);
            } else {
                throw new DatabaseException("O estoque de destino não existe!");
            }
            Optional<AlmoxarifadoEstoque> estoqueAlmoxarifadoOrigemNovo = almoxarifadoEstoqueRepository.findByAlmoxarifadoIdAndProdutoId(almoxarifadoOrigem.getId(), produto.getId());
            if (estoqueAlmoxarifadoOrigemNovo.isPresent()) {
                AlmoxarifadoEstoque estoqueExistente = estoqueAlmoxarifadoOrigemNovo.get();

                if (dto.getQuantidade() > estoqueExistente.getQuantidade()) {
                    throw new DatabaseException("Quantidade insuficiente em estoque!");
                }

                int novaQuantidade = estoqueExistente.getQuantidade() - dto.getQuantidade();
                estoqueExistente.setQuantidade(novaQuantidade);
                almoxarifadoEstoqueRepository.save(estoqueExistente);
            } else {
                throw new DatabaseException("Não existe estoque no almoxarifado de origem!");
            }

            Optional<AlmoxarifadoEstoque> estoqueAlmoxarifadoDestinoNovo = almoxarifadoEstoqueRepository.findByAlmoxarifadoIdAndProdutoId(almoxarifadoDestino.getId(), produto.getId());
            if (estoqueAlmoxarifadoDestinoNovo.isPresent()) {
                AlmoxarifadoEstoque estoqueExistente = estoqueAlmoxarifadoDestinoNovo.get();

                int novaQuantidade = estoqueExistente.getQuantidade() + dto.getQuantidade();
                estoqueExistente.setQuantidade(novaQuantidade);
                almoxarifadoEstoqueRepository.save(estoqueExistente);
            } else {
                AlmoxarifadoEstoque novoEstoque = new AlmoxarifadoEstoque();
                novoEstoque.setAlmoxarifado(almoxarifadoDestino);
                novoEstoque.setProduto(produto);
                novoEstoque.setQuantidade(dto.getQuantidade());
                almoxarifadoEstoqueRepository.save(novoEstoque);
            }

            transferenciaItemExiste.setProduto(produto);
            transferenciaItemExiste.setQuantidade(dto.getQuantidade());
            TransferenciaItem transferenciaSalva = transferenciaRepository.save(transferenciaItemExiste);
            return new TransferenciaItemResponseDTO(transferenciaSalva);
        }
}
