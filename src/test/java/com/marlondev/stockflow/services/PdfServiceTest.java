package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.enums.ParticipacaoRelatorioTecnicoEnum;
import com.marlondev.stockflow.domain.enums.StatusEnum;
import com.marlondev.stockflow.dto.RelatorioTecnicoAtendimentoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PdfServiceTest {

    @Mock
    private RelatorioTecnicoService relatorioTecnicoService;

    private PdfService pdfService;

    @BeforeEach
    void setUp() {
        pdfService = new PdfService(null, null, null, null, relatorioTecnicoService);
    }

    @Test
    void gerarRelatorioAtendimentosTecnicosDeveGerarPdfValidoComTodosOsRegistros() {
        LocalDate dataInicial = LocalDate.of(2026, 10, 1);
        LocalDate dataFinal = LocalDate.of(2026, 10, 7);
        List<RelatorioTecnicoAtendimentoDTO> atendimentos = List.of(
                new RelatorioTecnicoAtendimentoDTO(
                        1L,
                        "Cliente A",
                        1L,
                        "Instalação",
                        10L,
                        "João",
                        11L,
                        "Carlos",
                        LocalDateTime.of(2026, 10, 2, 8, 0),
                        LocalDateTime.of(2026, 10, 2, 9, 30),
                        StatusEnum.AGUARDANDO_CONFERENCIA
                ),
                new RelatorioTecnicoAtendimentoDTO(
                        2L,
                        "Cliente B",
                        2L,
                        "Manutenção",
                        10L,
                        "João",
                        null,
                        null,
                        LocalDateTime.of(2026, 10, 3, 10, 0),
                        LocalDateTime.of(2026, 10, 3, 10, 47),
                        StatusEnum.FINALIZADA
                )
        );
        atendimentos.get(0).setParticipacaoDoTecnico("RESPONSAVEL");
        atendimentos.get(1).setParticipacaoDoTecnico("RESPONSAVEL");

        when(relatorioTecnicoService.buscarAtendimentosParaPdf(
                dataInicial,
                dataFinal,
                10L,
                ParticipacaoRelatorioTecnicoEnum.RESPONSAVEL,
                null
        )).thenReturn(atendimentos);
        when(relatorioTecnicoService.buscarNomeTecnicoFiltro(10L)).thenReturn("João");
        when(relatorioTecnicoService.buscarNomeTipoOrdemServicoFiltro(null)).thenReturn(null);

        byte[] pdf = pdfService.gerarRelatorioAtendimentosTecnicos(
                dataInicial,
                dataFinal,
                10L,
                ParticipacaoRelatorioTecnicoEnum.RESPONSAVEL,
                null
        );

        assertTrue(new String(pdf, 0, 4, StandardCharsets.ISO_8859_1).startsWith("%PDF"));
        assertTrue(pdf.length > 1000);
        verify(relatorioTecnicoService).buscarAtendimentosParaPdf(
                dataInicial,
                dataFinal,
                10L,
                ParticipacaoRelatorioTecnicoEnum.RESPONSAVEL,
                null
        );
    }

    @Test
    void gerarRelatorioAtendimentosTecnicosDeveGerarPdfValidoSemRegistros() {
        LocalDate dataInicial = LocalDate.of(2026, 10, 1);
        LocalDate dataFinal = LocalDate.of(2026, 10, 7);

        when(relatorioTecnicoService.buscarAtendimentosParaPdf(
                dataInicial,
                dataFinal,
                null,
                ParticipacaoRelatorioTecnicoEnum.AMBOS,
                null
        )).thenReturn(List.of());
        when(relatorioTecnicoService.buscarNomeTecnicoFiltro(null)).thenReturn("Todos os técnicos");
        when(relatorioTecnicoService.buscarNomeTipoOrdemServicoFiltro(null)).thenReturn(null);

        byte[] pdf = pdfService.gerarRelatorioAtendimentosTecnicos(
                dataInicial,
                dataFinal,
                null,
                ParticipacaoRelatorioTecnicoEnum.AMBOS,
                null
        );

        assertTrue(new String(pdf, 0, 4, StandardCharsets.ISO_8859_1).startsWith("%PDF"));
        assertTrue(pdf.length > 1000);
    }
}
