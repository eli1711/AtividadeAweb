package com.medpro.medpro.infra.exceptions.security;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class TratadorDeErros {


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> tratarError404(){
        return ResponseEntity.notFound().build();
    }

     public ResponseEntity<List<TratadorDeErros.DadosErroValidacao>> tratarErro400(
            MethodArgumentNotValidException e){
                var erro = e.getFieldError();
                return ResponseEntity
                .badRequest()
                .body(erro.stream().map(DadosErroValidacao::new).toList());
        }

    public record DadosErroValidacao(String campo, String mensagem){
        this(erro.getField(), erro.getDefaultMessage());

    }

}
