INSERT INTO perfil(id_usuario, tipo)
VALUES ((SELECT ID FROM usuario WHERE login = 'admin'), 'ADMIN');