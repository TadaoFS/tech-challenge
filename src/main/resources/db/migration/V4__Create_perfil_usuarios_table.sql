CREATE TABLE perfis_usuarios (
                                 id_usuario INT NOT NULL,
                                 id_perfil INT NOT NULL,
                                 PRIMARY KEY (id_usuario, id_perfil),
                                 CONSTRAINT fk_perfis_usuarios_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(ID),
                                 CONSTRAINT fk_perfis_usuarios_perfil FOREIGN KEY (id_perfil) REFERENCES perfil(ID)
);