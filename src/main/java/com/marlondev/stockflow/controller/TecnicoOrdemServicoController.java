package com.marlondev.stockflow.controller;

import com.marlondev.stockflow.dto.OrdemDeServicoConclusaoAtendimentoRequestDTO;
import com.marlondev.stockflow.dto.OrdemDeServicoResponseDTO;
import com.marlondev.stockflow.dto.OrdemServicoAnexoResponseDTO;
import com.marlondev.stockflow.dto.OrdemServicoItemResponseDTO;
import com.marlondev.stockflow.dto.TecnicoOrdemServicoDetalheDTO;
import com.marlondev.stockflow.dto.TecnicoOrdemServicoItemRequestDTO;
import com.marlondev.stockflow.dto.TecnicoOrdemServicoResumoDTO;
import com.marlondev.stockflow.security.UsuarioDetails;
import com.marlondev.stockflow.services.TecnicoOrdemServicoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/tecnico")
public class TecnicoOrdemServicoController {

    private final TecnicoOrdemServicoService tecnicoOrdemServicoService;

    public TecnicoOrdemServicoController(TecnicoOrdemServicoService tecnicoOrdemServicoService) {
        this.tecnicoOrdemServicoService = tecnicoOrdemServicoService;
    }

    @GetMapping(value = "/minhas-os")
    public ResponseEntity<List<TecnicoOrdemServicoResumoDTO>> listarMinhasOrdens(
            @AuthenticationPrincipal UsuarioDetails usuarioDetails
    ) {
        List<TecnicoOrdemServicoResumoDTO> ordens =
                tecnicoOrdemServicoService.listarMinhasOrdens(usuarioDetails.getUsuario());

        return ResponseEntity.ok().body(ordens);
    }

    @GetMapping(value = "/os/{id}")
    public ResponseEntity<TecnicoOrdemServicoDetalheDTO> buscarMinhaOrdemPorId(
            @AuthenticationPrincipal UsuarioDetails usuarioDetails,
            @PathVariable Long id
    ) {
        TecnicoOrdemServicoDetalheDTO ordem =
                tecnicoOrdemServicoService.buscarMinhaOrdemPorId(usuarioDetails.getUsuario(), id);

        return ResponseEntity.ok().body(ordem);
    }

    @PatchMapping(value = "/os/{id}/iniciar")
    public ResponseEntity<OrdemDeServicoResponseDTO> iniciarAtendimento(
            @AuthenticationPrincipal UsuarioDetails usuarioDetails,
            @PathVariable Long id
    ) {
        OrdemDeServicoResponseDTO ordem =
                tecnicoOrdemServicoService.iniciarAtendimento(usuarioDetails.getUsuario(), id);

        return ResponseEntity.ok().body(ordem);
    }

    @PatchMapping(value = "/os/{id}/concluir-atendimento")
    public ResponseEntity<OrdemDeServicoResponseDTO> concluirAtendimento(
            @AuthenticationPrincipal UsuarioDetails usuarioDetails,
            @PathVariable Long id,
            @RequestBody @Valid OrdemDeServicoConclusaoAtendimentoRequestDTO dto
    ) {
        OrdemDeServicoResponseDTO ordem =
                tecnicoOrdemServicoService.concluirAtendimento(usuarioDetails.getUsuario(), id, dto);

        return ResponseEntity.ok().body(ordem);
    }

    @PostMapping(value = "/os/{id}/itens")
    public ResponseEntity<OrdemServicoItemResponseDTO> adicionarProdutoUtilizado(
            @AuthenticationPrincipal UsuarioDetails usuarioDetails,
            @PathVariable Long id,
            @RequestBody @Valid TecnicoOrdemServicoItemRequestDTO dto
    ) {
        OrdemServicoItemResponseDTO item =
                tecnicoOrdemServicoService.adicionarProdutoUtilizado(usuarioDetails.getUsuario(), id, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @DeleteMapping(value = "/os/{osId}/itens/{itemId}")
    public ResponseEntity<Void> removerProdutoUtilizado(
            @AuthenticationPrincipal UsuarioDetails usuarioDetails,
            @PathVariable Long osId,
            @PathVariable Long itemId
    ) {
        tecnicoOrdemServicoService.removerProdutoUtilizado(usuarioDetails.getUsuario(), osId, itemId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/os/{id}/anexos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<OrdemServicoAnexoResponseDTO> adicionarAnexo(
            @AuthenticationPrincipal UsuarioDetails usuarioDetails,
            @PathVariable Long id,
            @RequestParam("arquivo") MultipartFile arquivo
    ) {
        OrdemServicoAnexoResponseDTO anexo =
                tecnicoOrdemServicoService.adicionarAnexo(usuarioDetails.getUsuario(), id, arquivo);

        return ResponseEntity.status(HttpStatus.CREATED).body(anexo);
    }
}