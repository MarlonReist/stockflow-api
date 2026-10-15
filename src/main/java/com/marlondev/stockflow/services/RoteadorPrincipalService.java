package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.Cliente;
import com.marlondev.stockflow.domain.OrdemDeServico;
import com.marlondev.stockflow.domain.RoteadorPrincipalHistorico;
import com.marlondev.stockflow.domain.Usuario;
import com.marlondev.stockflow.dto.ModeloRoteadorSugestaoResponseDTO;
import com.marlondev.stockflow.dto.RoteadorPrincipalHistoricoResponseDTO;
import com.marlondev.stockflow.dto.RoteadorPrincipalRequestDTO;
import com.marlondev.stockflow.repositories.ModeloRoteadorSugestaoRepository;
import com.marlondev.stockflow.repositories.RoteadorPrincipalHistoricoRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
public class RoteadorPrincipalService {
    private static final int LIMITE_MAXIMO_SUGESTOES = 20;
    private final RoteadorPrincipalHistoricoRepository historicoRepository;
    private final ModeloRoteadorSugestaoRepository sugestaoRepository;

    public RoteadorPrincipalService(RoteadorPrincipalHistoricoRepository historicoRepository, ModeloRoteadorSugestaoRepository sugestaoRepository) {
        this.historicoRepository = historicoRepository;
        this.sugestaoRepository = sugestaoRepository;
    }

    @Transactional
    public boolean atualizar(Cliente cliente, RoteadorPrincipalRequestDTO dto, Usuario usuario, OrdemDeServico ordemDeServico) {
        String modeloNovo = limparModelo(dto.getModelo());
        String modeloAtual = cliente.getModeloRoteadorPrincipal();
        boolean mesmoModelo = modeloAtual != null && normalizarModelo(modeloAtual).equals(normalizarModelo(modeloNovo));
        boolean mesmaPropriedade = cliente.getPropriedadeRoteadorPrincipal() == dto.getPropriedade();
        if (mesmoModelo && mesmaPropriedade) return false;

        LocalDateTime agora = LocalDateTime.now();
        String modeloPersistido = mesmoModelo ? modeloAtual : modeloNovo;
        RoteadorPrincipalHistorico historico = new RoteadorPrincipalHistorico();
        historico.setCliente(cliente);
        historico.setModeloAnterior(modeloAtual);
        historico.setPropriedadeAnterior(cliente.getPropriedadeRoteadorPrincipal());
        historico.setModeloNovo(modeloPersistido);
        historico.setPropriedadeNova(dto.getPropriedade());
        historico.setDataAlteracao(agora);
        historico.setUsuario(usuario);
        historico.setOrdemDeServico(ordemDeServico);

        cliente.setModeloRoteadorPrincipal(modeloPersistido);
        cliente.setPropriedadeRoteadorPrincipal(dto.getPropriedade());
        cliente.setDataAtualizacaoRoteador(agora);
        cliente.setUsuarioAtualizacaoRoteador(usuario);
        historicoRepository.save(historico);
        if (!mesmoModelo) {
            sugestaoRepository.registrarUso(modeloPersistido, normalizarModelo(modeloPersistido), agora);
        }
        return true;
    }

    public List<ModeloRoteadorSugestaoResponseDTO> buscarSugestoes(String consulta, Integer limite) {
        if (consulta == null || consulta.isBlank()) return List.of();
        if (limite == null || limite <= 0 || limite > LIMITE_MAXIMO_SUGESTOES) {
            throw new DatabaseException("Limite de sugestões deve estar entre 1 e " + LIMITE_MAXIMO_SUGESTOES + "!");
        }
        return sugestaoRepository.findByModeloNormalizadoContainingOrderByFrequenciaDescModeloAsc(
                        normalizarModelo(consulta), PageRequest.of(0, limite))
                .stream().map(ModeloRoteadorSugestaoResponseDTO::new).toList();
    }

    public List<RoteadorPrincipalHistoricoResponseDTO> listarHistorico(Long clienteId) {
        return historicoRepository.findByClienteIdOrderByDataAlteracaoDescIdDesc(clienteId)
                .stream().map(RoteadorPrincipalHistoricoResponseDTO::new).toList();
    }

    public boolean possuiRoteadorPrincipal(Cliente cliente) {
        return cliente.getModeloRoteadorPrincipal() != null && !cliente.getModeloRoteadorPrincipal().isBlank()
                && cliente.getPropriedadeRoteadorPrincipal() != null;
    }

    private String limparModelo(String modelo) { return modelo.trim().replaceAll("\\s+", " "); }
    private String normalizarModelo(String modelo) {
        return Normalizer.normalize(limparModelo(modelo), Normalizer.Form.NFKC).toLowerCase(Locale.ROOT);
    }
}
