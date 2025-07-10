package br.com.tech.challenge.controllers;

import br.com.tech.challenge.controllers.auth.AutenticacaoController;
import br.com.tech.challenge.controllers.auth.request.LoginRequest;
import br.com.tech.challenge.controllers.auth.response.LoginResponse;
import br.com.tech.challenge.services.AutenticacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AutenticacaoControllerTest {

    @Mock
    private AutenticacaoService autenticacaoService;

    @InjectMocks
    private AutenticacaoController autenticacaoController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(autenticacaoController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Deve realizar login com sucesso e retornar token")
    void deveRealizarLoginComSucessoERetornarToken() throws Exception {
        LoginRequest request = new LoginRequest("usuario@teste.com", "senha123");
        LoginResponse expectedResponse = new LoginResponse("mocked-jwt-token");

        when(autenticacaoService.login(any(LoginRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar login com credenciais inválidas")
    void deveRetornarErroAoTentarLoginComCredenciaisInvalidas() {

        LoginRequest request = new LoginRequest("usuario@teste.com", "senhaInvalida");

        when(autenticacaoService.login(any(LoginRequest.class)))
                .thenThrow(new RuntimeException("Senha incorreta"));

        assertThrows(ServletException.class, () ->
                mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))));
    }
}
