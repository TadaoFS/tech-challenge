package com.br.tech.challenge.controllers;

import com.br.tech.challenge.entities.Usuario;
import com.br.tech.challenge.services.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/v1/usuarios")
public class UsuarioController {

    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public Usuario getUsuario(@PathVariable long id){
        return this.usuarioService.buscarUsuario(id);
    }

    @PostMapping(produces = "application/json")
    public String postUsuario(@RequestBody Usuario usuario){
        return this.usuarioService.inserirUsuario(usuario);
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    public String putUsuario(@PathVariable long id, @RequestBody Usuario usuario){
        return this.usuarioService.editarUsuario(id, usuario);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public String deleteUsuario(@PathVariable long id){
        return this.usuarioService.deletarUsuario(id);
    }

}