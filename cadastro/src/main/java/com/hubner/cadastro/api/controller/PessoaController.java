package com.hubner.cadastro.api.controller;

import com.hubner.cadastro.domain.model.Pessoa;
import com.hubner.cadastro.domain.service.PessoaService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/pessoas")
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pessoa criarPessoa(@Valid @RequestBody Pessoa pessoa){
        return pessoaService.criar(pessoa);
    }

}
