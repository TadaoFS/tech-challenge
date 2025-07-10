package br.com.tech.challenge.entities;

import br.com.tech.challenge.enums.PerfilEnum;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Entity
@Table(name="perfil")
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    @MapsId
    private Usuario idUsuario;

    @Enumerated(EnumType.STRING)
    private PerfilEnum tipo;
}
