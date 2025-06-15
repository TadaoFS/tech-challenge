CREATE TABLE usuario (
                         ID SERIAL PRIMARY KEY,
                         nome VARCHAR(100) NOT NULL,
                         email VARCHAR(255) UNIQUE NOT NULL,
                         login VARCHAR(50) UNIQUE NOT NULL,
                         senha VARCHAR(255) NOT NULL,
                         status VARCHAR(50),
                         id_endereco INT UNIQUE,
                         data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         data_atualizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         CONSTRAINT fk_usuario_endereco FOREIGN KEY (id_endereco) REFERENCES Endereco(ID)
);