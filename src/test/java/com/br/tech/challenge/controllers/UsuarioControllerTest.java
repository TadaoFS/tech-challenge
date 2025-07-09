package com.br.tech.challenge.controllers;

import com.br.tech.challenge.entities.Usuario;
import com.br.tech.challenge.services.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataRetrievalFailureException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    @Test
    void getUsuarioSucesso() {
        //given
        long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome("Teste");

        //when
        when(usuarioService.buscarUsuario(id)).thenReturn(usuario);
        Usuario result = usuarioController.getUsuario(id);

        //then
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Teste", result.getNome());
    }

    @Test
    void getUsuarioNotFound() {
        //given
        long id = 1L;

        //when
        when(usuarioService.buscarUsuario(id)).thenThrow(
                new DataRetrievalFailureException("Usuário não encontrado")
        );
        Exception exception = assertThrows(RuntimeException.class, () -> usuarioController.getUsuario(id));

        //then
        assertEquals("Usuário não encontrado", exception.getMessage());
    }

    @Test
    void postUsuarioSucesso() {
        //given
        Usuario usuario = new Usuario();
        usuario.setNome("Novo Usuario");
        usuario.setEmail("Novo Email");

        //when
        when(usuarioService.inserirUsuario(any(Usuario.class)))
                .thenReturn(String.format("Usuário %s com email: %s, salvo com sucesso!",
                        usuario.getNome(), usuario.getEmail()));
        String result = usuarioController.postUsuario(usuario);

        //then
        assertNotNull(result);
        assertEquals(String.format("Usuário %s com email: %s, salvo com sucesso!",
                usuario.getNome(), usuario.getEmail()), result);
    }

    @Test
    void postUsuarioError() {
        //given
        Usuario usuario = new Usuario();
        usuario.setNome("Novo Usuario");

        //when
        when(usuarioService.inserirUsuario(any(Usuario.class)))
                .thenThrow(new RuntimeException("Erro ao salvar usuário"));
        Exception exception = assertThrows(RuntimeException.class, () -> usuarioController.postUsuario(usuario));

        //then
        assertEquals("Erro ao salvar usuário", exception.getMessage());
    }

    @Test
    void putUsuarioSucesso() {
        //given
        long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setEmail("NovoEmail@email.com");

        //when
        when(usuarioService.editarUsuario(eq(id), any(Usuario.class)))
                .thenReturn(String.format("Usuário %s alterado com sucesso!", usuario.getEmail()));
        String result = usuarioController.putUsuario(id, usuario);

        //then
        assertNotNull(result);
        assertEquals(String.format("Usuário %s alterado com sucesso!", usuario.getEmail()), result);
    }

    @Test
    void putUsuarioError() {
        //given
        long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setNome("Usuario Atualizado");

        //when
        when(usuarioService.editarUsuario(eq(id), any(Usuario.class)))
                .thenThrow(new RuntimeException("Erro ao atualizar usuário"));
        Exception exception = assertThrows(RuntimeException.class, () -> usuarioController.putUsuario(id, usuario));

        //then
        assertEquals("Erro ao atualizar usuário", exception.getMessage());
    }

    @Test
    void patchUsuarioSenhaSucesso() {
        //given
        long id = 1L;

        //when
        when(usuarioService.alterarSenha(eq(id), eq("novaSenha"))).thenReturn("Senha alterada com sucesso");
        String result = usuarioController.patchUsuarioSenha(id, "novaSenha");

        //then
        assertNotNull(result);
        assertEquals("Senha alterada com sucesso", result);
    }

    @Test
    void patchUsuarioSenhaError() {
        //given
        long id = 1L;

        //when
        when(usuarioService.alterarSenha(eq(id), eq("novaSenha")))
                .thenThrow(new RuntimeException("Erro ao alterar senha"));
        Exception exception = assertThrows(RuntimeException.class, () -> usuarioController.patchUsuarioSenha(id, "novaSenha"));

        //then
        assertEquals("Erro ao alterar senha", exception.getMessage());
    }

    @Test
    void deleteUsuarioSucesso() {
        //given
        long id = 1L;

        //when
        when(usuarioService.deletarUsuario(id)).thenReturn("Usuario deletado com sucesso");
        String result = usuarioController.deleteUsuario(id);

        //then
        assertNotNull(result);
        assertEquals("Usuario deletado com sucesso", result);
    }

    @Test
    void deleteUsuarioError() {
        //given
        long id = 1L;

        //when
        when(usuarioService.deletarUsuario(id))
                .thenThrow(new RuntimeException("Erro ao deletar usuário"));
        Exception exception = assertThrows(RuntimeException.class, () -> usuarioController.deleteUsuario(id));

        //then
        assertEquals("Erro ao deletar usuário", exception.getMessage());
    }
}
