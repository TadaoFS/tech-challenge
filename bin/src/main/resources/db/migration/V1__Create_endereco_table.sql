CREATE TABLE Endereco (
                          ID SERIAL PRIMARY KEY,
                          cep VARCHAR(20),
                          logradouro VARCHAR(255),
                          numero VARCHAR(10),
                          bairro VARCHAR(100),
                          cidade VARCHAR(100),
                          data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          data_atualizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);