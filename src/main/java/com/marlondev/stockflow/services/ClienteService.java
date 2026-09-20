package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.Cliente;
import com.marlondev.stockflow.dto.ClienteRequestDTO;
import com.marlondev.stockflow.dto.ClienteResponseDTO;
import com.marlondev.stockflow.repositories.ClienteRepository;
import com.marlondev.stockflow.domain.enums.TipoPessoaEnum;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;


    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    private void validarCliente(ClienteRequestDTO dto, Long idAtual) {
        if (dto.getTipoPessoa() == TipoPessoaEnum.FISICA) {
            validarPessoaFisica(dto, idAtual);
            return;
        }

        if (dto.getTipoPessoa() == TipoPessoaEnum.JURIDICA) {
            validarPessoaJuridica(dto, idAtual);
            return;
        }

        throw new DatabaseException("Tipo de pessoa inválido!");
    }

    private void validarPessoaFisica(ClienteRequestDTO dto, Long idAtual) {
        if (dto.getCpf() == null || dto.getCpf().isBlank()) {
            throw new DatabaseException("CPF é obrigatório para pessoa física!");
        }

        CPFValidator cpfValidator = new CPFValidator();
        cpfValidator.initialize(null);

        if (!cpfValidator.isValid(dto.getCpf(), null)) {
            throw new DatabaseException("CPF é inválido!");
        }

        Cliente outroCliente = clienteRepository.findByCpf(dto.getCpf()).orElse(null);

        if (outroCliente != null && !outroCliente.getId().equals(idAtual)) {
            throw new DatabaseException("Esse CPF já existe!");
        }
    }

    private boolean cnpjValido(String cnpj) {
        String numeros = cnpj.replaceAll("\\D", "");

        if (numeros.length() != 14) {
            return false;
        }

        if (numeros.chars().distinct().count() == 1) {
            return false;
        }

        int primeiroDigito = calcularDigitoCnpj(numeros.substring(0, 12), new int[]{5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});
        int segundoDigito = calcularDigitoCnpj(numeros.substring(0, 12) + primeiroDigito, new int[]{6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});

        return numeros.equals(numeros.substring(0, 12) + primeiroDigito + segundoDigito);
    }

    private int calcularDigitoCnpj(String base, int[] pesos) {
        int soma = 0;

        for (int i = 0; i < base.length(); i++) {
            soma += Character.getNumericValue(base.charAt(i)) * pesos[i];
        }

        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }

    private void validarPessoaJuridica(ClienteRequestDTO dto, Long idAtual) {
        if (dto.getCnpj() == null || dto.getCnpj().isBlank()) {
            throw new DatabaseException("CNPJ é obrigatório para pessoa jurídica!");
        }

        if (dto.getResponsavelContato() == null || dto.getResponsavelContato().isBlank()) {
            throw new DatabaseException("Responsável/contato é obrigatório para pessoa jurídica!");
        }

        if (!cnpjValido(dto.getCnpj())) {
            throw new DatabaseException("CNPJ é inválido!");
        }

        Cliente outroCliente = clienteRepository.findByCnpj(dto.getCnpj()).orElse(null);

        if (outroCliente != null && !outroCliente.getId().equals(idAtual)) {
            throw new DatabaseException("Esse CNPJ já existe!");
        }
    }

    @Transactional
    public ClienteResponseDTO salvarCliente(ClienteRequestDTO dto) {
        validarCliente(dto, null);

        Cliente cliente = new Cliente();
        cliente.setDataCadastro(LocalDate.now());
        cliente.setTipoPessoa(dto.getTipoPessoa());
        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getTipoPessoa() == TipoPessoaEnum.FISICA ? dto.getCpf() : null);
        cliente.setCnpj(dto.getTipoPessoa() == TipoPessoaEnum.JURIDICA ? dto.getCnpj() : null);
        cliente.setTelefone(dto.getTelefone());
        cliente.setEmail(dto.getEmail());
        cliente.setEndereco(dto.getEndereco());
        cliente.setResponsavelContato(dto.getTipoPessoa() == TipoPessoaEnum.JURIDICA ? dto.getResponsavelContato() : null);

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
        return list.stream().map(ClienteResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public ClienteResponseDTO atualizarCliente(Long id, ClienteRequestDTO dto) {
        Cliente existente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        validarCliente(dto, id);

        existente.setTipoPessoa(dto.getTipoPessoa());
        existente.setNome(dto.getNome());
        existente.setCpf(dto.getTipoPessoa() == TipoPessoaEnum.FISICA ? dto.getCpf() : null);
        existente.setCnpj(dto.getTipoPessoa() == TipoPessoaEnum.JURIDICA ? dto.getCnpj() : null);
        existente.setEndereco(dto.getEndereco());
        existente.setTelefone(dto.getTelefone());
        existente.setEmail(dto.getEmail());
        existente.setResponsavelContato(dto.getTipoPessoa() == TipoPessoaEnum.JURIDICA ? dto.getResponsavelContato() : null);

        Cliente clienteSalvo = clienteRepository.save(existente);
        return new ClienteResponseDTO(clienteSalvo);
    }
}
