package br.com.tech.challenge.controllers;

import br.com.tech.challenge.config.JwtFilter;
import br.com.tech.challenge.entities.Endereco;
import br.com.tech.challenge.entities.Usuario;
import br.com.tech.challenge.exception.EnderecoNaoEncontrado;
import br.com.tech.challenge.services.EnderecoService;
import br.com.tech.challenge.services.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = EnderecoController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtFilter.class)
)
@AutoConfigureMockMvc(addFilters = false)
class EnderecoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnderecoService enderecoService;

    // Mock dos beans de segurança que causavam problema
    @MockBean
    private TokenService tokenService;

    @MockBean
    private JwtFilter jwtFilter;

    private Endereco endereco;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        endereco = new Endereco();
        endereco.setId(1L);
        endereco.setCep("12345678");
        endereco.setLogradouro("Rua Teste");
        endereco.setNumero("100");
        endereco.setBairro("Centro");
        endereco.setCidade("Cidade");
        endereco.setEstado("UF");
    }

    @Test
    void listarTodosSucesso() throws Exception {
        when(enderecoService.listarEnderecos())
                .thenReturn(Collections.singletonList(endereco));

        mockMvc.perform(get("/v1/enderecos"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].cep").value("12345678"))
                .andExpect(jsonPath("$[0].logradouro").value("Rua Teste"));
    }

    @Test
    void buscarPorIdSucesso() throws Exception {
        when(enderecoService.buscarEnderecoPorId(1L))
                .thenReturn(Optional.of(endereco));

        mockMvc.perform(get("/v1/enderecos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cep").value("12345678"));
    }

    @Test
    void buscarPorIdNaoEncontrado() throws Exception {
        when(enderecoService.buscarEnderecoPorId(999L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/v1/enderecos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void criarEnderecoSucesso() throws Exception {
        when(enderecoService.salvarEndereco(any(Endereco.class)))
                .thenReturn(endereco);

        String json = """
                {
                  "cep": "12345678",
                  "logradouro": "Rua Teste",
                  "numero": "100",
                  "bairro": "Centro",
                  "cidade": "Cidade",
                  "estado": "UF"
                }
                """;

        mockMvc.perform(post("/v1/enderecos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cep").value("12345678"));
    }

    @Test
    void atualizarEnderecoSucesso() throws Exception {
        Endereco enderecoAtualizado = new Endereco();
        enderecoAtualizado.setId(1L);
        enderecoAtualizado.setCep("87654321");
        enderecoAtualizado.setLogradouro("Rua Atualizada");
        enderecoAtualizado.setNumero("200");
        enderecoAtualizado.setBairro("Bairro Novo");
        enderecoAtualizado.setCidade("Cidade");
        enderecoAtualizado.setEstado("UF");

        when(enderecoService.atualizarEndereco(eq(1L), any(Endereco.class)))
                .thenReturn(enderecoAtualizado);

        String json = """
                {
                  "cep": "87654321",
                  "logradouro": "Rua Atualizada",
                  "numero": "200",
                  "bairro": "Bairro Novo",
                  "cidade": "Cidade",
                  "estado": "UF"
                }
                """;

        mockMvc.perform(put("/v1/enderecos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cep").value("87654321"))
                .andExpect(jsonPath("$.logradouro").value("Rua Atualizada"));
    }

    @Test
    void atualizarEnderecoNaoEncontrado() throws Exception {
        when(enderecoService.atualizarEndereco(eq(999L), any(Endereco.class)))
                .thenThrow(new EnderecoNaoEncontrado("Endereço não encontrado"));

        String json = """
                {
                  "cep": "87654321",
                  "logradouro": "Rua Atualizada",
                  "numero": "200",
                  "bairro": "Bairro Novo",
                  "cidade": "Cidade",
                  "estado": "UF"
                }
                """;

        mockMvc.perform(put("/v1/enderecos/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletarEnderecoSucesso() throws Exception {
        Mockito.doNothing().when(enderecoService).deletarEndereco(1L);

        mockMvc.perform(delete("/v1/enderecos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletarEnderecoNaoEncontrado() throws Exception {
        Mockito.doThrow(new EnderecoNaoEncontrado("Endereço não encontrado"))
                .when(enderecoService).deletarEndereco(999L);

        mockMvc.perform(delete("/v1/enderecos/999"))
                .andExpect(status().isNotFound());
    }
}
