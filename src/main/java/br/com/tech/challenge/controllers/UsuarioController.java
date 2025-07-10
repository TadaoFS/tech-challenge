package br.com.tech.challenge.controllers;

import br.com.tech.challenge.entities.Usuario;
import br.com.tech.challenge.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/usuarios")
public class UsuarioController {

    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/{id}", produces = "application/json")
    public Usuario getUsuario(@PathVariable long id){
        return this.usuarioService.buscarUsuario(id);
    }

    @PostMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public String postUsuario(@RequestBody Usuario usuario){
        return this.usuarioService.inserirUsuario(usuario);
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