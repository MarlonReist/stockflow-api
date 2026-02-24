package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.Cliente;
import com.marlondev.stockflow.repositories.ClienteRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;


    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public void salvarCliente(Cliente cliente) {
        if(clienteRepository.findByCpf(cliente.getCpf()).isEmpty()) {
            cliente.setDataCadastro(LocalDate.now());
            clienteRepository.save(cliente);
        } else {
            throw new DatabaseException();
        }
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public void deletarClientePorId(Long id) {
        buscarPorId(id);
        clienteRepository.deleteById(id);
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public void atualizarCliente(Cliente cliente) {
        Cliente existente = clienteRepository.findById(cliente.getId())
                .orElseThrow(() -> new ResourceNotFoundException(cliente.getId()));
        Cliente outroCliente = clienteRepository.findByCpf(cliente.getCpf()).orElse(null);

        if (outroCliente == null || outroCliente.getId() == existente.getId()) {
            existente.setNome(cliente.getNome());
            existente.setCpf(cliente.getCpf());
            existente.setTelefone(cliente.getTelefone());
            existente.setEmail(cliente.getEmail());

            clienteRepository.save(existente);
        } else {
            throw new DatabaseException();
        }
    }

}
