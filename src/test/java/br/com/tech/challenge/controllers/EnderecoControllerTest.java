package br.com.tech.challenge.controllers;

import br.com.tech.challenge.entities.Endereco;
import br.com.tech.challenge.services.EnderecoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnderecoController.class)
public class EnderecoControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private EnderecoService enderecoService;

        private Endereco endereco;

        @BeforeEach
        void setUp() {
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
        void listarTodos_sucesso() throws Exception {
                Mockito.when(enderecoService.listarEnderecos())
                                .thenReturn(Arrays.asList(endereco));

                mockMvc.perform(get("/endereco"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(1)))
                                .andExpect(jsonPath("$[0].cep").value("12345678"));
        }

        @Test
        void buscarPorId_sucesso() throws Exception {
                Mockito.when(enderecoService.buscarEnderecoPorId(1L))
                                .thenReturn(Optional.of(endereco));

                mockMvc.perform(get("/endereco/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.cep").value("12345678"));
        }

        @Test
        void buscarPorId_naoEncontrado() throws Exception {
                Mockito.when(enderecoService.buscarEnderecoPorId(999L))
                                .thenReturn(Optional.empty());

                mockMvc.perform(get("/endereco/999"))
                                .andExpect(status().isNotFound());
        }

        @Test
        void criarEndereco_sucesso() throws Exception {
                Mockito.when(enderecoService.salvarEndereco(any(Endereco.class)))
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

                mockMvc.perform(post("/endereco")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.cep").value("12345678"));
        }

        @Test
        void atualizarEndereco_sucesso() throws Exception {
                Endereco enderecoAtualizado = new Endereco();
                enderecoAtualizado.setId(1L);
                enderecoAtualizado.setCep("87654321");
                enderecoAtualizado.setLogradouro("Rua Atualizada");
                enderecoAtualizado.setNumero("200");
                enderecoAtualizado.setBairro("Bairro Novo");
                enderecoAtualizado.setCidade("Cidade");
                enderecoAtualizado.setEstado("UF");

                Mockito.when(enderecoService.atualizarEndereco(eq(1L), any(Endereco.class)))
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

                mockMvc.perform(put("/endereco/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.cep").value("87654321"))
                                .andExpect(jsonPath("$.logradouro").value("Rua Atualizada"));
        }

        @Test
        void atualizarEndereco_naoEncontrado() throws Exception {
                Mockito.when(enderecoService.atualizarEndereco(eq(999L), any(Endereco.class)))
                                .thenThrow(new RuntimeException("Endereço não encontrado"));

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

                mockMvc.perform(put("/endereco/999")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                                .andExpect(status().isNotFound());
        }

        @Test
        void deletarEndereco_sucesso() throws Exception {
                Mockito.doNothing().when(enderecoService).deletarEndereco(1L);

                mockMvc.perform(delete("/endereco/1"))
                                .andExpect(status().isNoContent());
        }

        @Test
        void deletarEndereco_naoEncontrado() throws Exception {
                Mockito.doThrow(new RuntimeException("Endereço não encontrado"))
                                .when(enderecoService).deletarEndereco(999L);

                mockMvc.perform(delete("/endereco/999"))
                                .andExpect(status().isNotFound());
        }
}
