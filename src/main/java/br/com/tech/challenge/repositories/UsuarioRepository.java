package br.com.tech.challenge.repositories;

import br.com.tech.challenge.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
