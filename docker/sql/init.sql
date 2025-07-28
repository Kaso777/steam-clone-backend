-- Crea database se non esiste
CREATE DATABASE IF NOT EXISTS steamdb;
USE steamdb;

-- DROP tabelle per sicurezza (solo in ambiente di sviluppo)
-- Ho aggiunto user_profiles qui per assicurarmi che venga droppata prima di essere ricreata
DROP TABLE IF EXISTS user_game, game_tags, user_profiles, users, tags, games;

-- Tabella utenti
CREATE TABLE users (
  id BINARY(16) PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(20) NOT NULL
);

-- Tabella profili utente
CREATE TABLE user_profiles (
  user_id BINARY(16) PRIMARY KEY, -- Chiave esterna e primaria, si lega a users.id
  nickname VARCHAR(100) UNIQUE,
  bio TEXT,
  avatar_url VARCHAR(255),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
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


INSERT INTO users (id, username, email, password, role) VALUES
  (UNHEX(REPLACE('11111111-1111-1111-1111-111111111111', '-', '')), 'admin', 'admin@example.com', '$2a$10$Adl2ac0iv5B9XyZXNjN8SeHWlJHoAJ3.Ot7gIlVm/4zF7nTfd3wmO', 'ROLE_ADMIN'),
  (UNHEX(REPLACE('22222222-2222-2222-2222-222222222222', '-', '')), 'user', 'user@example.com', '$2a$10$hL82QwsgZu2jFRfAyUTSQeKaXtPvKs7OZ.7r1qiuf2FqcNvHVqUNa', 'ROLE_USER');

INSERT INTO user_profiles (user_id, nickname, bio, avatar_url) VALUES
  (UNHEX(REPLACE('11111111-1111-1111-1111-111111111111', '-', '')), 'AdminMaster', 'Sono l\'amministratore supremo.', 'http://example.com/admin_avatar.png'),
  (UNHEX(REPLACE('22222222-2222-2222-2222-222222222222', '-', '')), 'PlayerOne', 'Mi piacciono i giochi.', 'http://example.com/user_avatar.png');

INSERT INTO games (id, title, price, release_date, developer, publisher) VALUES
  (UNHEX(REPLACE('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '-', '')), 'CyberRumble', 59.99, '2023-01-10', 'CyberDev', 'MegaPub'),
  (UNHEX(REPLACE('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '-', '')), 'Fantasy War', 39.99, '2022-05-20', 'WarDev', 'FantasyCorp');

INSERT INTO tags (name) VALUES ('Azione'), ('Multiplayer');

INSERT INTO game_tags (game_id, tag_id) VALUES
  (UNHEX(REPLACE('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '-', '')), 1),
  (UNHEX(REPLACE('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '-', '')), 2);

INSERT INTO user_game (user_id, game_id, status, hours_played) VALUES
  (UNHEX(REPLACE('22222222-2222-2222-2222-222222222222', '-', '')), UNHEX(REPLACE('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '-', '')), 'In Gioco', 5);