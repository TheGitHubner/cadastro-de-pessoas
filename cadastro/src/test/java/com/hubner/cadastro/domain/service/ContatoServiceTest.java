package com.hubner.cadastro.domain.service;

import com.hubner.cadastro.domain.model.Contato;
import com.hubner.cadastro.domain.model.Pessoa;
import com.hubner.cadastro.domain.repository.ContatoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class ContatoServiceTest {
    @Mock
    ContatoRepository contatoRepository;
    ContatoService contatoService;
    Pessoa pessoa;
    Contato contato;
    Long contatoId;

    @BeforeEach
    public void setUp(){
        contatoRepository = mock(ContatoRepository.class);
        contatoService = new ContatoService(contatoRepository);

        pessoa = new Pessoa("teste pessoa","09950668921", LocalDate.now(), new ArrayList<>());
        contato = new Contato("teste contato","44999800121","teste@hotmail.com", pessoa);
        pessoa.getContatos().add(contato);
        pessoa.setId(1L);
        contato.setId(1L);

        contatoId = 1L;
    }

    @Test
    public void deveRetornarOContatoComSucesso() {
        when(contatoRepository.findById(contatoId)).thenReturn(Optional.of(contato));

        ResponseEntity<Contato> response = contatoService.getContatoPorId(contatoId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contato, response.getBody());

        verify(contatoRepository).findById(contatoId);
        verifyNoMoreInteractions(contatoRepository);
    }

    @Test
    public void deveRetornarNullQuandoNaoEncontrarOContato() {
        when(contatoRepository.findById(contatoId)).thenReturn(Optional.empty());

        ResponseEntity<Contato> response = contatoService.getContatoPorId(contatoId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(contatoRepository).findById(contatoId);
        verifyNoMoreInteractions(contatoRepository);
    }

//  TODO: Teste ainda não funcional
//    @Test
//    public void deveDeletarUmContatoComSucesso(){
//        when(contatoRepository.existsById(contatoId)).thenReturn(true);
//        when(contatoService.getContato(contatoId)).thenReturn(contato);
//        when(contatoService.getContato(contatoId).getPessoa()).thenReturn(pessoa);
//        when(contatoService.getContato(contatoId).getPessoa().getContatos()).thenReturn(Collections.singletonList(contato));
//        when((contatoService.getContato(contatoId).getPessoa().getContatos().size()>1)).thenReturn(true);
//
//        ContatoService contatoService = new ContatoService(contatoRepository);
//
//        ResponseEntity<Void> response = contatoService.deletarContato(contatoId);
//
//        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
//        assertNull(response.getBody());
//
//        verify(contatoRepository).deleteById(contatoId);
//        verifyNoMoreInteractions(contatoRepository);
//    }


}

