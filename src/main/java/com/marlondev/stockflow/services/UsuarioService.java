package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.Usuario;
import com.marlondev.stockflow.domain.enums.StatusUsuario;
import com.marlondev.stockflow.dto.*;
import com.marlondev.stockflow.repositories.ConviteUsuarioRepository;
import com.marlondev.stockflow.repositories.UsuarioRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Value("${stockflow.convite.cooldown-minutos}")
    private long cooldownReenvioMinutos;

    private final UsuarioRepository usuarioRepository;
    private final ConviteUsuarioRepository conviteUsuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, ConviteUsuarioRepository conviteUsuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.conviteUsuarioRepository = conviteUsuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponseDTO salvar(UsuarioRequestDTO dto) {
        if (usuarioRepository.findByLogin(dto.getLogin()).isPresent()) {
            throw new DatabaseException("Login já existe!");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setLogin(dto.getLogin());
        usuario.setPerfil(dto.getPerfil());
        usuario.setStatus(StatusUsuario.CONVIDADO);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return toResponseDTO(usuarioSalvo);
    }

    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return toResponseDTO(usuario);
    }

    public void deletarUsuarioPorId(Long id) {
        buscarPorId(id);
        usuarioRepository.deleteById(id);
    }

    public List<UsuarioResponseDTO> listarTodos() {
        List<Usuario> list = usuarioRepository.findAll();
        return list.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    public UsuarioResponseDTO bloquearUsuario(Long id) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        if (usuarioExistente.getStatus() == StatusUsuario.BLOQUEADO) {
            throw new DatabaseException("Usuário já está bloqueado!");
        }

        usuarioExistente.setStatus(StatusUsuario.BLOQUEADO);
        usuarioRepository.save(usuarioExistente);
        return toResponseDTO(usuarioExistente);
    }

    public UsuarioResponseDTO desbloquearUsuario(Long id) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        if (usuarioExistente.getStatus() == StatusUsuario.CONVIDADO) {
            throw new DatabaseException("Usuário convidado precisa ativar a conta pelo convite!");
        }

        if (usuarioExistente.getStatus() == StatusUsuario.ATIVO) {
            throw new DatabaseException("Usuário já está ativo!");
        }

        usuarioExistente.setStatus(StatusUsuario.ATIVO);
        usuarioRepository.save(usuarioExistente);
        return toResponseDTO(usuarioExistente);
    }

    @Transactional
    public UsuarioResponseDTO atualizarUsuario(Long id, UsuarioRequestDTO dto) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        if (usuarioExistente.getStatus() == StatusUsuario.BLOQUEADO) {
            throw new DatabaseException("Usuário está bloqueado!");
        }

        Usuario outroUsuario = usuarioRepository.findByLogin(dto.getLogin()).orElse(null);

        if (outroUsuario == null || outroUsuario.getId().equals(usuarioExistente.getId())) {
            usuarioExistente.setNome(dto.getNome());
            usuarioExistente.setLogin(dto.getLogin());
            usuarioExistente.setPerfil(dto.getPerfil());

            Usuario usuarioSalvo = usuarioRepository.save(usuarioExistente);
            return toResponseDTO(usuarioSalvo);
        }

        throw new DatabaseException("Esse login já existe!");
    }

    public MeuPerfilResponseDTO buscarMeuPerfil(Usuario usuarioAutenticado) {
        return new MeuPerfilResponseDTO(usuarioAutenticado);
    }

    @Transactional
    public AlterarSenhaResponseDTO alterarMinhaSenha(
            Usuario usuarioAutenticado,
            AlterarSenhaRequestDTO dto
    ) {
        if (!dto.getNovaSenha().equals(dto.getConfirmacaoSenha())) {
            throw new DatabaseException("Nova senha e confirmação de senha não conferem!");
        }

        if (usuarioAutenticado.getSenha() == null || usuarioAutenticado.getSenha().isBlank()) {
            throw new DatabaseException("Usuário ainda não possui senha definida!");
        }

        boolean senhaAtualCorreta = passwordEncoder.matches(
                dto.getSenhaAtual(),
                usuarioAutenticado.getSenha()
        );

        if (!senhaAtualCorreta) {
            throw new DatabaseException("Senha atual inválida!");
        }

        usuarioAutenticado.setSenha(passwordEncoder.encode(dto.getNovaSenha()));
        usuarioRepository.save(usuarioAutenticado);

        return new AlterarSenhaResponseDTO(true, "Senha alterada com sucesso!");
    }

    private boolean podeReenviarConvite(Usuario usuario) {
        if (usuario.getStatus() != StatusUsuario.CONVIDADO) {
            return false;
        }

        return conviteUsuarioRepository
                .findFirstByUsuarioIdAndDataUtilizacaoIsNullAndDataCancelamentoIsNullOrderByDataCriacaoDesc(usuario.getId())
                .map(convite -> {
                    LocalDateTime proximoReenvioPermitido = convite
                            .getDataCriacao()
                            .plusMinutes(cooldownReenvioMinutos);

                    return !LocalDateTime.now().isBefore(proximoReenvioPermitido);
                })
                .orElse(true);
    }

    private long segundosParaReenviarConvite(Usuario usuario) {
        if (usuario.getStatus() != StatusUsuario.CONVIDADO) {
            return 0L;
        }

        return conviteUsuarioRepository
                .findFirstByUsuarioIdAndDataUtilizacaoIsNullAndDataCancelamentoIsNullOrderByDataCriacaoDesc(usuario.getId())
                .map(convite -> {
                    LocalDateTime agora = LocalDateTime.now();

                    LocalDateTime proximoReenvioPermitido = convite
                            .getDataCriacao()
                            .plusMinutes(cooldownReenvioMinutos);

                    if (!agora.isBefore(proximoReenvioPermitido)) {
                        return 0L;
                    }

                    return Duration.between(agora, proximoReenvioPermitido).getSeconds();
                })
                .orElse(0L);
    }

    private UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        boolean conviteExpirado = usuario.getStatus() == StatusUsuario.CONVIDADO
                && conviteUsuarioRepository.existsByUsuarioIdAndDataUtilizacaoIsNullAndDataCancelamentoIsNullAndDataExpiracaoBefore(
                usuario.getId(),
                LocalDateTime.now()
        );

        boolean podeReenviarConvite = podeReenviarConvite(usuario);
        long segundosParaReenviarConvite = segundosParaReenviarConvite(usuario);

        return new UsuarioResponseDTO(
                usuario,
                conviteExpirado,
                podeReenviarConvite,
                segundosParaReenviarConvite
        );
    }
}
