CREATE TABLE perfil (
                                 ID SERIAL PRIMARY KEY,
                                 tipo VARCHAR(100) NOT NULL,
                                 id_usuario INT UNIQUE,
                                 CONSTRAINT fk_perfis_usuarios_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(ID)
                             );