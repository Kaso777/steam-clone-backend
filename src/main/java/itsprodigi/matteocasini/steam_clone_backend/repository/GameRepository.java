package itsprodigi.matteocasini.steam_clone_backend.repository;

import itsprodigi.matteocasini.steam_clone_backend.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameRepository extends JpaRepository<Game, UUID> {

    Optional<Game> findByTitle(String title);

    @Query("SELECT g FROM Game g LEFT JOIN FETCH g.tags WHERE g.id = :id")
    Optional<Game> findByIdWithTags(@Param("id") UUID id);

    @Query("SELECT DISTINCT g FROM Game g LEFT JOIN FETCH g.tags")
    List<Game> findAllWithTags();

    @Query("SELECT DISTINCT g FROM Game g LEFT JOIN FETCH g.tags WHERE LOWER(g.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Game> findByTitleContainingIgnoreCase(@Param("title") String title);

    @Query("SELECT DISTINCT g FROM Game g JOIN FETCH g.tags t WHERE LOWER(t.name) = LOWER(:tagName)")
    List<Game> findByTags_NameIgnoreCase(@Param("tagName") String tagName);

    @Query("SELECT DISTINCT g FROM Game g LEFT JOIN FETCH g.tags WHERE LOWER(g.developer) LIKE LOWER(CONCAT('%', :developer, '%'))")
    List<Game> findByDeveloperIgnoreCase(@Param("developer") String developer);

    @Query("SELECT DISTINCT g FROM Game g LEFT JOIN FETCH g.tags WHERE LOWER(g.publisher) LIKE LOWER(CONCAT('%', :publisher, '%'))")
    List<Game> findByPublisherIgnoreCase(@Param("publisher") String publisher);
}
