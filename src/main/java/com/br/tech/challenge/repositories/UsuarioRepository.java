package com.br.tech.challenge.repositories;

import com.br.tech.challenge.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
