package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.Colaborador;
import com.marlondev.stockflow.domain.OrdemDeServico;
import com.marlondev.stockflow.domain.Usuario;
import com.marlondev.stockflow.domain.enums.PerfilUsuario;
import com.marlondev.stockflow.domain.enums.StatusEnum;
import com.marlondev.stockflow.dto.OrdemDeServicoConclusaoAtendimentoRequestDTO;
import com.marlondev.stockflow.dto.OrdemDeServicoResponseDTO;
import com.marlondev.stockflow.dto.OrdemServicoAnexoResponseDTO;
import com.marlondev.stockflow.dto.OrdemServicoItemRequestDTO;
import com.marlondev.stockflow.dto.OrdemServicoItemResponseDTO;
import com.marlondev.stockflow.dto.TecnicoOrdemServicoDetalheDTO;
import com.marlondev.stockflow.dto.TecnicoOrdemServicoItemRequestDTO;
import com.marlondev.stockflow.dto.TecnicoOrdemServicoResumoDTO;
import com.marlondev.stockflow.repositories.OrdemDeServicoRepository;
import com.marlondev.stockflow.repositories.OrdemServicoAnexoRepository;
import com.marlondev.stockflow.repositories.OrdemServicoItemRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ForbiddenException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class TecnicoOrdemServicoService {

    private static final List<StatusEnum> STATUS_VISIVEIS_PARA_TECNICO = List.of(
            StatusEnum.AGENDADA,
            StatusEnum.EM_ATENDIMENTO,
            StatusEnum.AGUARDANDO_CONFERENCIA
    );

    private final OrdemDeServicoRepository ordemDeServicoRepository;
    private final OrdemServicoItemRepository ordemServicoItemRepository;
    private final OrdemServicoAnexoRepository ordemServicoAnexoRepository;
    private final OrdemDeServicoService ordemDeServicoService;
    private final OrdemServicoItemService ordemServicoItemService;
    private final OrdemServicoAnexoService ordemServicoAnexoService;

    public TecnicoOrdemServicoService(
            OrdemDeServicoRepository ordemDeServicoRepository,
            OrdemServicoItemRepository ordemServicoItemRepository,
            OrdemServicoAnexoRepository ordemServicoAnexoRepository,
            OrdemDeServicoService ordemDeServicoService,
            OrdemServicoItemService ordemServicoItemService,
            OrdemServicoAnexoService ordemServicoAnexoService
    ) {
        this.ordemDeServicoRepository = ordemDeServicoRepository;
        this.ordemServicoItemRepository = ordemServicoItemRepository;
        this.ordemServicoAnexoRepository = ordemServicoAnexoRepository;
        this.ordemDeServicoService = ordemDeServicoService;
        this.ordemServicoItemService = ordemServicoItemService;
        this.ordemServicoAnexoService = ordemServicoAnexoService;
    }

    public List<TecnicoOrdemServicoResumoDTO> listarMinhasOrdens(Usuario usuario) {
        Colaborador colaborador = obterColaboradorTecnico(usuario);

        return ordemDeServicoRepository
                .findByColaboradorIdAndStatusInOrderByDataAgendadaAsc(
                        colaborador.getId(),
                        STATUS_VISIVEIS_PARA_TECNICO
                )
                .stream()
                .map(TecnicoOrdemServicoResumoDTO::new)
                .toList();
    }

    public TecnicoOrdemServicoDetalheDTO buscarMinhaOrdemPorId(Usuario usuario, Long osId) {
        Colaborador colaborador = obterColaboradorTecnico(usuario);
        OrdemDeServico os = buscarOrdemDoTecnico(osId, colaborador);

        List<OrdemServicoItemResponseDTO> produtosUtilizados = ordemServicoItemRepository
                .findByOrdemDeServicoId(os.getId())
                .stream()
                .map(OrdemServicoItemResponseDTO::new)
                .toList();

        List<OrdemServicoAnexoResponseDTO> anexos = ordemServicoAnexoRepository
                .findByOrdemDeServicoIdOrderByDataUploadDesc(os.getId())
                .stream()
                .map(OrdemServicoAnexoResponseDTO::new)
                .toList();

        return new TecnicoOrdemServicoDetalheDTO(os, produtosUtilizados, anexos);
    }

    public OrdemDeServicoResponseDTO iniciarAtendimento(Usuario usuario, Long osId) {
        Colaborador colaborador = obterColaboradorTecnico(usuario);
        buscarOrdemDoTecnico(osId, colaborador);

        return ordemDeServicoService.iniciarAtendimento(osId);
    }

    public OrdemDeServicoResponseDTO concluirAtendimento(
            Usuario usuario,
            Long osId,
            OrdemDeServicoConclusaoAtendimentoRequestDTO dto
    ) {
        Colaborador colaborador = obterColaboradorTecnico(usuario);
        buscarOrdemDoTecnico(osId, colaborador);

        return ordemDeServicoService.concluirAtendimento(osId, dto);
    }

    public OrdemServicoItemResponseDTO adicionarProdutoUtilizado(
            Usuario usuario,
            Long osId,
            TecnicoOrdemServicoItemRequestDTO dto
    ) {
        Colaborador colaborador = obterColaboradorTecnico(usuario);
        OrdemDeServico os = buscarOrdemDoTecnico(osId, colaborador);

        if (os.getStatus() != StatusEnum.EM_ATENDIMENTO) {
            throw new DatabaseException("Ordem de serviço precisa estar em atendimento para adicionar produto!");
        }

        OrdemServicoItemRequestDTO itemDto = new OrdemServicoItemRequestDTO(
                osId,
                dto.getProdutoId(),
                dto.getAlmoxarifadoId(),
                dto.getQuantidade()
        );

        return ordemServicoItemService.salvarDuranteAtendimento(itemDto);
    }

    public void removerProdutoUtilizado(Usuario usuario, Long osId, Long itemId) {
        Colaborador colaborador = obterColaboradorTecnico(usuario);
        OrdemDeServico os = buscarOrdemDoTecnico(osId, colaborador);

        if (os.getStatus() != StatusEnum.EM_ATENDIMENTO) {
            throw new DatabaseException("Ordem de serviço precisa estar em atendimento para remover produto!");
        }

        ordemServicoItemRepository.findByIdAndOrdemDeServicoId(itemId, osId)
                .orElseThrow(() -> new ForbiddenException("Item não pertence à ordem de serviço informada."));

        ordemServicoItemService.deletarDuranteAtendimento(itemId);
    }

    public OrdemServicoAnexoResponseDTO adicionarAnexo(
            Usuario usuario,
            Long osId,
            MultipartFile arquivo
    ) {
        Colaborador colaborador = obterColaboradorTecnico(usuario);
        OrdemDeServico os = buscarOrdemDoTecnico(osId, colaborador);

        if (os.getStatus() != StatusEnum.EM_ATENDIMENTO) {
            throw new DatabaseException("Ordem de serviço precisa estar em atendimento para adicionar anexo!");
        }

        return ordemServicoAnexoService.salvar(osId, arquivo);
    }

    private Colaborador obterColaboradorTecnico(Usuario usuario) {
        if (usuario.getPerfil() != PerfilUsuario.TECNICO) {
            throw new ForbiddenException("Acesso permitido apenas para técnicos.");
        }

        if (usuario.getColaborador() == null) {
            throw new ForbiddenException("Usuário técnico não possui colaborador vinculado.");
        }

        return usuario.getColaborador();
    }

    private OrdemDeServico buscarOrdemDoTecnico(Long osId, Colaborador colaborador) {
        return ordemDeServicoRepository.findByIdAndColaboradorId(osId, colaborador.getId())
                .orElseThrow(() -> new ForbiddenException("Ordem de serviço não pertence ao técnico autenticado."));
    }
}
