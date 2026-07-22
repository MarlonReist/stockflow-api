package com.marlondev.stockflow.services;

import com.marlondev.stockflow.dto.AtivarConviteRequestDTO;
import com.marlondev.stockflow.repositories.ConviteUsuarioRepository;
import com.marlondev.stockflow.repositories.UsuarioRepository;
import com.marlondev.stockflow.domain.enums.StatusUsuario;
import com.marlondev.stockflow.dto.ConviteUsuarioResponseDTO;
import com.marlondev.stockflow.dto.UsuarioRequestDTO;
import com.marlondev.stockflow.domain.ConviteUsuario;
import com.marlondev.stockflow.domain.Usuario;
import com.marlondev.stockflow.dto.ConviteValidacaoResponseDTO;
import com.marlondev.stockflow.services.exceptions.DatabaseException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConviteUsuarioService {

    private final SecureRandom secureRandom = new SecureRandom();
    private final UsuarioRepository usuarioRepository;
    private final ConviteUsuarioRepository conviteUsuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public ConviteUsuarioService(UsuarioRepository usuarioRepository, ConviteUsuarioRepository conviteUsuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.conviteUsuarioRepository = conviteUsuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ConviteUsuarioResponseDTO convidarUsuario(UsuarioRequestDTO dto) {
        if (usuarioRepository.findByLogin(dto.getLogin()).isPresent()) {
            throw new DatabaseException("Login já existe!");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setLogin(dto.getLogin());
        usuario.setPerfil(dto.getPerfil());
        usuario.setStatus(StatusUsuario.CONVIDADO);
        usuario.setSenha(null);

        usuario = usuarioRepository.save(usuario);

        String token = criarConvite(usuario);

        return new ConviteUsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getLogin(),
                usuario.getPerfil(),
                usuario.getStatus(),
                token
        );
    }

    public ConviteValidacaoResponseDTO validarConvite(String token) {
        if (token == null || token.isBlank()) {
            return new ConviteValidacaoResponseDTO(false, null, null);
        }

        String tokenHash = gerarHashToken(token);

        ConviteUsuario convite = conviteUsuarioRepository.findByTokenHash(tokenHash)
                .orElse(null);

        if (convite == null) {
            return new ConviteValidacaoResponseDTO(false, null, null);
        }

        if (convite.getDataUtilizacao() != null) {
            return new ConviteValidacaoResponseDTO(false, null, null);
        }

        if (convite.getDataCancelamento() != null) {
            return new ConviteValidacaoResponseDTO(false, null, null);
        }

        if (convite.getDataExpiracao().isBefore(LocalDateTime.now())) {
            return new ConviteValidacaoResponseDTO(false, null, null);
        }

        if (convite.getUsuario().getStatus() != StatusUsuario.CONVIDADO) {
            return new ConviteValidacaoResponseDTO(false, null, null);
        }

        Usuario usuario = convite.getUsuario();

        return new ConviteValidacaoResponseDTO(
                true,
                usuario.getNome(),
                usuario.getLogin()
        );
    }

    public ConviteValidacaoResponseDTO ativarConvite(AtivarConviteRequestDTO dto) {
        if (!dto.getSenha().equals(dto.getConfirmacaoSenha())) {
            throw new DatabaseException("Senha e confirmação de senha não conferem!");
        }

        String tokenHash = gerarHashToken(dto.getToken());

        ConviteUsuario convite = conviteUsuarioRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new DatabaseException("Convite inválido!"));

        if (convite.getDataUtilizacao() != null) {
            throw new DatabaseException("Convite já utilizado!");
        }

        if (convite.getDataCancelamento() != null) {
            throw new DatabaseException("Convite cancelado!");
        }

        if (convite.getDataExpiracao().isBefore(LocalDateTime.now())) {
            throw new DatabaseException("Convite expirado!");
        }

        Usuario usuario = convite.getUsuario();

        if (usuario.getStatus() != StatusUsuario.CONVIDADO) {
            throw new DatabaseException("Usuário não está aguardando ativação!");
        }

        String senhaCriptografada = passwordEncoder.encode(dto.getSenha());

        usuario.setSenha(senhaCriptografada);
        usuario.setStatus(StatusUsuario.ATIVO);

        convite.setDataUtilizacao(LocalDateTime.now());

        usuarioRepository.save(usuario);
        conviteUsuarioRepository.save(convite);

        return new ConviteValidacaoResponseDTO(
                true,
                usuario.getNome(),
                usuario.getLogin()
        );
    }

    private void cancelarConvitesAbertos(Long usuarioId) {
        List<ConviteUsuario> convitesAbertos = conviteUsuarioRepository
                .findByUsuarioIdAndDataUtilizacaoIsNullAndDataCancelamentoIsNull(usuarioId);

        LocalDateTime agora = LocalDateTime.now();

        for (ConviteUsuario convite : convitesAbertos) {
            convite.setDataCancelamento(agora);
        }

        conviteUsuarioRepository.saveAll(convitesAbertos);
    }

    private String criarConvite(Usuario usuario) {
        String token = gerarToken();
        String tokenHash = gerarHashToken(token);

        LocalDateTime agora = LocalDateTime.now();

        ConviteUsuario convite = new ConviteUsuario();
        convite.setUsuario(usuario);
        convite.setTokenHash(tokenHash);
        convite.setDataCriacao(agora);
        convite.setDataExpiracao(agora.plusDays(2));

        conviteUsuarioRepository.save(convite);

        return token;
    }

    String gerarToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return HexFormat.of().formatHex(bytes);
    }

    String gerarHashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 não disponível", ex);
        }
    }
}