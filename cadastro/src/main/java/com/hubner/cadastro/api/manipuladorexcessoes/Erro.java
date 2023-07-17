package com.hubner.cadastro.api.manipuladorexcessoes;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
public class Erro {
    private Integer status;
    private LocalDateTime momento;
    private String titulo;
    private List<Campo> campos;

    public Erro(Integer status, LocalDateTime momento, String titulo) {
        this.status = status;
        this.momento = momento;
        this.titulo = titulo;
    }

    @AllArgsConstructor
    @Getter
    public static class Campo{
        private String nomeCampo;
        private String mensagem;
    }
}
