package br.com.tech.challenge.services;

import br.com.tech.challenge.controllers.auth.request.LoginRequest;
import br.com.tech.challenge.controllers.auth.response.LoginResponse;
import br.com.tech.challenge.repositories.PerfilRepository;
import br.com.tech.challenge.repositories.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AutenticacaoService {

    private TokenService tokenService;
    private PerfilRepository perfilRepository;
    private UsuarioRepository usuarioRepository;
    private static final String TOKEN_TYPE = "Bearer";

    public AutenticacaoService(TokenService tokenService, PerfilRepository perfilRepository, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.perfilRepository = perfilRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public LoginResponse login(LoginRequest request) {
        var usuario = usuarioRepository.findByLogin((request.login()))
                .orElseThrow(() -> {
                    log.error("[AutenticacaoService] - Usuário não encontrado: {}", request.login());
                    return new RuntimeException("Usuário não encontrado");
                });
        var perfil = perfilRepository.findByIdUsuario(usuario)
                .orElseThrow(() -> {
                    log.error("[AutenticacaoService] - Perfil não encontrado para usuário: {}", usuario.getLogin());
                    return new RuntimeException("Perfil não encontrado");
                });
        if(!usuario.getSenha().equals(request.senha())) {
            log.error("[AutenticacaoService] - Senha incorreta para usuário: {}", request.login());
            throw new RuntimeException("Senha incorreta");
        }
        var token = tokenService.gerarToken(usuario, perfil);
        return new LoginResponse(token, TOKEN_TYPE);
    }
}
