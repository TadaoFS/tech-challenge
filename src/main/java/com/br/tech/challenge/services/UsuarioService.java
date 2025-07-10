package com.br.tech.challenge.services;

import com.br.tech.challenge.entities.Usuario;
import com.br.tech.challenge.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Usuario buscarUsuario(long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new DataRetrievalFailureException("Usuário não encontrado"));
    }

    @Transactional
    public String inserirUsuario(Usuario usuario) {
        Usuario usr = usuarioRepository.save(usuario);
        return String.format("Usuário %s com email: %s, salvo com sucesso!", usr.getNome(), usr.getEmail());
    }

    public String editarUsuario(Long id, Usuario usuario) {
        Usuario usr = buscarUsuario(id);
        usr.setNome(usuario.getNome());
        usr.setLogin(usuario.getLogin());
        usr.setEmail(usuario.getEmail());
        usr.setSenha(usuario.getSenha());
        usr.setDataAlteracao(usuario.getDataAlteracao());
        usr.setEndereco(usuario.getEndereco());
        usuarioRepository.save(usr);
        return String.format("Usuário %s alterado com sucesso!", usuario.getEmail());
    }

    @Transactional
    public String deletarUsuario(long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
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
}
