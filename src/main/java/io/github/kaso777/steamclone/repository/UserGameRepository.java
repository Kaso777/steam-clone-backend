package io.github.kaso777.steamclone.repository;

import io.github.kaso777.steamclone.model.UserGame;
import io.github.kaso777.steamclone.model.UserGameId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserGameRepository extends JpaRepository<UserGame, UserGameId> {
    List<UserGame> findByUserId(UUID userId);
}
