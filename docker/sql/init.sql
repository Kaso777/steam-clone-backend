-- Crea database se non esiste
CREATE DATABASE IF NOT EXISTS steamdb;
USE steamdb;

-- DROP tabelle per sviluppo
DROP TABLE IF EXISTS game_tags;
DROP TABLE IF EXISTS user_profiles;
DROP TABLE IF EXISTS user_games;
DROP TABLE IF EXISTS games;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS users;

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
  user_id BINARY(16) PRIMARY KEY,
  nickname VARCHAR(100) UNIQUE,
  bio TEXT,
  avatar_url VARCHAR(255),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Tabella giochi
CREATE TABLE games (
  id BINARY(16) PRIMARY KEY,
  title VARCHAR(100) NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  release_date DATE NOT NULL,
  developer VARCHAR(100) NOT NULL,
  publisher VARCHAR(100) NOT NULL
);

-- Tabella tag
CREATE TABLE tags (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) UNIQUE NOT NULL
);

-- Join Game ↔ Tag
CREATE TABLE game_tags (
  game_id BINARY(16),
  tag_id  BIGINT,
  PRIMARY KEY (game_id, tag_id),
  FOREIGN KEY (game_id) REFERENCES games(id),
  FOREIGN KEY (tag_id)  REFERENCES tags(id)
);

-- Tabella relazione Utente ↔ Gioco con chiave composta
CREATE TABLE user_games (
  user_uuid      BINARY(16),
  game_uuid      BINARY(16),
  purchase_date  DATE        NOT NULL,
  playtime_hours INT         NOT NULL,
  PRIMARY KEY (user_uuid, game_uuid),
  FOREIGN KEY (user_uuid) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (game_uuid) REFERENCES games(id) ON DELETE CASCADE
);

-- Dati di esempio
INSERT INTO users (id, username, email, password, role) VALUES
  (UNHEX(REPLACE('11111111-1111-1111-1111-111111111111','-','')), 'admin', 'admin@example.com', '$2a$10$Adl2ac0iv5B9XyZXNjN8SeHWlJHoAJ3.Ot7gIlVm/4zF7nTfd3wmO', 'ROLE_ADMIN'),
  (UNHEX(REPLACE('22222222-2222-2222-2222-222222222222','-','')), 'user',  'user@example.com',  '$2a$10$hL82QwsgZu2jFRfAyUTSQeKaXtPvKs7OZ.7r1qiuf2FqcNvHVqUNa', 'ROLE_USER');

INSERT INTO user_profiles (user_id, nickname, bio, avatar_url) VALUES
  (UNHEX(REPLACE('11111111-1111-1111-1111-111111111111','-','')), 'AdminMaster', 'Sono l’amministratore supremo.', 'http://example.com/admin.png'),
  (UNHEX(REPLACE('22222222-2222-2222-2222-222222222222','-','')), 'PlayerOne',   'Mi piacciono i giochi.',        'http://example.com/user.png');

INSERT INTO games (id, title, price, release_date, developer, publisher) VALUES
  (UNHEX(REPLACE('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa','-','')), 'CyberRumble', 59.99, '2023-01-10', 'CyberDev',   'MegaPub'),
  (UNHEX(REPLACE('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb','-','')), 'Fantasy War', 39.99, '2022-05-20', 'WarDev',     'FantasyCorp');

INSERT INTO tags (name) VALUES 
  ('Azione'), 
  ('Multiplayer');

INSERT INTO game_tags (game_id, tag_id) VALUES
  (UNHEX(REPLACE('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa','-','')), 1),
  (UNHEX(REPLACE('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb','-','')), 2);

INSERT INTO user_games (user_uuid, game_uuid, purchase_date, playtime_hours) VALUES
  (UNHEX(REPLACE('22222222-2222-2222-2222-222222222222','-','')), 
   UNHEX(REPLACE('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa','-','')), 
   '2023-07-01', 5);
