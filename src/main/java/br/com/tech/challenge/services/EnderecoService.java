package br.com.tech.challenge.services;

import br.com.tech.challenge.entities.Endereco;
import br.com.tech.challenge.entities.Usuario;
import br.com.tech.challenge.repositories.EnderecoRepository;
import br.com.tech.challenge.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;
    private final UsuarioRepository usuarioRepository;

    public EnderecoService(EnderecoRepository enderecoRepository, UsuarioRepository usuarioRepository) {
        this.enderecoRepository = enderecoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Endereco salvarEndereco(Endereco endereco) {
        Long usuarioId = endereco.getUsuario() != null ? endereco.getUsuario().getId() : null;

        if (usuarioId == null) {
            throw new RuntimeException("Usuário é obrigatório para salvar um endereço.");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário com ID " + usuarioId + " não encontrado."));

        
        endereco.setUsuario(usuario);
        usuario.setEndereco(endereco);

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
        Endereco existente = enderecoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Endereço com ID " + id + " não encontrado"));

        existente.setCep(enderecoAtualizado.getCep());
        existente.setLogradouro(enderecoAtualizado.getLogradouro());
        existente.setNumero(enderecoAtualizado.getNumero());
        existente.setBairro(enderecoAtualizado.getBairro());
        existente.setCidade(enderecoAtualizado.getCidade());
        existente.setEstado(enderecoAtualizado.getEstado());

        
        if (enderecoAtualizado.getUsuario() != null && enderecoAtualizado.getUsuario().getId() != null) {
            Usuario usuario = usuarioRepository.findById(enderecoAtualizado.getUsuario().getId())
                    .orElseThrow(() -> new RuntimeException("Usuário com ID " + enderecoAtualizado.getUsuario().getId() + " não encontrado."));

            existente.setUsuario(usuario);
            usuario.setEndereco(existente);
        }

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
