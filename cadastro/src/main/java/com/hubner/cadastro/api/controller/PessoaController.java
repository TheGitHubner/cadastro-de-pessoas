package com.hubner.cadastro.api.controller;

import com.hubner.cadastro.domain.dto.FiltroPessoaDTO;
import com.hubner.cadastro.domain.model.Pessoa;
import com.hubner.cadastro.domain.service.PessoaService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/pessoas")
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;

    @GetMapping("/all")
    public List<Pessoa> getTodasPessoas(){
        return this.pessoaService.getTodasPessoas();
    }

    @GetMapping("/{pessoaId}")
    public ResponseEntity<Pessoa> getPessoaPorId(@PathVariable Long pessoaId){
        return this.pessoaService.getPessoaPorId(pessoaId);
    }

    @GetMapping
    public ResponseEntity<Page<Pessoa>> getPessoasPaginadoComFiltro(@RequestBody FiltroPessoaDTO filtroPessoaDTO,
                                                                    @RequestParam(defaultValue="0") int page,
                                                                    @RequestParam(defaultValue="15") int pageSize) {
        return ResponseEntity.ok(this.pessoaService.getPessoasPaginadoComFiltro(filtroPessoaDTO, page, pageSize));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pessoa criarPessoa(@Valid @RequestBody Pessoa pessoa){
        return this.pessoaService.criar(pessoa);
    }

}
