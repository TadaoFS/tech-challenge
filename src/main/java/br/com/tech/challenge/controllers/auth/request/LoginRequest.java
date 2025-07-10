package br.com.tech.challenge.controllers.auth.request;

public record LoginRequest(
        String login,
        String senha
) {
}
