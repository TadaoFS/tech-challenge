package br.com.tech.challenge.services;


import br.com.tech.challenge.entities.Perfil;
import br.com.tech.challenge.entities.Usuario;
import br.com.tech.challenge.enums.PerfilEnum;
import br.com.tech.challenge.models.CustomUserDetails;
import br.com.tech.challenge.repositories.PerfilRepository;
import br.com.tech.challenge.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PerfilRepository perfilRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private Usuario usuario;
    private Perfil perfil;

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
    }

    @Test
    @DisplayName("Deve carregar o usuário e o perfil com sucesso")
    void shouldLoadUserAndProfileSuccessfully() {
        when(usuarioRepository.findByLogin("testuser")).thenReturn(Optional.of(usuario));
        when(perfilRepository.findByIdUsuario(usuario)).thenReturn(Optional.of(perfil));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertTrue(userDetails instanceof CustomUserDetails);
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        assertEquals("testuser", customUserDetails.getUsername());
        assertEquals("password", customUserDetails.getPassword());
        assertEquals(1, customUserDetails.getAuthorities().size());
        assertTrue(customUserDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("USER")));

        verify(usuarioRepository, times(1)).findByLogin("testuser");
        verify(perfilRepository, times(1)).findByIdUsuario(usuario);
    }

    @Test
    @DisplayName("Deve lançar UsernameNotFoundException quando o usuário não for encontrado")
    void shouldThrowUsernameNotFoundExceptionWhenUserNotFound() {
        when(usuarioRepository.findByLogin("nonexistent")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
                customUserDetailsService.loadUserByUsername("nonexistent")
        );

        assertEquals("Usuário não encontrado: nonexistent", exception.getMessage());
        verify(usuarioRepository, times(1)).findByLogin("nonexistent");
        verify(perfilRepository, never()).findByIdUsuario(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar UsernameNotFoundException quando o perfil não for encontrado para o usuário")
    void shouldThrowUsernameNotFoundExceptionWhenProfileNotFoundForUser() {
        when(usuarioRepository.findByLogin("testuser")).thenReturn(Optional.of(usuario));
        when(perfilRepository.findByIdUsuario(usuario)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
                customUserDetailsService.loadUserByUsername("testuser")
        );

        assertEquals("Perfil não encontrado para o usuário: testuser", exception.getMessage());
        verify(usuarioRepository, times(1)).findByLogin("testuser");
        verify(perfilRepository, times(1)).findByIdUsuario(usuario);
    }
}
