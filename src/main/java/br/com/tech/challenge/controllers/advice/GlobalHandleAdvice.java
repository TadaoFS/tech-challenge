package br.com.tech.challenge.controllers.advice;

import br.com.tech.challenge.exception.EnderecoNaoEncontrado;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(-1)
public class GlobalHandleAdvice {

    @ExceptionHandler(EnderecoNaoEncontrado.class)
    @ResponseStatus(org.springframework.http.HttpStatus.NOT_FOUND)
    public String handleEnderecoNaoEncontrado(EnderecoNaoEncontrado ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUsernameNotFound(UsernameNotFoundException ex) {
        return ex.getMessage();
    }
}
