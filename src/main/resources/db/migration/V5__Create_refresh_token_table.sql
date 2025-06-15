CREATE TABLE refresh_token (
                               ID SERIAL PRIMARY KEY,
                               id_usuario INT UNIQUE NOT NULL,
                               data_expiracao TIMESTAMP NOT NULL,
                               CONSTRAINT fk_refresh_token_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(ID)
);