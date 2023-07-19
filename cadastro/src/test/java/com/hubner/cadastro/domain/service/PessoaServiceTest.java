package com.hubner.cadastro.domain.service;

import com.hubner.cadastro.domain.model.Contato;
import com.hubner.cadastro.domain.model.Pessoa;
import com.hubner.cadastro.domain.repository.PessoaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class PessoaServiceTest {
    @Mock
    PessoaRepository pessoaRepository;
    PessoaService pessoaService;
    Pessoa pessoa;
    Contato contato;
    Long pessoaId;

    @BeforeEach
    public void setUp(){
        pessoaRepository = mock(PessoaRepository.class);
        pessoaService = new PessoaService(pessoaRepository);

        pessoa = new Pessoa("teste pessoa","09950668921",LocalDate.now(), new ArrayList<>());
        contato = new Contato("teste contato","44999800121","teste@hotmail.com", pessoa);
        pessoa.getContatos().add(contato);

        pessoaId = 1L;
    }

    @Test
    public void deveRetornarAPessoaComSucesso() {
        when(pessoaRepository.findById(pessoaId)).thenReturn(Optional.of(pessoa));

        ResponseEntity<Pessoa> response = pessoaService.getPessoaPorId(pessoaId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pessoa, response.getBody());

        verify(pessoaRepository).findById(pessoaId);
        verifyNoMoreInteractions(pessoaRepository);
    }

    @Test
    public void deveRetornarNullQuandoNaoEncontrarAPessoa() {
        when(pessoaRepository.findById(pessoaId)).thenReturn(Optional.empty());

        ResponseEntity<Pessoa> response = pessoaService.getPessoaPorId(pessoaId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(pessoaRepository).findById(pessoaId);
        verifyNoMoreInteractions(pessoaRepository);
    }

    @Test
    public void deveDeletarUmaPessoaComSucesso(){
        when(pessoaRepository.existsById(pessoaId)).thenReturn(true);

        ResponseEntity<Void> response = pessoaService.deletarPessoa(pessoaId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(pessoaRepository).deleteById(pessoaId);
    }
}
