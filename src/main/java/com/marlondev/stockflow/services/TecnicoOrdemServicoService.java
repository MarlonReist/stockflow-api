package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.Colaborador;
import com.marlondev.stockflow.domain.OrdemDeServico;
import com.marlondev.stockflow.domain.OrdemServicoAnexo;
import com.marlondev.stockflow.domain.Usuario;
import com.marlondev.stockflow.domain.enums.PerfilUsuario;
import com.marlondev.stockflow.domain.enums.StatusEnum;
import com.marlondev.stockflow.domain.enums.StatusUsuario;
import com.marlondev.stockflow.dto.OrdemDeServicoConclusaoAtendimentoRequestDTO;
import com.marlondev.stockflow.dto.OrdemDeServicoResponseDTO;
import com.marlondev.stockflow.dto.OrdemServicoAnexoResponseDTO;
import com.marlondev.stockflow.dto.OrdemServicoItemRequestDTO;
import com.marlondev.stockflow.dto.OrdemServicoItemResponseDTO;
import com.marlondev.stockflow.dto.TecnicoAjudanteRequestDTO;
import com.marlondev.stockflow.dto.TecnicoAjudanteResponseDTO;
import com.marlondev.stockflow.dto.TecnicoHistoricoOrdemServicoDetalheDTO;
import com.marlondev.stockflow.dto.TecnicoHistoricoOrdemServicoPaginaDTO;
import com.marlondev.stockflow.dto.TecnicoHistoricoOrdemServicoResumoDTO;
import com.marlondev.stockflow.dto.TecnicoOrdemServicoDetalheDTO;
import com.marlondev.stockflow.dto.TecnicoOrdemServicoItemRequestDTO;
import com.marlondev.stockflow.dto.TecnicoOrdemServicoResumoDTO;
import com.marlondev.stockflow.repositories.OrdemDeServicoRepository;
import com.marlondev.stockflow.repositories.OrdemServicoAnexoRepository;
import com.marlondev.stockflow.repositories.OrdemServicoItemRepository;
import com.marlondev.stockflow.repositories.TipoOrdemServicoRepository;
import com.marlondev.stockflow.repositories.UsuarioRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ForbiddenException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final UsuarioRepository usuarioRepository;
    private final TipoOrdemServicoRepository tipoOrdemServicoRepository;

    public TecnicoOrdemServicoService(
            OrdemDeServicoRepository ordemDeServicoRepository,
            OrdemServicoItemRepository ordemServicoItemRepository,
            OrdemServicoAnexoRepository ordemServicoAnexoRepository,
            OrdemDeServicoService ordemDeServicoService,
            OrdemServicoItemService ordemServicoItemService,
            OrdemServicoAnexoService ordemServicoAnexoService,
            UsuarioRepository usuarioRepository,
            TipoOrdemServicoRepository tipoOrdemServicoRepository
    ) {
        this.ordemDeServicoRepository = ordemDeServicoRepository;
        this.ordemServicoItemRepository = ordemServicoItemRepository;
        this.ordemServicoAnexoRepository = ordemServicoAnexoRepository;
        this.ordemDeServicoService = ordemDeServicoService;
        this.ordemServicoItemService = ordemServicoItemService;
        this.ordemServicoAnexoService = ordemServicoAnexoService;
        this.usuarioRepository = usuarioRepository;
        this.tipoOrdemServicoRepository = tipoOrdemServicoRepository;
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

    public List<TecnicoAjudanteResponseDTO> listarPossiveisAjudantes(Usuario usuario) {
        Colaborador colaborador = obterColaboradorTecnico(usuario);

        return usuarioRepository
                .findByPerfilAndStatusAndColaboradorIsNotNullOrderByColaboradorNomeAsc(
                        PerfilUsuario.TECNICO,
                        StatusUsuario.ATIVO
                )
                .stream()
                .filter(tecnico -> !tecnico.getColaborador().getId().equals(colaborador.getId()))
                .map(TecnicoAjudanteResponseDTO::new)
                .toList();
    }

    public TecnicoHistoricoOrdemServicoPaginaDTO listarHistorico(
            Usuario usuario,
            Long osId,
            LocalDate dataInicialConclusao,
            LocalDate dataFinalConclusao,
            Long tipoOrdemServicoId,
            Integer page,
            Integer size
    ) {
        Colaborador colaborador = obterColaboradorTecnico(usuario);
        PeriodoConclusao periodo = resolverPeriodoConclusao(dataInicialConclusao, dataFinalConclusao);
        validarFiltrosHistorico(tipoOrdemServicoId);
        validarPaginacao(page, size);

        Page<OrdemDeServico> pagina = ordemDeServicoRepository.buscarHistoricoDoTecnico(
                colaborador.getId(),
                osId,
                periodo.inicio(),
                periodo.fimExclusivo(),
                tipoOrdemServicoId,
                PageRequest.of(page, size)
        );

        List<TecnicoHistoricoOrdemServicoResumoDTO> conteudo = pagina.getContent()
                .stream()
                .map(os -> new TecnicoHistoricoOrdemServicoResumoDTO(os, colaborador.getId()))
                .toList();

        return new TecnicoHistoricoOrdemServicoPaginaDTO(
                conteudo,
                pagina.getTotalElements(),
                pagina.getTotalPages(),
                pagina.getNumber(),
                pagina.getSize()
        );
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

    public TecnicoHistoricoOrdemServicoDetalheDTO buscarHistoricoPorId(Usuario usuario, Long osId) {
        Colaborador colaborador = obterColaboradorTecnico(usuario);
        OrdemDeServico os = ordemDeServicoRepository
                .buscarHistoricoDoTecnicoPorId(osId, colaborador.getId())
                .orElseThrow(() -> new ForbiddenException("Ordem de servi\u00e7o n\u00e3o pertence ao hist\u00f3rico do t\u00e9cnico autenticado."));

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

        return new TecnicoHistoricoOrdemServicoDetalheDTO(os, colaborador.getId(), produtosUtilizados, anexos);
    }

    public OrdemServicoAnexo buscarAnexoHistorico(Usuario usuario, Long anexoId) {
        Colaborador colaborador = obterColaboradorTecnico(usuario);
        OrdemServicoAnexo anexo = ordemServicoAnexoService.buscarEntidadePorId(anexoId);

        ordemDeServicoRepository
                .buscarHistoricoDoTecnicoPorId(anexo.getOrdemDeServico().getId(), colaborador.getId())
                .orElseThrow(() -> new ForbiddenException("Anexo n\u00e3o pertence ao hist\u00f3rico do t\u00e9cnico autenticado."));

        return anexo;
    }

    public Resource carregarAnexo(Long anexoId) {
        return ordemServicoAnexoService.carregarArquivo(anexoId);
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

        return ordemServicoAnexoService.salvarDuranteAtendimento(osId, arquivo);
    }

    public OrdemDeServicoResponseDTO atualizarAjudante(
            Usuario usuario,
            Long osId,
            TecnicoAjudanteRequestDTO dto
    ) {
        Colaborador colaborador = obterColaboradorTecnico(usuario);
        OrdemDeServico os = buscarOrdemDoTecnico(osId, colaborador);

        if (os.getStatus() != StatusEnum.AGENDADA && os.getStatus() != StatusEnum.EM_ATENDIMENTO) {
            throw new DatabaseException("Ajudante s\u00f3 pode ser alterado com a ordem agendada ou em atendimento!");
        }

        if (dto.getAjudanteId() == null) {
            os.setAjudante(null);
            return new OrdemDeServicoResponseDTO(ordemDeServicoRepository.save(os));
        }

        if (dto.getAjudanteId().equals(colaborador.getId())) {
            throw new DatabaseException("T\u00e9cnico respons\u00e1vel n\u00e3o pode ser selecionado como ajudante!");
        }

        Usuario ajudanteUsuario = usuarioRepository
                .findByColaboradorIdAndPerfilAndStatus(
                        dto.getAjudanteId(),
                        PerfilUsuario.TECNICO,
                        StatusUsuario.ATIVO
                )
                .orElseThrow(() -> new DatabaseException("Ajudante precisa ser um t\u00e9cnico ativo com colaborador vinculado!"));

        os.setAjudante(ajudanteUsuario.getColaborador());
        return new OrdemDeServicoResponseDTO(ordemDeServicoRepository.save(os));
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

    private PeriodoConclusao resolverPeriodoConclusao(LocalDate dataInicial, LocalDate dataFinal) {
        if (dataInicial == null && dataFinal == null) {
            return new PeriodoConclusao(null, null);
        }

        if (dataInicial == null || dataFinal == null) {
            throw new DatabaseException("Data inicial e data final de conclus\u00e3o devem ser informadas juntas!");
        }

        if (dataInicial.isAfter(dataFinal)) {
            throw new DatabaseException("Data inicial de conclus\u00e3o n\u00e3o pode ser maior que a data final!");
        }

        return new PeriodoConclusao(
                dataInicial.atStartOfDay(),
                dataFinal.plusDays(1).atStartOfDay()
        );
    }

    private void validarFiltrosHistorico(Long tipoOrdemServicoId) {
        if (tipoOrdemServicoId != null && !tipoOrdemServicoRepository.existsById(tipoOrdemServicoId)) {
            throw new ResourceNotFoundException(tipoOrdemServicoId);
        }
    }

    private void validarPaginacao(Integer page, Integer size) {
        if (page == null || page < 0) {
            throw new DatabaseException("P\u00e1gina deve ser maior ou igual a zero!");
        }
        if (size == null || size <= 0) {
            throw new DatabaseException("Tamanho da p\u00e1gina deve ser maior que zero!");
        }
        if (size > 100) {
            throw new DatabaseException("Tamanho da p\u00e1gina deve ser no m\u00e1ximo 100!");
        }
    }

    private record PeriodoConclusao(LocalDateTime inicio, LocalDateTime fimExclusivo) {
    }
}
