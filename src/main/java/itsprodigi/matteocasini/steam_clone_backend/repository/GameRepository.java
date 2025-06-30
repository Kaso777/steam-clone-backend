package itsprodigi.matteocasini.steam_clone_backend.repository;

import itsprodigi.matteocasini.steam_clone_backend.model.Game; // Importa la tua entità Game
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List; // Aggiunto per futuri metodi di ricerca

@Repository // Indica a Spring che questa è un'interfaccia repository e un componente gestito
public interface GameRepository extends JpaRepository<Game, Long> {
    // JpaRepository fornisce già metodi CRUD di base come save(), findById(), findAll(), deleteById(), count()

    /**
     * Trova un gioco tramite il suo identificatore pubblico (UUID).
     * @param uuid L'UUID del gioco da cercare.
     * @return Un Optional che contiene il gioco se trovato, altrimenti un Optional vuoto.
     */
    Optional<Game> findByUuid(UUID uuid);

    /**
     * Verifica se un gioco esiste tramite il suo identificatore pubblico (UUID).
     * Questo può essere più efficiente di findByUuid se hai solo bisogno di sapere se esiste.
     * @param uuid L'UUID del gioco da verificare.
     * @return true se il gioco esiste, false altrimenti.
     */
    boolean existsByUuid(UUID uuid);

    /**
     * Elimina un gioco dal database tramite il suo identificatore pubblico (UUID).
     * @param uuid L'UUID del gioco da eliminare.
     */
    void deleteByUuid(UUID uuid);

    // --- Metodi di ricerca aggiuntivi (opzionali per ora, ma utili per il futuro) ---

    /**
     * Trova tutti i giochi con un titolo che contiene una specifica stringa (case-insensitive).
     * @param title La stringa da cercare nel titolo del gioco.
     * @return Una lista di giochi che corrispondono al criterio.
     */
    List<Game> findByTitleContainingIgnoreCase(String title);

    /**
     * Trova tutti i giochi appartenenti a un determinato genere (case-insensitive).
     * @param genre Il genere da cercare.
     * @return Una lista di giochi che corrispondono al genere.
     */
    List<Game> findByGenreIgnoreCase(String genre);

    /**
     * Trova tutti i giochi sviluppati da un certo sviluppatore (case-insensitive).
     * @param developer Lo sviluppatore da cercare.
     * @return Una lista di giochi sviluppati da quel produttore.
     */
    List<Game> findByDeveloperContainingIgnoreCase(String developer);

    /**
     * Trova tutti i giochi pubblicati da un certo editore (case-insensitive).
     * @param publisher L'editore da cercare.
     * @return Una lista di giochi pubblicati da quell'editore.
     */
    List<Game> findByPublisherContainingIgnoreCase(String publisher);
}