package br.com.tech.challenge.services;

import java.util.List;
import java.util.Optional;

import br.com.tech.challenge.entities.Endereco;
import br.com.tech.challenge.repositories.EnderecoRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;

    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    @Transactional
    public Endereco salvarEndereco(Endereco endereco) {
        return enderecoRepository.save(endereco);
    }

    @Transactional
    public List<Endereco> listarEnderecos() {
        return enderecoRepository.findAll();
    }

    @Transactional
    public Optional<Endereco> buscarEnderecoPorId(Long id) {
        return enderecoRepository.findById(id);
    }

    @Transactional
    public Endereco atualizarEndereco(Long id, Endereco enderecoAtualizado) {
        Optional<Endereco> existenteOpt = enderecoRepository.findById(id);

        if (existenteOpt.isEmpty()) {
            throw new RuntimeException("Endereço com ID " + id + " não encontrado");
        }

        Endereco existente = existenteOpt.get();
        existente.setCep(enderecoAtualizado.getCep());
        existente.setLogradouro(enderecoAtualizado.getLogradouro());
        existente.setNumero(enderecoAtualizado.getNumero());
        existente.setBairro(enderecoAtualizado.getBairro());
        existente.setCidade(enderecoAtualizado.getCidade());

        return enderecoRepository.save(existente);
    }

    @Transactional
    public void deletarEndereco(Long id) {
        if (!enderecoRepository.existsById(id)) {
            throw new RuntimeException("Endereço com ID " + id + " não encontrado");
        }
        enderecoRepository.deleteById(id);
    }

}
