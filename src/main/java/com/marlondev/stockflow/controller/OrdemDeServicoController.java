package com.marlondev.stockflow.controller;
import com.marlondev.stockflow.dto.OrdemDeServicoRequestDTO;
import com.marlondev.stockflow.dto.OrdemDeServicoResponseDTO;
import com.marlondev.stockflow.services.OrdemDeServicoService;
import com.marlondev.stockflow.services.PdfService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/os")
public class OrdemDeServicoController {

    private final OrdemDeServicoService osService;
    private final PdfService pdfService;

    public OrdemDeServicoController(OrdemDeServicoService osService, PdfService pdfService) {
        this.osService = osService;
        this.pdfService = pdfService;
    }

    @PostMapping
    public ResponseEntity<OrdemDeServicoResponseDTO> salvarOs(@RequestBody @Valid OrdemDeServicoRequestDTO dto){
        OrdemDeServicoResponseDTO dtoSalvar = osService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoSalvar);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<OrdemDeServicoResponseDTO> buscarPorId(@PathVariable Long id) {
        OrdemDeServicoResponseDTO obj = osService.buscarPorId(id);
        return ResponseEntity.ok(obj);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable Long id) {
        osService.deletarOsPorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<OrdemDeServicoResponseDTO>> listarTodos(){
        List<OrdemDeServicoResponseDTO> listDto = osService.listarTodos();
        return ResponseEntity.ok().body(listDto);
    }

    @PutMapping(value = "/{id}/descricao")
    public ResponseEntity<OrdemDeServicoResponseDTO> atualizarDescricao(@PathVariable Long id, @RequestBody @Valid OrdemDeServicoRequestDTO dto) {
        OrdemDeServicoResponseDTO descricaoAtualizada = osService.atualizarDescricao(id, dto);
        return ResponseEntity.ok().body(descricaoAtualizada);
    }

    @PatchMapping (value = "/{id}/finalizar")
    public ResponseEntity<OrdemDeServicoResponseDTO> finalizarOs(@PathVariable Long id) {
        OrdemDeServicoResponseDTO osFinalizada = osService.finalizarOs(id);
        return ResponseEntity.ok().body(osFinalizada);
    }

    @PatchMapping (value = "/{id}/cancelar")
    public ResponseEntity<OrdemDeServicoResponseDTO> cancelarOs(@PathVariable Long id) {
        OrdemDeServicoResponseDTO osCancelada = osService.cancelarOs(id);
        return ResponseEntity.ok().body(osCancelada);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> gerarPdf(@PathVariable Long id) {
        byte[] pdf = pdfService.gerarPdfOrdemServico(id);

        return ResponseEntity.ok()
                .header("Content-Disposition", "inline; filename=os-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/{id}/produtos/pdf")
    public ResponseEntity<byte[]> gerarRelatorioProdutosPdf(@PathVariable Long id) {
        byte[] pdf = pdfService.gerarRelatorioProdutosOrdemServico(id);

        return ResponseEntity.ok()
                .header("Content-Disposition", "inline; filename=os-" + id + "-produtos.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
