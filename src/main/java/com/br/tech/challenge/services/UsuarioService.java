package com.br.tech.challenge.services;

import com.br.tech.challenge.entities.Usuario;
import com.br.tech.challenge.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Usuario buscarUsuario(long id){
        try{
            return this.usuarioRepository.findById(id)
                    .orElseThrow(()-> new DataRetrievalFailureException(
                    "Usuário não encontrado"));
        } catch (DataRetrievalFailureException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException("Erro ao buscar o usuário."){};
        }
    }

    @Transactional
    public String inserirUsuario(Usuario usuario){
        try{
            Usuario usr = this.usuarioRepository.save(usuario);
            return String.format("Usuário %s com email: %s, salvo com sucesso!",
                    usr.getNome(), usr.getEmail());
        } catch (Exception e) {
            throw new DataAccessException("Erro ao gravar o usuário."){};
        }
    }

    public String editarUsuario(Long id, Usuario usuario){
        try{
            Usuario usr = this.buscarUsuario(id);

            usr.setNome(usuario.getNome());
            usr.setLogin(usuario.getLogin());
            usr.setEmail(usuario.getEmail());
            usr.setSenha(usuario.getSenha());
            usr.setDataAlteracao(usuario.getDataAlteracao());
            usr.setEndereco(usuario.getEndereco());

            this.inserirUsuario(usr);
            return String.format("Usuário %s alterado com sucesso!", usuario.getEmail());
        }catch (Exception e){
            throw new DataAccessException("Erro ao alterar o usuário."){};
        }
    }

    @Transactional
    public String deletarUsuario(long id){
        try{
            if(this.usuarioRepository.existsById(id)){
                this.usuarioRepository.deleteById(id);
                return "Usuário deletado com sucesso!";
            }
            return "Usuário não encontrado para deleção.";
        } catch (Exception e) {
            throw new DataAccessException("Erro ao deletar o usuário."){};
        }
    }
}
