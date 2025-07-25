package br.com.tech.challenge.services;


import br.com.tech.challenge.entities.Endereco;
import br.com.tech.challenge.repositories.EnderecoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnderecoServiceTest {

    @InjectMocks
    private EnderecoService enderecoService;

    @Mock
    private EnderecoRepository enderecoRepository;

    private Endereco endereco;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

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
    void salvarEnderecoSucesso() {
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);

        Endereco salvo = enderecoService.salvarEndereco(endereco);

        assertNotNull(salvo);
        assertEquals("12345678", salvo.getCep());
        verify(enderecoRepository, times(1)).save(endereco);
    }

    @Test
    void listarEnderecosSucesso() {
        when(enderecoRepository.findAll()).thenReturn(List.of(endereco));

        List<Endereco> lista = enderecoService.listarEnderecos();

        assertEquals(1, lista.size());
        assertEquals("12345678", lista.get(0).getCep());
        verify(enderecoRepository, times(1)).findAll();
    }

    @Test
    void buscarEnderecoPorIdSucesso() {
        when(enderecoRepository.findById(1L)).thenReturn(Optional.of(endereco));

        Optional<Endereco> resultado = enderecoService.buscarEnderecoPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("12345678", resultado.get().getCep());
        verify(enderecoRepository, times(1)).findById(1L);
    }

    @Test
    void buscarEnderecoPorIdNaoEncontrado() {
        when(enderecoRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Endereco> resultado = enderecoService.buscarEnderecoPorId(999L);

        assertTrue(resultado.isEmpty());
        verify(enderecoRepository, times(1)).findById(999L);
    }

    @Test
    void atualizarEnderecoSucesso() {
        Endereco enderecoAtualizado = new Endereco();
        enderecoAtualizado.setCep("87654321");
        enderecoAtualizado.setLogradouro("Rua Atualizada");
        enderecoAtualizado.setNumero("200");
        enderecoAtualizado.setBairro("Bairro Novo");
        enderecoAtualizado.setCidade("Cidade");
        enderecoAtualizado.setEstado("UF");

        when(enderecoRepository.findById(1L)).thenReturn(Optional.of(endereco));
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(enderecoAtualizado);

        Endereco result = enderecoService.atualizarEndereco(1L, enderecoAtualizado);

        assertNotNull(result);
        assertEquals("87654321", result.getCep());
        assertEquals("Rua Atualizada", result.getLogradouro());
        verify(enderecoRepository, times(1)).findById(1L);
        verify(enderecoRepository, times(1)).save(any(Endereco.class));
    }

    @Test
    void atualizarEnderecoNaoEncontrado() {
        when(enderecoRepository.findById(999L)).thenReturn(Optional.empty());

        Endereco enderecoAtualizado = new Endereco();
        enderecoAtualizado.setCep("87654321");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            enderecoService.atualizarEndereco(999L, enderecoAtualizado);
        });

        assertTrue(exception.getMessage().contains("não encontrado"));
        verify(enderecoRepository, times(1)).findById(999L);
        verify(enderecoRepository, never()).save(any(Endereco.class));
    }

    @Test
    void deletarEnderecoSucesso() {
        when(enderecoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(enderecoRepository).deleteById(1L);

        enderecoService.deletarEndereco(1L);

        verify(enderecoRepository, times(1)).existsById(1L);
        verify(enderecoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletarEnderecoNaoEncontrado() {
        when(enderecoRepository.existsById(999L)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            enderecoService.deletarEndereco(999L);
        });

        assertTrue(exception.getMessage().contains("não encontrado"));
        verify(enderecoRepository, times(1)).existsById(999L);
        verify(enderecoRepository, never()).deleteById(anyLong());
    }
}
