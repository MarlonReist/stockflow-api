package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.Usuario;
import com.marlondev.stockflow.domain.enums.StatusUsuario;
import com.marlondev.stockflow.dto.UsuarioRequestDTO;
import com.marlondev.stockflow.dto.UsuarioResponseDTO;
import com.marlondev.stockflow.repositories.UsuarioRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public UsuarioResponseDTO salvar(UsuarioRequestDTO dto){
        if (usuarioRepository.findByLogin(dto.getLogin()).isPresent()) {
            throw new DatabaseException("Login já existe!");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setLogin(dto.getLogin());
        usuario.setPerfil(dto.getPerfil());
        usuario.setStatus(StatusUsuario.CONVIDADO);
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return new UsuarioResponseDTO(usuarioSalvo);
        }

        public UsuarioResponseDTO buscarPorId(Long id){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return new UsuarioResponseDTO(usuario);
        }

        public void deletarUsuarioPorId(Long id){
        buscarPorId(id);
        usuarioRepository.deleteById(id);
        }

    public List<UsuarioResponseDTO> listarTodos() {
        List<Usuario> list = usuarioRepository.findAll();
        return list.stream().map(UsuarioResponseDTO::new).collect(Collectors.toList());
    }

    public UsuarioResponseDTO bloquearUsuario(Long id) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        if (usuarioExistente.getStatus() == StatusUsuario.BLOQUEADO) {
            throw new DatabaseException("Usuário já está bloqueado!");
        }

            usuarioExistente.setStatus(StatusUsuario.BLOQUEADO);
            usuarioRepository.save(usuarioExistente);
            return new UsuarioResponseDTO(usuarioExistente);
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
        return new UsuarioResponseDTO(usuarioExistente);
    }

    @Transactional
    public UsuarioResponseDTO atualizarUsuario(Long id, UsuarioRequestDTO dto){
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
            return new UsuarioResponseDTO(usuarioSalvo);
        }
        throw new DatabaseException("Esse login já existe!");
    }
}
