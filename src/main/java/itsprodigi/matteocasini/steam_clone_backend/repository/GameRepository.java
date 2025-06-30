package itsprodigi.matteocasini.steam_clone_backend.repository;

import itsprodigi.matteocasini.steam_clone_backend.model.Game; // Importa l'entità Game
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository // Indica a Spring che questa è un'interfaccia repository e un componente gestito
public interface GameRepository extends JpaRepository<Game, UUID> {
    // JpaRepository fornisce già metodi CRUD di base come save(), findById(), findAll(), deleteById(), count()

    List<Game> findByTitleContainingIgnoreCase(String title);

    List<Game> findByGenreIgnoreCase(String genre);

    List<Game> findByDeveloperContainingIgnoreCase(String developer);

    List<Game> findByPublisherContainingIgnoreCase(String publisher);

}