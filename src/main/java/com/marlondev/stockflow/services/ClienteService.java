package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.Cliente;
import com.marlondev.stockflow.dto.ClienteRequestDTO;
import com.marlondev.stockflow.dto.ClienteResponseDTO;
import com.marlondev.stockflow.repositories.ClienteRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;


    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public ClienteResponseDTO salvarCliente(ClienteRequestDTO dto) {
        if(clienteRepository.findByCpf(dto.getCpf()).isPresent()) {
            throw new DatabaseException();
        }
        Cliente cliente = new Cliente();
        cliente.setDataCadastro(LocalDate.now());
        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getCpf());
        cliente.setTelefone(dto.getTelefone());
        cliente.setEmail(dto.getEmail());
        cliente.setEndereco(dto.getEndereco());
        Cliente clienteSalvo = clienteRepository.save(cliente);
        return new ClienteResponseDTO(clienteSalvo);
    }

    public ClienteResponseDTO buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return new ClienteResponseDTO(cliente);
    }

    public void deletarClientePorId(Long id) {
        buscarPorId(id);
        clienteRepository.deleteById(id);
    }

    public List<ClienteResponseDTO> listarTodos() {
        List<Cliente> list = clienteRepository.findAll();
        List<ClienteResponseDTO> listDto = list.stream().map(ClienteResponseDTO::new).collect(Collectors.toList());
        return listDto;
    }

    public ClienteResponseDTO atualizarCliente(Long id, ClienteRequestDTO dto) {
        Cliente existente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        Cliente outroCliente = clienteRepository.findByCpf(dto.getCpf()).orElse(null);

        if (outroCliente == null || outroCliente.getId().equals(existente.getId())) {
            existente.setNome(dto.getNome());
            existente.setCpf(dto.getCpf());
            existente.setEndereco(dto.getEndereco());
            existente.setTelefone(dto.getTelefone());
            existente.setEmail(dto.getEmail());

            Cliente clienteSalvo = clienteRepository.save(existente);
            return new ClienteResponseDTO(clienteSalvo);
        }
            throw new DatabaseException();
        }
    }
