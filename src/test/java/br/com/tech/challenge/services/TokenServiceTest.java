package br.com.tech.challenge.services;

import br.com.tech.challenge.entities.Perfil;
import br.com.tech.challenge.entities.Usuario;
import br.com.tech.challenge.enums.PerfilEnum;
import br.com.tech.challenge.models.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    private static final String TEST_SECRET_KEY = "c2VjcmV0S2V5Rm9ySldUVGVzdGluZ0FwcGxpY2F0aW9uMTIzNDU2Nzg5MDEyMzQ1Njc4OTA=";
    private static final Long TEST_EXPIRATION_TIME = 3600L;

    private TokenService tokenService;

    private Usuario usuario;
    private Perfil perfil;
    private CustomUserDetails customUserDetails;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService(TEST_SECRET_KEY, TEST_EXPIRATION_TIME);
        usuario = Usuario.builder()
                .id(1L)
                .login("userTst")
                .build();
        perfil = Perfil.builder()
                .idUsuario(usuario)
                .tipo(PerfilEnum.ADMIN)
                .build();

        customUserDetails = new CustomUserDetails(usuario, perfil);
    }


    @Test
    @DisplayName("Deve retornar um token jwt correto")
    void deveRetornarUmTokenJwtCorreto() {

        var token = tokenService.gerarToken(usuario, perfil);
        assertNotNull(token);
        assertFalse(token.isEmpty());

        String extrairLogin = tokenService.extrairLogin(token);
        assertEquals(usuario.getLogin(), extrairLogin);

        assertTrue(tokenService.validarToken(token));
    }

    @Test
    @DisplayName("Deve retornar false para token inválido")
    void deveRetornarFalseParaTokenInvalido() {
        String validToken = tokenService.gerarToken(usuario, perfil);
        String invalidToken = validToken.substring(0, validToken.length() - 5) + "ABCDE";
        assertFalse(tokenService.validarToken(invalidToken));
    }

    @Test
    @DisplayName("Deve retornar false para token expirado")
    void deveRetornarFalseParaTokenExpirado() throws InterruptedException {
        TokenService expiredTokenService = new TokenService(TEST_SECRET_KEY, 1L);
        String token = expiredTokenService.gerarToken(usuario, perfil);
        Thread.sleep(1500);
        assertFalse(expiredTokenService.validarToken(token));
    }

    @Test
    @DisplayName("Deve retornar true para um token válido e UserDetails correspondente")
    void shouldReturnTrueForValidTokenAndMatchingUserDetails() {
        String token = tokenService.gerarToken(usuario, perfil);
        assertTrue(tokenService.validarToken(token, customUserDetails));
    }

    @Test
    @DisplayName("Deve retornar false para um token válido mas UserDetails não correspondente")
    void shouldReturnFalseForValidTokenAndNonMatchingUserDetails() {
        String token = tokenService.gerarToken(usuario, perfil);

        Usuario usuarioDiferente = Usuario.builder()
                .id(2L)
                .login("differentUser")
                .build();
        UserDetails userDetails = new CustomUserDetails(usuarioDiferente, perfil);

        assertFalse(tokenService.validarToken(token, userDetails));
    }

    @Test
    @DisplayName("Deve retornar false para um token expirado, mesmo com UserDetails correspondente")
    void shouldReturnFalseForExpiredTokenWithMatchingUserDetails() throws InterruptedException {
        TokenService expiredTokenService = new TokenService(TEST_SECRET_KEY, 1L);
        String token = expiredTokenService.gerarToken(usuario, perfil);
        Thread.sleep(1500);
        assertFalse(expiredTokenService.validarToken(token, customUserDetails));
    }

    @Test
    @DisplayName("Deve retornar false para um token inválido (assinatura incorreta) com UserDetails")
    void shouldReturnFalseForInvalidTokenWithUserDetails() {
        String validToken = tokenService.gerarToken(usuario, perfil);
        String invalidToken = validToken.substring(0, validToken.length() - 5) + "FGHIJ";
        assertFalse(tokenService.validarToken(invalidToken, customUserDetails));
    }
}
