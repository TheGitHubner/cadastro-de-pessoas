package com.hubner.cadastro.domain.exception;

public class LancadorExcecao extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public LancadorExcecao(String message) {
        super(message);
    }
}
