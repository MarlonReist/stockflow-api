package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.Cliente;
import com.marlondev.stockflow.domain.ModeloRoteadorSugestao;
import com.marlondev.stockflow.domain.OrdemDeServico;
import com.marlondev.stockflow.domain.Usuario;
import com.marlondev.stockflow.domain.enums.PropriedadeRoteadorEnum;
import com.marlondev.stockflow.dto.ModeloRoteadorSugestaoResponseDTO;
import com.marlondev.stockflow.dto.RoteadorPrincipalRequestDTO;
import com.marlondev.stockflow.repositories.ModeloRoteadorSugestaoRepository;
import com.marlondev.stockflow.repositories.RoteadorPrincipalHistoricoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoteadorPrincipalServiceTest {
    @Mock private RoteadorPrincipalHistoricoRepository historicoRepository;
    @Mock private ModeloRoteadorSugestaoRepository sugestaoRepository;
    private RoteadorPrincipalService service;

    @BeforeEach
    void setUp() {
        service = new RoteadorPrincipalService(historicoRepository, sugestaoRepository);
    }

    @Test
    void deveAprenderModeloLivreERegistrarHistoricoSemEstoque() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        Usuario usuario = new Usuario();
        usuario.setId(4L);
        OrdemDeServico os = new OrdemDeServico();
        os.setId(9L);

        boolean alterou = service.atualizar(cliente, requisicao("  Huawei   AX3 ", PropriedadeRoteadorEnum.PROPRIO), usuario, os);

        assertTrue(alterou);
        assertEquals("Huawei AX3", cliente.getModeloRoteadorPrincipal());
        assertEquals(PropriedadeRoteadorEnum.PROPRIO, cliente.getPropriedadeRoteadorPrincipal());
        verify(sugestaoRepository).registrarUso(eq("Huawei AX3"), eq("huawei ax3"), any());
        verify(historicoRepository).save(any());
    }

    @Test
    void naoDeveDuplicarHistoricoOuFrequenciaParaMesmoModeloNormalizado() {
        Cliente cliente = new Cliente();
        cliente.setModeloRoteadorPrincipal("TP-Link Archer C6");
        cliente.setPropriedadeRoteadorPrincipal(PropriedadeRoteadorEnum.COMODATO);

        boolean alterou = service.atualizar(cliente, requisicao(" tp-link   archer c6 ", PropriedadeRoteadorEnum.COMODATO), new Usuario(), null);

        assertFalse(alterou);
        verify(historicoRepository, never()).save(any());
        verify(sugestaoRepository, never()).registrarUso(any(), any(), any());
    }

    @Test
    void devePreservarModeloNoDicionarioQuandoApenasPropriedadeMuda() {
        Cliente cliente = new Cliente();
        cliente.setModeloRoteadorPrincipal("Intelbras RF 301K");
        cliente.setPropriedadeRoteadorPrincipal(PropriedadeRoteadorEnum.COMODATO);

        boolean alterou = service.atualizar(cliente, requisicao("intelbras rf 301k", PropriedadeRoteadorEnum.PROPRIO), new Usuario(), null);

        assertTrue(alterou);
        verify(historicoRepository).save(any());
        verify(sugestaoRepository, never()).registrarUso(any(), any(), any());
    }

    @Test
    void sugestoesDevemRespeitarOrdemDeFrequenciaEPesquisaNormalizada() {
        ModeloRoteadorSugestao maisUsado = sugestao("TP-Link Archer C6", 8);
        ModeloRoteadorSugestao menosUsado = sugestao("TP-Link Archer AX10", 3);
        when(sugestaoRepository.findByModeloNormalizadoContainingOrderByFrequenciaDescModeloAsc(eq("archer"), any()))
                .thenReturn(List.of(maisUsado, menosUsado));

        List<ModeloRoteadorSugestaoResponseDTO> resultado = service.buscarSugestoes("  ArChEr ", 10);

        assertEquals(List.of("TP-Link Archer C6", "TP-Link Archer AX10"), resultado.stream().map(ModeloRoteadorSugestaoResponseDTO::getModelo).toList());
    }

    @Test
    void entradaVaziaNaoConsultaODicionario() {
        assertTrue(service.buscarSugestoes("   ", 10).isEmpty());
        verify(sugestaoRepository, never()).findByModeloNormalizadoContainingOrderByFrequenciaDescModeloAsc(any(), any());
    }

    private RoteadorPrincipalRequestDTO requisicao(String modelo, PropriedadeRoteadorEnum propriedade) {
        RoteadorPrincipalRequestDTO dto = new RoteadorPrincipalRequestDTO();
        dto.setModelo(modelo);
        dto.setPropriedade(propriedade);
        return dto;
    }

    private ModeloRoteadorSugestao sugestao(String modelo, long frequencia) {
        ModeloRoteadorSugestao sugestao = new ModeloRoteadorSugestao();
        sugestao.setModelo(modelo);
        sugestao.setFrequencia(frequencia);
        return sugestao;
    }
}
