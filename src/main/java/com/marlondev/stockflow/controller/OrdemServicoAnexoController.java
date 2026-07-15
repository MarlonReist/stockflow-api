package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.domain.OrdemServicoAnexo;
import com.marlondev.stockflow.dto.OrdemServicoAnexoResponseDTO;
import com.marlondev.stockflow.services.OrdemServicoAnexoService;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
public class OrdemServicoAnexoController {

    private final OrdemServicoAnexoService anexoService;

    public OrdemServicoAnexoController(OrdemServicoAnexoService anexoService) {
        this.anexoService = anexoService;
    }

    @PostMapping(value = "/os/{osId}/anexos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<OrdemServicoAnexoResponseDTO> salvar(@PathVariable Long osId,
                                                               @RequestParam("arquivo") MultipartFile arquivo) {
        OrdemServicoAnexoResponseDTO dto = anexoService.salvar(osId, arquivo);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/os/{osId}/anexos")
    public ResponseEntity<List<OrdemServicoAnexoResponseDTO>> listar(@PathVariable Long osId) {
        return ResponseEntity.ok(anexoService.listarPorOs(osId));
    }

    @GetMapping("/os/anexos/{anexoId}/arquivo")
    public ResponseEntity<Resource> baixar(@PathVariable Long anexoId) {
        OrdemServicoAnexo anexo = anexoService.buscarEntidadePorId(anexoId);
        Resource arquivo = anexoService.carregarArquivo(anexoId);

        MediaType mediaType = MediaType.parseMediaType(anexo.getContentType());
        ContentDisposition contentDisposition = ContentDisposition.inline()
                .filename(anexo.getNomeOriginal(), StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                .body(arquivo);
    }

    @DeleteMapping("/os/anexos/{anexoId}")
    public ResponseEntity<Void> deletar(@PathVariable Long anexoId) {
        anexoService.deletar(anexoId);
        return ResponseEntity.noContent().build();
    }
}
