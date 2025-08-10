package br.com.tech.challenge.exception;

public class EnderecoNaoEncontrado extends RuntimeException {
    public EnderecoNaoEncontrado(String message) {
        super(message);
    }
}
