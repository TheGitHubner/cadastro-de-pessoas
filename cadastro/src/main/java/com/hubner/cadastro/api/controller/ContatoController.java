package com.hubner.cadastro.api.controller;

import com.hubner.cadastro.domain.dto.ContatoRequestDTO;
import com.hubner.cadastro.domain.dto.FiltroContatoDTO;
import com.hubner.cadastro.domain.model.Contato;
import com.hubner.cadastro.domain.service.ContatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/contatos")
public class ContatoController {

    @Autowired
    private ContatoService contatoService;

    @GetMapping("/all")
    public List<Contato> getTodosContatos(){
        return this.contatoService.getTodosContatos();
    }

    @GetMapping("/{contatoId}")
    public ResponseEntity<Contato> getContatoPorId(@PathVariable Long contatoId){
        return this.contatoService.getContatoPorId(contatoId);
    }

    @GetMapping
    public ResponseEntity<Page<Contato>> getContatosPaginadoComFiltro(@RequestBody FiltroContatoDTO filtroContatoDTO,
                                                                      @RequestParam(defaultValue="0") int page,
                                                                      @RequestParam(defaultValue="15") int pageSize) {
        return ResponseEntity.ok(this.contatoService.getContatosPaginadoComFiltro(filtroContatoDTO, page, pageSize));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Contato criarContato(@Valid @RequestBody ContatoRequestDTO contatoDTO) {
        return this.contatoService.salvarContato(contatoDTO, null);
    }

    @PutMapping("/{contatoId}")
    public ResponseEntity<Contato> alterarContato(@PathVariable Long contatoId,
                                                 @RequestBody ContatoRequestDTO contatoDTO) {
        return this.contatoService.alterarContato(contatoId, contatoDTO);
    }

    @DeleteMapping("/{contatoId}")
    public ResponseEntity<Void> deletarContato(@PathVariable Long contatoId) {
        return this.contatoService.deletarContato(contatoId);
    }
}
