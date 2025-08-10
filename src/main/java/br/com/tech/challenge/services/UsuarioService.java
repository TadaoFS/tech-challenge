package br.com.tech.challenge.services;

import br.com.tech.challenge.entities.Perfil;
import br.com.tech.challenge.entities.Usuario;
import br.com.tech.challenge.enums.PerfilEnum;
import br.com.tech.challenge.repositories.EnderecoRepository;
import br.com.tech.challenge.repositories.PerfilRepository;
import br.com.tech.challenge.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UsuarioService {

    private final EnderecoRepository enderecoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;

    public UsuarioService(EnderecoRepository enderecoRepository, UsuarioRepository usuarioRepository, PerfilRepository perfilRepository) {
        this.enderecoRepository = enderecoRepository;
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
    }

    @Transactional
    public Usuario buscarUsuario(long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new DataRetrievalFailureException("Usuário não encontrado"));
    }

    @Transactional
    public Usuario inserirUsuario(Usuario usuario) {
        if(Objects.isNull(usuario.getDataAtualizacao()))
            usuario.setDataAtualizacao(Instant.now().toString());
        var result = usuarioRepository.save(usuario);
        var perfil = perfilRepository.save(Perfil.builder()
                .idUsuario(result)
                .tipo(PerfilEnum.USER)
                .build());
        return Optional.of(perfil)
                .map(Perfil::getIdUsuario)
                .orElseThrow(() -> new RuntimeException("Erro ao inserir usuário"));
    }

    public String editarUsuario(Long id, Usuario usuario) {
        Usuario usr = buscarUsuario(id);
        usr.setNome(usuario.getNome());
        usr.setLogin(usuario.getLogin());
        usr.setEmail(usuario.getEmail());
        usr.setSenha(usuario.getSenha());
        usr.setEndereco(usuario.getEndereco());
        usuarioRepository.save(usr);
        return String.format("Usuário %s alterado com sucesso!", usuario.getEmail());
    }

    @Transactional
    public String deletarUsuario(long id) {
        var usuario = buscarUsuario(id);
        if (!Objects.isNull(usuario)) {
            perfilRepository.deleteByIdUsuario(usuario);
            enderecoRepository.deleteByUsuario(usuario);
            usuarioRepository.delete(usuario);
            return "Usuário deletado com sucesso!";
        }
        return "Usuário não encontrado para deleção.";
    }

    @Transactional
    public String alterarSenha(long id, String novaSenha) {
        Usuario usuario = buscarUsuario(id);
        if (novaSenha == null || novaSenha.isEmpty()) {
            throw new IllegalArgumentException("A nova senha não pode ser nula ou vazia.");
        }
        usuario.setSenha(novaSenha);
        usuarioRepository.save(usuario);
        return "Senha alterada com sucesso!";
    }

    public List<Usuario> buscarUsuarios() {
        return usuarioRepository.findAll();
    }
}
