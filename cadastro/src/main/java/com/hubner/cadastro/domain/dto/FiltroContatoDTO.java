package com.hubner.cadastro.domain.dto;

import lombok.Data;

@Data
public class FiltroContatoDTO {
    private String nome = "";
    private String telefone = "";
    private String email = "";
    private Long pessoaId;
}
