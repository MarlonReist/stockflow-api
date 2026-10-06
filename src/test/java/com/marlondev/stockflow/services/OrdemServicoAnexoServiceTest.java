package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.OrdemDeServico;
import com.marlondev.stockflow.domain.OrdemServicoAnexo;
import com.marlondev.stockflow.domain.enums.StatusEnum;
import com.marlondev.stockflow.dto.OrdemServicoAnexoNomeRequestDTO;
import com.marlondev.stockflow.dto.OrdemServicoAnexoResponseDTO;
import com.marlondev.stockflow.repositories.OrdemDeServicoRepository;
import com.marlondev.stockflow.repositories.OrdemServicoAnexoRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
class OrdemServicoAnexoServiceTest {

    @Mock
    private OrdemServicoAnexoRepository anexoRepository;

    @Mock
    private OrdemDeServicoRepository ordemDeServicoRepository;

    private OrdemServicoAnexoService service;

    @BeforeEach
    void setUp() {
        service = new OrdemServicoAnexoService(anexoRepository, ordemDeServicoRepository, "uploads-test");
    }

    @Test
    void renomearDeveAtualizarApenasNomeOriginalQuandoOsAguardandoConferencia() {
        OrdemServicoAnexo anexo = criarAnexo(StatusEnum.AGUARDANDO_CONFERENCIA);
        OrdemServicoAnexoNomeRequestDTO dto = new OrdemServicoAnexoNomeRequestDTO();
        dto.setNome("vistoria");

        when(anexoRepository.findById(1L)).thenReturn(Optional.of(anexo));
        when(anexoRepository.save(any(OrdemServicoAnexo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrdemServicoAnexoResponseDTO response = service.renomear(1L, dto);

        ArgumentCaptor<OrdemServicoAnexo> captor = ArgumentCaptor.forClass(OrdemServicoAnexo.class);
        verify(anexoRepository).save(captor.capture());
        OrdemServicoAnexo anexoSalvo = captor.getValue();

        assertEquals("vistoria.png", response.getNomeOriginal());
        assertEquals("vistoria.png", anexoSalvo.getNomeOriginal());
        assertEquals("uuid-fisico.png", anexoSalvo.getNomeArquivo());
        assertEquals("uploads/os/1/uuid-fisico.png", anexoSalvo.getCaminhoArquivo());
    }

    @Test
    void renomearDeveBloquearQuandoOsFinalizada() {
        OrdemServicoAnexo anexo = criarAnexo(StatusEnum.FINALIZADA);
        OrdemServicoAnexoNomeRequestDTO dto = new OrdemServicoAnexoNomeRequestDTO();
        dto.setNome("vistoria.png");

        when(anexoRepository.findById(1L)).thenReturn(Optional.of(anexo));

        assertThrows(DatabaseException.class, () -> service.renomear(1L, dto));
        verify(anexoRepository, never()).save(any());
    }

    private OrdemServicoAnexo criarAnexo(StatusEnum status) {
        OrdemDeServico ordemDeServico = new OrdemDeServico();
        ordemDeServico.setId(1L);
        ordemDeServico.setStatus(status);

        OrdemServicoAnexo anexo = new OrdemServicoAnexo();
        anexo.setId(1L);
        anexo.setOrdemDeServico(ordemDeServico);
        anexo.setNomeOriginal("foto.png");
        anexo.setNomeArquivo("uuid-fisico.png");
        anexo.setCaminhoArquivo("uploads/os/1/uuid-fisico.png");
        anexo.setContentType("image/png");
        anexo.setTamanho(10L);
        return anexo;
    }
}
