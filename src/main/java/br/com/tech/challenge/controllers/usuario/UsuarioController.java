package br.com.tech.challenge.controllers.usuario;

import br.com.tech.challenge.controllers.usuario.response.UsuarioResponse;
import br.com.tech.challenge.entities.Usuario;
import br.com.tech.challenge.services.UsuarioService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/usuarios")
public class UsuarioController {

    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public List<Usuario> getUsuario(){
        return this.usuarioService.buscarUsuarios();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public Usuario getUsuario(@PathVariable long id){
        return this.usuarioService.buscarUsuario(id);
    }

    @PostMapping(produces = "application/json")
    public UsuarioResponse postUsuario(@RequestBody Usuario usuario){
        return Optional.of(this.usuarioService.inserirUsuario(usuario))
                .map(usuarioR -> UsuarioResponse.builder().id(usuarioR.getId()).build())
                .orElseThrow(() -> new RuntimeException("Erro ao inserir usuário"));
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    public String putUsuario(@PathVariable long id, @RequestBody Usuario usuario){
        return this.usuarioService.editarUsuario(id, usuario);
    }

    @PatchMapping(value = "/{id}/senha", produces = "application/json")
    public String patchUsuarioSenha(@PathVariable long id, @RequestBody String novaSenha) {
        return this.usuarioService.alterarSenha(id, novaSenha);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public String deleteUsuario(@PathVariable long id){
        return this.usuarioService.deletarUsuario(id);
    }

}