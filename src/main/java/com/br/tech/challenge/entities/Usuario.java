package com.br.tech.challenge.entities;


public class Usuario {

    String nome;

    String email;

    String login;

    String senha;

    String dataAlteracao;

    Endereco endereco;

    public Usuario(String nome, String email, String login, String senha, String dataAlteracao, Endereco endereco) {
        this.nome = nome;
        this.email = email;
        this.login = login;
        this.senha = senha;
        this.dataAlteracao = dataAlteracao;
        this.endereco = endereco;
    }

}
