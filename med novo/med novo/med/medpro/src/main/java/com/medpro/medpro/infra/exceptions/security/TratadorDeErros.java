package com.medpro.medpro.infra.exceptions.security;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.validation.FieldError;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> tratarError404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DadosErroValidacao>> tratarErro400(MethodArgumentNotValidException e) {
        List<FieldError> erros = e.getBindingResult().getFieldErrors();
        List<DadosErroValidacao> dadosErroValidacao = erros.stream()
                .map(erro -> new DadosErroValidacao(erro.getField(), erro.getDefaultMessage()))
                .toList();
        return ResponseEntity.badRequest().body(dadosErroValidacao);
    }

    public record DadosErroValidacao(String campo, String mensagem) {
        public DadosErroValidacao(String campo, String mensagem) {
            this.campo = campo;
            this.mensagem = mensagem;
        }
    }
}
