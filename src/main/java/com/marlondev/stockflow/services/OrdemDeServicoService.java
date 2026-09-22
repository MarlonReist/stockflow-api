package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.Cliente;
import com.marlondev.stockflow.domain.Colaborador;
import com.marlondev.stockflow.domain.OrdemDeServico;
import com.marlondev.stockflow.domain.TipoOrdemServico;
import com.marlondev.stockflow.domain.enums.StatusEnum;
import com.marlondev.stockflow.dto.OrdemDeServicoAgendamentoRequestDTO;
import com.marlondev.stockflow.dto.OrdemDeServicoConclusaoAtendimentoRequestDTO;
import com.marlondev.stockflow.dto.OrdemDeServicoDescricaoRequestDTO;
import com.marlondev.stockflow.dto.OrdemDeServicoRequestDTO;
import com.marlondev.stockflow.dto.OrdemDeServicoResponseDTO;
import com.marlondev.stockflow.dto.OrdemDeServicoTipoRequestDTO;
import com.marlondev.stockflow.repositories.ClienteRepository;
import com.marlondev.stockflow.repositories.ColaboradorRepository;
import com.marlondev.stockflow.repositories.OrdemDeServicoRepository;
import com.marlondev.stockflow.repositories.TipoOrdemServicoRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdemDeServicoService {

    private final OrdemDeServicoRepository ordemDeServicoRepository;
    private final ClienteRepository clienteRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final TipoOrdemServicoRepository tipoOrdemServicoRepository;

    public OrdemDeServicoService(OrdemDeServicoRepository ordemDeServicoRepository, ClienteRepository clienteRepository, ColaboradorRepository colaboradorRepository, TipoOrdemServicoRepository tipoOrdemServicoRepository) {
        this.ordemDeServicoRepository = ordemDeServicoRepository;
        this.clienteRepository = clienteRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.tipoOrdemServicoRepository = tipoOrdemServicoRepository;
    }

    @Transactional
    public OrdemDeServicoResponseDTO salvar(OrdemDeServicoRequestDTO dto) {
        Cliente clienteEncontrado = clienteRepository.findById((dto.getClienteId()))
                .orElseThrow(() -> new ResourceNotFoundException(dto.getClienteId()));
        Colaborador colaboradorEncontrado = null;

        if (dto.getColaboradorId() != null) {
            colaboradorEncontrado = colaboradorRepository.findById(dto.getColaboradorId())
                    .orElseThrow(() -> new ResourceNotFoundException(dto.getColaboradorId()));
        }
        TipoOrdemServico tipoEncontrado = tipoOrdemServicoRepository.findById(dto.getTipoOrdemServicoId())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getTipoOrdemServicoId()));

        if (!Boolean.TRUE.equals(tipoEncontrado.getAtivo())) {
            throw new DatabaseException("Tipo de ordem de serviço está inativo!");
        }

        OrdemDeServico os = new OrdemDeServico();
        os.setDescricao(dto.getDescricao());
        os.setCliente(clienteEncontrado);
        os.setColaborador(colaboradorEncontrado);
        os.setDataAbertura(LocalDate.now());
        os.setStatus(StatusEnum.ABERTA);
        os.setTipoOrdemServico(tipoEncontrado);
        OrdemDeServico osSalva = ordemDeServicoRepository.save(os);
        return new OrdemDeServicoResponseDTO(osSalva);
    }

    public OrdemDeServicoResponseDTO buscarPorId(Long id) {
        OrdemDeServico buscarOs = ordemDeServicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return new OrdemDeServicoResponseDTO(buscarOs);
    }

    public void deletarOsPorId(Long id) {
        OrdemDeServico osEncontrada = ordemDeServicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        if (osEncontrada.getStatus() != StatusEnum.ABERTA) {
            throw new DatabaseException("Ordem de serviço não está aberta!");
        }
        ordemDeServicoRepository.delete(osEncontrada);
    }

    public List<OrdemDeServicoResponseDTO> listarTodos() {
        List<OrdemDeServico> list = ordemDeServicoRepository.findAll();
        return list.stream().map(OrdemDeServicoResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public OrdemDeServicoResponseDTO atualizarDescricao(Long id, OrdemDeServicoDescricaoRequestDTO dto) {
        OrdemDeServico osExistente = ordemDeServicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        if (osExistente.getStatus() != StatusEnum.ABERTA) {
            throw new DatabaseException("Ordem de serviço não está aberta!");
        }
        osExistente.setDescricao(dto.getDescricao());
        OrdemDeServico osSalva = ordemDeServicoRepository.save(osExistente);
        return new OrdemDeServicoResponseDTO(osSalva);
    }

    @Transactional
    public OrdemDeServicoResponseDTO atualizarTipo(Long id, OrdemDeServicoTipoRequestDTO dto) {
        OrdemDeServico osExistente = ordemDeServicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        if (osExistente.getStatus() != StatusEnum.ABERTA) {
            throw new DatabaseException("Ordem de serviço não está aberta!");
        }

        TipoOrdemServico tipoEncontrado = tipoOrdemServicoRepository.findById(dto.getTipoOrdemServicoId())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getTipoOrdemServicoId()));

        if (!Boolean.TRUE.equals(tipoEncontrado.getAtivo())) {
            throw new DatabaseException("Tipo de ordem de serviço está inativo!");
        }

        osExistente.setTipoOrdemServico(tipoEncontrado);

        OrdemDeServico osSalva = ordemDeServicoRepository.save(osExistente);
        return new OrdemDeServicoResponseDTO(osSalva);
    }

    @Transactional
    public OrdemDeServicoResponseDTO finalizarOs(Long id) {
        OrdemDeServico osExistente = ordemDeServicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        if (osExistente.getStatus() != StatusEnum.AGUARDANDO_CONFERENCIA) {
            throw new DatabaseException("Ordem de serviço precisa estar aguardando conferência para ser finalizada!");
        }

        osExistente.setStatus(StatusEnum.FINALIZADA);
        osExistente.setDataFechamento(LocalDateTime.now());

        OrdemDeServico osSalva = ordemDeServicoRepository.save(osExistente);
        return new OrdemDeServicoResponseDTO(osSalva);
    }

    @Transactional
    public OrdemDeServicoResponseDTO cancelarOs(Long id) {
        OrdemDeServico osExistente = ordemDeServicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        if (osExistente.getStatus() != StatusEnum.ABERTA
                && osExistente.getStatus() != StatusEnum.AGENDADA) {
            throw new DatabaseException("Ordem de serviço só pode ser cancelada quando estiver aberta ou agendada!");
        }

        osExistente.setStatus(StatusEnum.CANCELADA);
        osExistente.setDataFechamento(LocalDateTime.now());

        OrdemDeServico osSalva = ordemDeServicoRepository.save(osExistente);
        return new OrdemDeServicoResponseDTO(osSalva);
    }

    @Transactional
    public OrdemDeServicoResponseDTO agendarOs(Long id, OrdemDeServicoAgendamentoRequestDTO dto) {
        OrdemDeServico osExistente = ordemDeServicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        if (osExistente.getStatus() != StatusEnum.ABERTA) {
            throw new DatabaseException("Ordem de serviço precisa estar aberta para ser agendada!");
        }

        Colaborador colaboradorEncontrado = colaboradorRepository.findById(dto.getColaboradorId())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getColaboradorId()));

        osExistente.setColaborador(colaboradorEncontrado);
        osExistente.setDataAgendada(dto.getDataAgendada());
        osExistente.setStatus(StatusEnum.AGENDADA);

        OrdemDeServico osSalva = ordemDeServicoRepository.save(osExistente);
        return new OrdemDeServicoResponseDTO(osSalva);
    }

    @Transactional
    public OrdemDeServicoResponseDTO iniciarAtendimento(Long id) {
        OrdemDeServico osExistente = ordemDeServicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        if (osExistente.getStatus() != StatusEnum.AGENDADA) {
            throw new DatabaseException("Ordem de serviço precisa estar agendada para iniciar atendimento!");
        }

        osExistente.setInicioAtendimento(LocalDateTime.now());
        osExistente.setStatus(StatusEnum.EM_ATENDIMENTO);

        OrdemDeServico osSalva = ordemDeServicoRepository.save(osExistente);
        return new OrdemDeServicoResponseDTO(osSalva);
    }

    @Transactional
    public OrdemDeServicoResponseDTO concluirAtendimento(Long id, OrdemDeServicoConclusaoAtendimentoRequestDTO dto) {
        OrdemDeServico osExistente = ordemDeServicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        if (osExistente.getStatus() != StatusEnum.EM_ATENDIMENTO) {
            throw new DatabaseException("Ordem de serviço precisa estar em atendimento para ser concluída!");
        }

        osExistente.setFimAtendimento(LocalDateTime.now());
        osExistente.setObservacaoConclusao(dto.getObservacaoConclusao());
        osExistente.setStatus(StatusEnum.AGUARDANDO_CONFERENCIA);

        OrdemDeServico osSalva = ordemDeServicoRepository.save(osExistente);
        return new OrdemDeServicoResponseDTO(osSalva);
    }

}
