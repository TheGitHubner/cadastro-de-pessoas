package com.hubner.cadastro.api.manipuladorexcessoes;

import com.hubner.cadastro.domain.exception.LancadorExcecao;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@ControllerAdvice
public class ApiManipuladorExcessoes extends ResponseEntityExceptionHandler {
    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        List<Erro.Campo> campos = new ArrayList<>();

        for (ObjectError erro : ex.getBindingResult().getAllErrors()) {
            String nomeCampo = ((FieldError) erro).getField();
            String mensagem = messageSource.getMessage(erro, LocaleContextHolder.getLocale());
            campos.add(new Erro.Campo(nomeCampo, mensagem));
        }

        Erro erro = new Erro(status.value(),
                             LocalDateTime.now(),
                       "Existe(m) campo(s) inválido(s). Preencha-os e tente novamente.",
                             campos);

        return handleExceptionInternal(ex, erro, headers, status, request);
    }

    @ExceptionHandler(LancadorExcecao.class)
    public ResponseEntity<Object> handleBusiness(LancadorExcecao ex, WebRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Erro erro = new Erro(status.value(), LocalDateTime.now(), ex.getMessage());
        return handleExceptionInternal(ex, erro, new HttpHeaders(), status, request);
    }
}
