package br.com.tech.challenge.services;

import br.com.tech.challenge.controllers.auth.request.LoginRequest;
import br.com.tech.challenge.controllers.auth.response.LoginResponse;
import br.com.tech.challenge.entities.Perfil;
import br.com.tech.challenge.entities.Usuario;
import br.com.tech.challenge.enums.PerfilEnum;
import br.com.tech.challenge.repositories.PerfilRepository;
import br.com.tech.challenge.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AutenticacaoServiceTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private PerfilRepository perfilRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AutenticacaoService autenticacaoService;

    private Usuario usuario;
    private Perfil perfil;
    private LoginRequest loginRequest;
    private final String TOKEN_FICTICIO = "tokenFicticioGerado";

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .id(1L)
                .login("testuser")
                .senha("password")
                .build();

        perfil = Perfil.builder()
                .id(1L)
                .idUsuario(usuario)
                .tipo(PerfilEnum.USER)
                .build();

        loginRequest = new LoginRequest("testUser", "password");
    }

    @Test
    @DisplayName("Deve retornar LoginResponse com token quando o login for bem-sucedido")
    void deveRetornarLoginResponseComTokenQuandoLoginBemSucedido() {
        when(usuarioRepository.findByLogin(loginRequest.login())).thenReturn(Optional.of(usuario));
        when(perfilRepository.findByIdUsuario(usuario)).thenReturn(Optional.of(perfil));
        when(tokenService.gerarToken(usuario, perfil)).thenReturn(TOKEN_FICTICIO);

        LoginResponse response = autenticacaoService.login(loginRequest);

        assertNotNull(response);
        assertEquals(TOKEN_FICTICIO, response.token());
        assertEquals("Bearer", response.tokenType());

        verify(usuarioRepository, times(1)).findByLogin(loginRequest.login());
        verify(perfilRepository, times(1)).findByIdUsuario(usuario);
        verify(tokenService, times(1)).gerarToken(usuario, perfil);
    }

    @Test
    @DisplayName("Deve lançar RuntimeException quando o usuário não for encontrado")
    void deveLancarRuntimeExceptionQuandoUsuarioNaoEncontrado() {
        when(usuarioRepository.findByLogin(loginRequest.login())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                autenticacaoService.login(loginRequest)
        );

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(usuarioRepository, times(1)).findByLogin(loginRequest.login());
        verify(perfilRepository, never()).findByIdUsuario(any());
        verify(tokenService, never()).gerarToken(any(), any());
    }

    @Test
    @DisplayName("Deve lançar RuntimeException quando o perfil do usuário não for encontrado")
    void deveLancarRuntimeExceptionQuandoPerfilNaoEncontrado() {
        when(usuarioRepository.findByLogin(loginRequest.login())).thenReturn(Optional.of(usuario));
        when(perfilRepository.findByIdUsuario(usuario)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                autenticacaoService.login(loginRequest)
        );

        assertEquals("Perfil não encontrado", exception.getMessage());
        verify(usuarioRepository, times(1)).findByLogin(loginRequest.login());
        verify(perfilRepository, times(1)).findByIdUsuario(usuario);
        verify(tokenService, never()).gerarToken(any(), any());
    }

    @Test
    @DisplayName("Deve lançar RuntimeException quando a senha estiver incorreta")
    void deveLancarRuntimeExceptionQuandoSenhaIncorreta() {
        LoginRequest wrongPasswordRequest = new LoginRequest("testUser", "wrongPass");

        when(usuarioRepository.findByLogin(wrongPasswordRequest.login())).thenReturn(Optional.of(usuario));
        when(perfilRepository.findByIdUsuario(usuario)).thenReturn(Optional.of(perfil));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                autenticacaoService.login(wrongPasswordRequest)
        );

        assertEquals("Senha incorreta", exception.getMessage());
        verify(usuarioRepository, times(1)).findByLogin(wrongPasswordRequest.login());
        verify(perfilRepository, times(1)).findByIdUsuario(usuario);
        verify(tokenService, never()).gerarToken(any(), any());
    }
}
