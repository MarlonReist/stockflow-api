package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.Usuario;
import com.marlondev.stockflow.domain.enums.StatusUsuario;
import com.marlondev.stockflow.dto.LoginRequestDTO;
import com.marlondev.stockflow.dto.LoginResponseDTO;
import com.marlondev.stockflow.repositories.UsuarioRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByLogin(dto.getLogin())
                .orElseThrow(() -> new DatabaseException("Login ou senha inválidos!"));

        if (usuario.getStatus() != StatusUsuario.ATIVO) {
            throw new DatabaseException("Usuário não está ativo!");
        }

        if (usuario.getSenha() == null || usuario.getSenha().isBlank()) {
            throw new DatabaseException("Usuário ainda não definiu senha!");
        }

        boolean senhaCorreta = passwordEncoder.matches(dto.getSenha(), usuario.getSenha());

        if (!senhaCorreta) {
            throw new DatabaseException("Login ou senha inválidos!");
        }

        return new LoginResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getLogin(),
                usuario.getPerfil(),
                usuario.getStatus(),
                true
        );
    }
}