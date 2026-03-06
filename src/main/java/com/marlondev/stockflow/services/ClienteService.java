package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.Cliente;
import com.marlondev.stockflow.dto.ClienteRequestDTO;
import com.marlondev.stockflow.dto.ClienteResponseDTO;
import com.marlondev.stockflow.repositories.ClienteRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
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
    public ClienteResponseDTO salvarCliente(Cliente cliente) {
        if(clienteRepository.findByCpf(cliente.getCpf()).isPresent()) {
            throw new DatabaseException();
        }
            cliente.setDataCadastro(LocalDate.now());
            clienteRepository.save(cliente);
            return new ClienteResponseDTO(cliente);
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

    public ClienteResponseDTO atualizarCliente(Cliente cliente) {
        Cliente existente = clienteRepository.findById(cliente.getId())
                .orElseThrow(() -> new ResourceNotFoundException(cliente.getId()));
        Cliente outroCliente = clienteRepository.findByCpf(cliente.getCpf()).orElse(null);

        if (outroCliente == null || outroCliente.getId().equals(existente.getId())) {
            existente.setNome(cliente.getNome());
            existente.setCpf(cliente.getCpf());
            existente.setEndereco(cliente.getEndereco());
            existente.setTelefone(cliente.getTelefone());
            existente.setEmail(cliente.getEmail());

            clienteRepository.save(existente);
            return new ClienteResponseDTO(existente);
        } else {
            throw new DatabaseException();
        }
    }

}
