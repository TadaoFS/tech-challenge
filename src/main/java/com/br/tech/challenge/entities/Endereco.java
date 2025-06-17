package com.br.tech.challenge.entities;


public class Endereco {

    String cep;

    String logradouro;

    String numero;

    String bairro;

    String cidade;

    String estado;

    public Endereco(String cep, String logradouro, String numero, String bairro, String cidade, String estado) {
        this.cep = cep;
        this.logradouro = logradouro;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
    }

}
