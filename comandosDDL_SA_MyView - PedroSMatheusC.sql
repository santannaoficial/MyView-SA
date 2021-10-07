/*
NOME: Exemplo
SENHA: 12345678
*/

CREATE DATABASE db_sa_myview_pedrosmatheusc;

USE db_sa_myview_pedrosmatheusc;

CREATE TABLE id_user (
id_user INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
name_user VARCHAR(40) NOT NULL,
email_user VARCHAR(80) NOT NULL,
password_user INT UNSIGNED NOT NULL,
profile_user VARCHAR(15) NOT NULL,
cpf_user VARCHAR(11)
);

CREATE TABLE tb_content(
id_content INT ZEROFILL UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
name_content VARCHAR(40) NOT NULL,
type_content VARCHAR(15) NOT NULL,
grade_content TINYINT UNSIGNED NOT NULL,
obs_content TEXT,
date_content timestamp NOT NULL,
tb_user_id_user INT UNSIGNED NOT NULL,
favorite_content VARCHAR(3),
CONSTRAINT tb_user_id_user FOREIGN KEY (tb_user_id_user) REFERENCES tb_user(id_user)
);

ALTER TABLE tb_content MODIFY COLUMN date_content timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP;

INSERT INTO tb_user (id_user, name_user, email_user, password_user, profile_user, cpf_user) VALUES (1,'Exemplo', 'Exemplo@gmail.com', 12345678, 'Admin', 12345678910);
