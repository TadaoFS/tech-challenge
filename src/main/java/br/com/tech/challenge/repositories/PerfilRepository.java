package br.com.tech.challenge.repositories;

import br.com.tech.challenge.entities.Perfil;
import br.com.tech.challenge.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    Optional<Perfil> findByIdUsuario(Usuario idUsuario);

    void deleteByIdUsuario(Usuario idUsuario);
}
