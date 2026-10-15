package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.Cliente;
import com.marlondev.stockflow.domain.OrdemDeServico;
import com.marlondev.stockflow.domain.Usuario;
import com.marlondev.stockflow.domain.enums.PropriedadeRoteadorEnum;
import com.marlondev.stockflow.domain.enums.StatusEnum;
import com.marlondev.stockflow.dto.OrdemDeServicoConclusaoAtendimentoRequestDTO;
import com.marlondev.stockflow.dto.RoteadorPrincipalRequestDTO;
import com.marlondev.stockflow.repositories.ClienteRepository;
import com.marlondev.stockflow.repositories.ColaboradorRepository;
import com.marlondev.stockflow.repositories.OrdemDeServicoRepository;
import com.marlondev.stockflow.repositories.TipoOrdemServicoRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrdemDeServicoRoteadorServiceTest {
    @Mock private OrdemDeServicoRepository ordemRepository;
    @Mock private ClienteRepository clienteRepository;
    @Mock private ColaboradorRepository colaboradorRepository;
    @Mock private TipoOrdemServicoRepository tipoRepository;
    @Mock private RoteadorPrincipalService roteadorService;
    private OrdemDeServicoService service;

    @BeforeEach
    void setUp() {
        service = new OrdemDeServicoService(ordemRepository, clienteRepository, colaboradorRepository, tipoRepository, roteadorService);
    }

    @Test
    void clienteSemRoteadorNaoConcluiMesmoSemDependerDoTipoDaOs() {
        OrdemDeServico os = ordemEmAtendimento();
        when(ordemRepository.findById(1L)).thenReturn(Optional.of(os));
        when(roteadorService.possuiRoteadorPrincipal(os.getCliente())).thenReturn(false);

        assertThrows(DatabaseException.class, () -> service.concluirAtendimento(1L, conclusaoSemRoteador(), new Usuario()));

        assertEquals(StatusEnum.EM_ATENDIMENTO, os.getStatus());
        verify(ordemRepository, never()).save(any());
    }

    @Test
    void clienteComRoteadorConcluiSemAlteracao() {
        OrdemDeServico os = ordemEmAtendimento();
        when(ordemRepository.findById(1L)).thenReturn(Optional.of(os));
        when(roteadorService.possuiRoteadorPrincipal(os.getCliente())).thenReturn(true);
        when(ordemRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        service.concluirAtendimento(1L, conclusaoSemRoteador(), new Usuario());

        assertEquals(StatusEnum.AGUARDANDO_CONFERENCIA, os.getStatus());
        verify(roteadorService, never()).atualizar(any(), any(), any(), any());
    }

    @Test
    void conclusaoComRoteadorAtualizaNoMesmoFluxo() {
        OrdemDeServico os = ordemEmAtendimento();
        when(ordemRepository.findById(1L)).thenReturn(Optional.of(os));
        when(roteadorService.possuiRoteadorPrincipal(os.getCliente())).thenReturn(false);
        when(ordemRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        OrdemDeServicoConclusaoAtendimentoRequestDTO dto = conclusaoComRoteador();

        service.concluirAtendimento(1L, dto, new Usuario());

        verify(roteadorService).atualizar(os.getCliente(), dto.getRoteadorPrincipal(), org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.same(os));
    }

    @Test
    void falhaAoAtualizarRoteadorNaoAlteraStatusDaOrdem() {
        OrdemDeServico os = ordemEmAtendimento();
        OrdemDeServicoConclusaoAtendimentoRequestDTO dto = conclusaoComRoteador();
        when(ordemRepository.findById(1L)).thenReturn(Optional.of(os));
        when(roteadorService.possuiRoteadorPrincipal(os.getCliente())).thenReturn(false);
        when(roteadorService.atualizar(any(), any(), any(), any()))
                .thenThrow(new DatabaseException("falha simulada"));

        assertThrows(DatabaseException.class, () -> service.concluirAtendimento(1L, dto, new Usuario()));

        assertEquals(StatusEnum.EM_ATENDIMENTO, os.getStatus());
        verify(ordemRepository, never()).save(any());
    }

    private OrdemDeServico ordemEmAtendimento() {
        Cliente cliente = new Cliente();
        cliente.setId(3L);
        OrdemDeServico os = new OrdemDeServico();
        os.setId(1L);
        os.setCliente(cliente);
        os.setStatus(StatusEnum.EM_ATENDIMENTO);
        return os;
    }

    private OrdemDeServicoConclusaoAtendimentoRequestDTO conclusaoSemRoteador() {
        OrdemDeServicoConclusaoAtendimentoRequestDTO dto = new OrdemDeServicoConclusaoAtendimentoRequestDTO();
        dto.setObservacaoConclusao("Atendimento concluído");
        return dto;
    }

    private OrdemDeServicoConclusaoAtendimentoRequestDTO conclusaoComRoteador() {
        OrdemDeServicoConclusaoAtendimentoRequestDTO dto = conclusaoSemRoteador();
        RoteadorPrincipalRequestDTO roteador = new RoteadorPrincipalRequestDTO();
        roteador.setModelo("Modelo livre sem produto");
        roteador.setPropriedade(PropriedadeRoteadorEnum.COMODATO);
        dto.setRoteadorPrincipal(roteador);
        return dto;
    }
}
