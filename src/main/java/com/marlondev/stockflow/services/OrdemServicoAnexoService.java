package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.OrdemDeServico;
import com.marlondev.stockflow.domain.OrdemServicoAnexo;
import com.marlondev.stockflow.dto.OrdemServicoAnexoResponseDTO;
import com.marlondev.stockflow.repositories.OrdemDeServicoRepository;
import com.marlondev.stockflow.repositories.OrdemServicoAnexoRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrdemServicoAnexoService {

    private static final List<String> TIPOS_PERMITIDOS = List.of(
            "image/jpeg",
            "image/png",
            "application/pdf"
    );

    private final OrdemServicoAnexoRepository anexoRepository;
    private final OrdemDeServicoRepository ordemDeServicoRepository;
    private final Path uploadRoot;

    public OrdemServicoAnexoService(OrdemServicoAnexoRepository anexoRepository,
                                    OrdemDeServicoRepository ordemDeServicoRepository,
                                    @Value("${stockflow.upload-dir:uploads}") String uploadDir) {
        this.anexoRepository = anexoRepository;
        this.ordemDeServicoRepository = ordemDeServicoRepository;
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    @Transactional
    public OrdemServicoAnexoResponseDTO salvar(Long osId, MultipartFile arquivo) {
        validarArquivo(arquivo);

        OrdemDeServico os = ordemDeServicoRepository.findById(osId)
                .orElseThrow(() -> new ResourceNotFoundException(osId));

        String nomeOriginal = limparNomeArquivo(arquivo.getOriginalFilename());
        String nomeArquivo = UUID.randomUUID() + extensao(nomeOriginal);
        Path pastaOs = uploadRoot.resolve("os").resolve(osId.toString()).normalize();
        Path destino = pastaOs.resolve(nomeArquivo).normalize();

        if (!destino.startsWith(uploadRoot)) {
            throw new DatabaseException("Caminho de arquivo invalido");
        }

        try {
            Files.createDirectories(pastaOs);
            Files.copy(arquivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new DatabaseException("Erro ao salvar arquivo da ordem de servico");
        }

        OrdemServicoAnexo anexo = new OrdemServicoAnexo();
        anexo.setOrdemDeServico(os);
        anexo.setNomeOriginal(nomeOriginal);
        anexo.setNomeArquivo(nomeArquivo);
        anexo.setContentType(arquivo.getContentType());
        anexo.setTamanho(arquivo.getSize());
        anexo.setCaminhoArquivo(destino.toString());
        anexo.setDataUpload(LocalDateTime.now());

        OrdemServicoAnexo anexoSalvo = anexoRepository.save(anexo);
        return new OrdemServicoAnexoResponseDTO(anexoSalvo);
    }

    public List<OrdemServicoAnexoResponseDTO> listarPorOs(Long osId) {
        if (!ordemDeServicoRepository.existsById(osId)) {
            throw new ResourceNotFoundException(osId);
        }
        return anexoRepository.findByOrdemDeServicoIdOrderByDataUploadDesc(osId)
                .stream()
                .map(OrdemServicoAnexoResponseDTO::new)
                .collect(Collectors.toList());
    }

    public OrdemServicoAnexo buscarEntidadePorId(Long anexoId) {
        return anexoRepository.findById(anexoId)
                .orElseThrow(() -> new ResourceNotFoundException(anexoId));
    }

    public Resource carregarArquivo(Long anexoId) {
        OrdemServicoAnexo anexo = buscarEntidadePorId(anexoId);

        try {
            Path arquivo = Paths.get(anexo.getCaminhoArquivo()).normalize();
            Resource resource = new UrlResource(arquivo.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new ResourceNotFoundException(anexoId);
            }
            return resource;
        } catch (MalformedURLException e) {
            throw new DatabaseException("Erro ao carregar arquivo da ordem de servico");
        }
    }

    @Transactional
    public void deletar(Long anexoId) {
        OrdemServicoAnexo anexo = buscarEntidadePorId(anexoId);

        try {
            Files.deleteIfExists(Paths.get(anexo.getCaminhoArquivo()));
        } catch (IOException e) {
            throw new DatabaseException("Erro ao deletar arquivo da ordem de servico");
        }

        anexoRepository.delete(anexo);
    }

    private void validarArquivo(MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new DatabaseException("Arquivo vazio");
        }

        if (!TIPOS_PERMITIDOS.contains(arquivo.getContentType())) {
            throw new DatabaseException("Tipo de arquivo nao permitido");
        }
    }

    private String limparNomeArquivo(String nomeOriginal) {
        if (nomeOriginal == null || nomeOriginal.isBlank()) {
            return "arquivo";
        }
        return Paths.get(nomeOriginal).getFileName().toString();
    }

    private String extensao(String nomeArquivo) {
        int index = nomeArquivo.lastIndexOf('.');
        if (index == -1) {
            return "";
        }
        return nomeArquivo.substring(index);
    }
}
