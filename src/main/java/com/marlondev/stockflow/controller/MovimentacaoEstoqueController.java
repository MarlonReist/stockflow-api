package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.domain.enums.TipoMovimentacao;
import com.marlondev.stockflow.dto.MovimentacaoEstoqueResponseDTO;
import com.marlondev.stockflow.services.MovimentacaoEstoqueService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/historico")
public class MovimentacaoEstoqueController {

    private final MovimentacaoEstoqueService movimentacaoEstoqueService;

    public MovimentacaoEstoqueController(MovimentacaoEstoqueService movimentacaoEstoqueService) {
        this.movimentacaoEstoqueService = movimentacaoEstoqueService;
    }

    @GetMapping (value = "/{id}")
    public ResponseEntity<MovimentacaoEstoqueResponseDTO> buscarPorId(@PathVariable Long id){
        MovimentacaoEstoqueResponseDTO dto = movimentacaoEstoqueService.buscarPorId(id);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping
    public ResponseEntity<List<MovimentacaoEstoqueResponseDTO>> listarTodos(
            @RequestParam(required = false) String busca,
            @RequestParam(required = false) String origem,
            @RequestParam(required = false) TipoMovimentacao tipo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal
    ) {
        List<MovimentacaoEstoqueResponseDTO> listDto = movimentacaoEstoqueService.listarComFiltros(
                busca,
                origem,
                tipo,
                dataInicial,
                dataFinal
        );
        return ResponseEntity.ok().body(listDto);
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> gerarPdf(
            @RequestParam(required = false) String busca,
            @RequestParam(required = false) String origem,
            @RequestParam(required = false) TipoMovimentacao tipo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal
    ) {
        byte[] pdf = movimentacaoEstoqueService.gerarPdfHistorico(
                busca,
                origem,
                tipo,
                dataInicial,
                dataFinal
        );

        return ResponseEntity.ok()
                .header("Content-Disposition", "inline; filename=historico-movimentacoes.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
