-- Crea database se non esiste
CREATE DATABASE IF NOT EXISTS steamdb;
USE steamdb;

-- DROP tabelle per sicurezza (solo in ambiente di sviluppo)
DROP TABLE IF EXISTS user_game, game_tags, users, tags, games;

-- Tabella utenti
CREATE TABLE users (
  id BINARY(16) PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(20) NOT NULL
);

-- Tabella giochi
CREATE TABLE games (
  id BINARY(16) PRIMARY KEY,
  title VARCHAR(100) NOT NULL,
  price DECIMAL(10, 2) NOT NULL,
  release_date DATE NOT NULL,
  developer VARCHAR(100) NOT NULL,
  publisher VARCHAR(100) NOT NULL
);

-- Tabella tag
CREATE TABLE tags (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) UNIQUE NOT NULL
);

-- Tabella relazione molti-a-molti Game <-> Tag
CREATE TABLE game_tags (
  game_id BINARY(16),
  tag_id BIGINT,
  PRIMARY KEY (game_id, tag_id),
  FOREIGN KEY (game_id) REFERENCES games(id),
  FOREIGN KEY (tag_id) REFERENCES tags(id)
);

-- Tabella relazione utenti-giochi
CREATE TABLE user_game (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BINARY(16),
  game_id BINARY(16),
  status VARCHAR(50),
  hours_played INT,
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (game_id) REFERENCES games(id)
);

-- Inserimento utenti (UUID semplificati per esempio)
INSERT INTO users (id, username, email, password, role) VALUES
  (UNHEX(REPLACE('11111111-1111-1111-1111-111111111111', '-', '')), 'admin', 'admin@example.com', 'adminpass', 'ROLE_ADMIN'),
  (UNHEX(REPLACE('22222222-2222-2222-2222-222222222222', '-', '')), 'user', 'user@example.com', 'userpass', 'ROLE_USER');

-- Inserimento giochi
INSERT INTO games (id, title, price, release_date, developer, publisher) VALUES
  (UNHEX(REPLACE('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '-', '')), 'CyberRumble', 59.99, '2023-01-10', 'CyberDev', 'MegaPub'),
  (UNHEX(REPLACE('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '-', '')), 'Fantasy War', 39.99, '2022-05-20', 'WarDev', 'FantasyCorp');

-- Inserimento tag
INSERT INTO tags (name) VALUES ('Azione'), ('Multiplayer');

-- Collegamento giochi-tag
INSERT INTO game_tags (game_id, tag_id) VALUES
  (UNHEX(REPLACE('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '-', '')), 1),
  (UNHEX(REPLACE('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '-', '')), 2);

-- Libreria utente (relazione user <-> game)
INSERT INTO user_game (user_id, game_id, status, hours_played) VALUES
  (UNHEX(REPLACE('22222222-2222-2222-2222-222222222222', '-', '')), UNHEX(REPLACE('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '-', '')), 'In Gioco', 5);
