package br.com.tech.challenge.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Entity
@Table(name="Usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String nome;

    String email;

    String login;

    String senha;

    String dataAlteracao;

    @OneToOne(cascade = CascadeType.ALL)
    Endereco endereco;

}
