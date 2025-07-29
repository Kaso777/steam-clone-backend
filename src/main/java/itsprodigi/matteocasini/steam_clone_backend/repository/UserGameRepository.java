package itsprodigi.matteocasini.steam_clone_backend.repository;

import itsprodigi.matteocasini.steam_clone_backend.model.UserGame;
import itsprodigi.matteocasini.steam_clone_backend.model.UserGameId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserGameRepository extends JpaRepository<UserGame, UserGameId> {
    List<UserGame> findByUserId(UUID userId);
}
