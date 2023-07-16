package com.hubner.cadastro.domain.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FiltroPessoaDTO {
    private String nome = "";
    private String cpf = "";
    private LocalDate dataInicial = LocalDate.of(1, 1, 1);
    private LocalDate dataFinal = LocalDate.now();
}
