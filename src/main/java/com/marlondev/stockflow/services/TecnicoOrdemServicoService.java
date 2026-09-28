package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.Colaborador;
import com.marlondev.stockflow.domain.OrdemDeServico;
import com.marlondev.stockflow.domain.Usuario;
import com.marlondev.stockflow.domain.enums.PerfilUsuario;
import com.marlondev.stockflow.domain.enums.StatusEnum;
import com.marlondev.stockflow.dto.OrdemServicoAnexoResponseDTO;
import com.marlondev.stockflow.dto.OrdemServicoItemResponseDTO;
import com.marlondev.stockflow.dto.TecnicoOrdemServicoDetalheDTO;
import com.marlondev.stockflow.dto.TecnicoOrdemServicoResumoDTO;
import com.marlondev.stockflow.dto.OrdemDeServicoResponseDTO;
import com.marlondev.stockflow.dto.OrdemDeServicoConclusaoAtendimentoRequestDTO;
import com.marlondev.stockflow.repositories.OrdemDeServicoRepository;
import com.marlondev.stockflow.repositories.OrdemServicoAnexoRepository;
import com.marlondev.stockflow.repositories.OrdemServicoItemRepository;
import com.marlondev.stockflow.services.exceptions.ForbiddenException;
import org.springframework.stereotype.Service;

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

    public TecnicoOrdemServicoService(
            OrdemDeServicoRepository ordemDeServicoRepository,
            OrdemServicoItemRepository ordemServicoItemRepository,
            OrdemServicoAnexoRepository ordemServicoAnexoRepository,
            OrdemDeServicoService ordemDeServicoService
    ) {
        this.ordemDeServicoRepository = ordemDeServicoRepository;
        this.ordemServicoItemRepository = ordemServicoItemRepository;
        this.ordemServicoAnexoRepository = ordemServicoAnexoRepository;
        this.ordemDeServicoService = ordemDeServicoService;
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
