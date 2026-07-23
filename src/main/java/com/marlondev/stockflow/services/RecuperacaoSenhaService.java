package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.RecuperacaoSenha;
import com.marlondev.stockflow.domain.Usuario;
import com.marlondev.stockflow.domain.enums.StatusUsuario;
import com.marlondev.stockflow.dto.EsqueciSenhaRequestDTO;
import com.marlondev.stockflow.dto.EsqueciSenhaResponseDTO;
import com.marlondev.stockflow.dto.RecuperacaoSenhaValidacaoResponseDTO;
import com.marlondev.stockflow.dto.RedefinirSenhaRequestDTO;
import com.marlondev.stockflow.dto.RedefinirSenhaResponseDTO;
import com.marlondev.stockflow.repositories.RecuperacaoSenhaRepository;
import com.marlondev.stockflow.repositories.UsuarioRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;

@Service
public class RecuperacaoSenhaService {

    private static final String MENSAGEM_SOLICITACAO =
            "Se o login informado existir e estiver ativo, a recuperação de senha será enviada.";

    private final SecureRandom secureRandom = new SecureRandom();
    private final UsuarioRepository usuarioRepository;
    private final RecuperacaoSenhaRepository recuperacaoSenhaRepository;
    private final PasswordEncoder passwordEncoder;

    public RecuperacaoSenhaService(
            UsuarioRepository usuarioRepository,
            RecuperacaoSenhaRepository recuperacaoSenhaRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.recuperacaoSenhaRepository = recuperacaoSenhaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public EsqueciSenhaResponseDTO solicitarRecuperacao(EsqueciSenhaRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByLogin(dto.getLogin()).orElse(null);

        if (usuario == null || usuario.getStatus() != StatusUsuario.ATIVO) {
            return new EsqueciSenhaResponseDTO(true, MENSAGEM_SOLICITACAO, null);
        }

        cancelarRecuperacoesAbertas(usuario.getId());
        String token = criarRecuperacao(usuario);

        return new EsqueciSenhaResponseDTO(true, MENSAGEM_SOLICITACAO, token);
    }

    public RecuperacaoSenhaValidacaoResponseDTO validarToken(String token) {
        RecuperacaoSenha recuperacao = buscarRecuperacaoValida(token);

        if (recuperacao == null) {
            return new RecuperacaoSenhaValidacaoResponseDTO(false, null, null);
        }

        Usuario usuario = recuperacao.getUsuario();

        return new RecuperacaoSenhaValidacaoResponseDTO(
                true,
                usuario.getNome(),
                usuario.getLogin()
        );
    }

    public RedefinirSenhaResponseDTO redefinirSenha(RedefinirSenhaRequestDTO dto) {
        if (!dto.getSenha().equals(dto.getConfirmacaoSenha())) {
            throw new DatabaseException("Senha e confirmação de senha não conferem!");
        }

        RecuperacaoSenha recuperacao = buscarRecuperacaoValida(dto.getToken());

        if (recuperacao == null) {
            throw new DatabaseException("Token de recuperação inválido ou expirado!");
        }

        Usuario usuario = recuperacao.getUsuario();
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        recuperacao.setDataUtilizacao(LocalDateTime.now());

        usuarioRepository.save(usuario);
        recuperacaoSenhaRepository.save(recuperacao);

        return new RedefinirSenhaResponseDTO(true, "Senha redefinida com sucesso!");
    }

    private RecuperacaoSenha buscarRecuperacaoValida(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }

        String tokenHash = gerarHashToken(token);

        RecuperacaoSenha recuperacao = recuperacaoSenhaRepository.findByTokenHash(tokenHash)
                .orElse(null);

        if (recuperacao == null) {
            return null;
        }

        if (recuperacao.getDataUtilizacao() != null) {
            return null;
        }

        if (recuperacao.getDataCancelamento() != null) {
            return null;
        }

        if (recuperacao.getDataExpiracao().isBefore(LocalDateTime.now())) {
            return null;
        }

        if (recuperacao.getUsuario().getStatus() != StatusUsuario.ATIVO) {
            return null;
        }

        return recuperacao;
    }

    private void cancelarRecuperacoesAbertas(Long usuarioId) {
        List<RecuperacaoSenha> recuperacoesAbertas = recuperacaoSenhaRepository
                .findByUsuarioIdAndDataUtilizacaoIsNullAndDataCancelamentoIsNull(usuarioId);

        LocalDateTime agora = LocalDateTime.now();

        for (RecuperacaoSenha recuperacao : recuperacoesAbertas) {
            recuperacao.setDataCancelamento(agora);
        }

        recuperacaoSenhaRepository.saveAll(recuperacoesAbertas);
    }

    private String criarRecuperacao(Usuario usuario) {
        String token = gerarToken();
        String tokenHash = gerarHashToken(token);
        LocalDateTime agora = LocalDateTime.now();

        RecuperacaoSenha recuperacao = new RecuperacaoSenha();
        recuperacao.setUsuario(usuario);
        recuperacao.setTokenHash(tokenHash);
        recuperacao.setDataCriacao(agora);
        recuperacao.setDataExpiracao(agora.plusHours(1));

        recuperacaoSenhaRepository.save(recuperacao);

        return token;
    }

    private String gerarToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return HexFormat.of().formatHex(bytes);
    }

    private String gerarHashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 não disponível", ex);
        }
    }
}
