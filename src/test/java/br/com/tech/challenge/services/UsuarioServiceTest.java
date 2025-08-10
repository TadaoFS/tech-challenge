package br.com.tech.challenge.services;

import br.com.tech.challenge.entities.Perfil;
import br.com.tech.challenge.entities.Usuario;
import br.com.tech.challenge.enums.PerfilEnum;
import br.com.tech.challenge.repositories.EnderecoRepository;
import br.com.tech.challenge.repositories.PerfilRepository;
import br.com.tech.challenge.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataRetrievalFailureException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private EnderecoRepository enderecoRepository;
    @Mock
    private PerfilRepository perfilRepository;

    @InjectMocks
    private UsuarioService usuarioService;


    @Test
    void buscarUsuarioSucesso() {
        //given
        long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome("Teste");

        //when
        Mockito.when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        Usuario result = usuarioService.buscarUsuario(id);

        //then
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Teste", result.getNome());
    }

    @Test
    void buscarUsuarioNotFound() {
        //given
        long id = 1L;

        //when
        Mockito.when(usuarioRepository.findById(id)).thenReturn(Optional.empty());
        Exception exception = assertThrows(DataRetrievalFailureException.class, () -> usuarioService.buscarUsuario(id));

        //then
        assertEquals("Usuário não encontrado", exception.getMessage());
    }

    @Test
    void inserirUsuarioSucesso() {
        //given
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Novo Usuario");
        usuario.setEmail("email@teste.com");

        Perfil perfil = new Perfil();
        perfil.setIdUsuario(usuario);
        perfil.setTipo(PerfilEnum.USER);

        //when
        Mockito.when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        Mockito.when(perfilRepository.save(any(Perfil.class))).thenReturn(perfil);
        Usuario result = usuarioService.inserirUsuario(usuario);

        //then
        assertNotNull(result);
        assertEquals(usuario.getId(), result.getId());
    }

    @Test
    void editarUsuarioSucesso() {
        //given
        long id = 1L;
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(id);
        usuarioExistente.setEmail("email@teste.com");

        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setEmail("novoemail@teste.com");

        //when
        Mockito.when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));
        Mockito.when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioAtualizado);
        String result = usuarioService.editarUsuario(id, usuarioAtualizado);

        //then
        assertNotNull(result);
        assertEquals("Usuário novoemail@teste.com alterado com sucesso!", result);
    }

    @Test
    void editarUsuarioNotFound() {
        //given
        long id = 1L;
        Usuario usuario = new Usuario();

        //when
        Mockito.when(usuarioRepository.findById(id)).thenReturn(Optional.empty());
        Exception exception = assertThrows(DataRetrievalFailureException.class, () -> usuarioService.editarUsuario(id, usuario));

        //then
        assertEquals("Usuário não encontrado", exception.getMessage());
    }

    @Test
    void deletarUsuarioSucesso() {
        long id = 1L;
        //given
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Novo Usuario");
        usuario.setEmail("email@teste.com");

        //when
        Mockito.when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        String result = usuarioService.deletarUsuario(id);

        //then
        assertNotNull(result);
        assertEquals("Usuário deletado com sucesso!", result);
    }

    @Test
    void alterarSenhaSucesso() {
        //given
        long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setSenha("senhaAntiga");

        //when
        Mockito.when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        Mockito.when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        String result = usuarioService.alterarSenha(id, "novaSenha");

        //then
        assertNotNull(result);
        assertEquals("Senha alterada com sucesso!", result);
    }

    @Test
    void alterarSenhaError() {
        //given
        long id = 1L;

        //when
        Mockito.when(usuarioRepository.findById(id)).thenReturn(Optional.empty());
        Exception exception = assertThrows(DataRetrievalFailureException.class, () -> usuarioService.alterarSenha(id, "novaSenha"));

        //then
        assertEquals("Usuário não encontrado", exception.getMessage());
    }
}
