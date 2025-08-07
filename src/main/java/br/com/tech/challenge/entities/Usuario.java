package br.com.tech.challenge.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Entity
@Table(name="Usuario")
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String nome;

    String email;

    String login;

    String senha;

    String dataAtualizacao;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    Endereco endereco;

}
