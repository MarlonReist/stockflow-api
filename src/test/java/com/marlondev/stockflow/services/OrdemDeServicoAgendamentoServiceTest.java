package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.Cliente;
import com.marlondev.stockflow.domain.Colaborador;
import com.marlondev.stockflow.domain.OrdemDeServico;
import com.marlondev.stockflow.domain.enums.StatusEnum;
import com.marlondev.stockflow.dto.OrdemDeServicoAgendamentoRequestDTO;
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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrdemDeServicoAgendamentoServiceTest {
    @Mock private OrdemDeServicoRepository ordemRepository;
    @Mock private ClienteRepository clienteRepository;
    @Mock private ColaboradorRepository colaboradorRepository;
    @Mock private TipoOrdemServicoRepository tipoRepository;
    @Mock private RoteadorPrincipalService roteadorService;
    private OrdemDeServicoService service;

    @BeforeEach
    void setUp() {
        service = new OrdemDeServicoService(
                ordemRepository,
                clienteRepository,
                colaboradorRepository,
                tipoRepository,
                roteadorService);
    }

    @Test
    void permiteTrocarTecnicoEDataDeOrdemAgendada() {
        OrdemDeServico ordem = criarOrdem(StatusEnum.AGENDADA);
        Colaborador novoTecnico = new Colaborador(9L, "Novo técnico", null, null, null);
        OrdemDeServicoAgendamentoRequestDTO dto = criarAgendamento(
                9L,
                LocalDateTime.of(2026, 10, 20, 14, 30));

        when(ordemRepository.findById(1L)).thenReturn(Optional.of(ordem));
        when(colaboradorRepository.findById(9L)).thenReturn(Optional.of(novoTecnico));
        when(ordemRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        service.agendarOs(1L, dto);

        assertEquals(StatusEnum.AGENDADA, ordem.getStatus());
        assertEquals(novoTecnico, ordem.getColaborador());
        assertEquals(dto.getDataAgendada(), ordem.getDataAgendada());
        verify(ordemRepository).save(ordem);
    }

    @Test
    void bloqueiaAlteracaoDepoisDoInicioDoAtendimento() {
        OrdemDeServico ordem = criarOrdem(StatusEnum.EM_ATENDIMENTO);
        OrdemDeServicoAgendamentoRequestDTO dto = criarAgendamento(
                9L,
                LocalDateTime.of(2026, 10, 20, 14, 30));
        when(ordemRepository.findById(1L)).thenReturn(Optional.of(ordem));

        assertThrows(DatabaseException.class, () -> service.agendarOs(1L, dto));

        verify(colaboradorRepository, never()).findById(any());
        verify(ordemRepository, never()).save(any());
    }

    private OrdemDeServico criarOrdem(StatusEnum status) {
        Cliente cliente = new Cliente();
        cliente.setId(3L);
        cliente.setNome("Cliente teste");

        OrdemDeServico ordem = new OrdemDeServico();
        ordem.setId(1L);
        ordem.setCliente(cliente);
        ordem.setStatus(status);
        return ordem;
    }

    private OrdemDeServicoAgendamentoRequestDTO criarAgendamento(
            Long colaboradorId,
            LocalDateTime data) {
        OrdemDeServicoAgendamentoRequestDTO dto = new OrdemDeServicoAgendamentoRequestDTO();
        dto.setColaboradorId(colaboradorId);
        dto.setDataAgendada(data);
        return dto;
    }
}
