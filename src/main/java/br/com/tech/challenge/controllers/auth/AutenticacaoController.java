package br.com.tech.challenge.controllers.auth;

import br.com.tech.challenge.controllers.auth.request.LoginRequest;
import br.com.tech.challenge.controllers.auth.response.LoginResponse;
import br.com.tech.challenge.services.AutenticacaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@Slf4j
public class AutenticacaoController {

    private final AutenticacaoService autenticacaoService;

    public AutenticacaoController(AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        log.info("[AutenticacaoController] - Iniciando autenticacao - Login: {}", request.login());
        return autenticacaoService.login(request);
    }
}
