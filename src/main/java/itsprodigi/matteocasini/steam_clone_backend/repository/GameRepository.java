package itsprodigi.matteocasini.steam_clone_backend.repository;

import itsprodigi.matteocasini.steam_clone_backend.model.Game; // Importa l'entità Game
import org.springframework.data.jpa.repository.JpaRepository; // Importa JpaRepository
import org.springframework.data.jpa.repository.Query; // Importa l'annotazione @Query per query personalizzate
import org.springframework.data.repository.query.Param; // Importa @Param per i parametri delle query
import org.springframework.stereotype.Repository; // Importa l'annotazione @Repository

import java.util.List;    // Per liste di giochi
import java.util.Optional; // Per gestire la possibile assenza di un valore
import java.util.UUID;     // Per gli ID univoci

/**
 * Interfaccia Repository per l'entità Game.
 * Estende JpaRepository per fornire automaticamente metodi CRUD (Create, Read, Update, Delete)
 * e funzionalità di paginazione e ordinamento per l'entità Game.
 * L'ID dell'entità Game è di tipo UUID.
 *
 * Questa interfaccia è responsabile dell'interazione diretta con il database
 * per le operazioni relative ai giochi, inclusi i caricamenti delle relazioni (Tag).
 */
@Repository // Indica a Spring che questa interfaccia è un componente Repository e deve essere gestita.
public interface GameRepository extends JpaRepository<Game, UUID> {

    /**
     * Trova un gioco tramite il suo titolo.
     * Utilizzato principalmente per verificare l'unicità del titolo durante la creazione o l'aggiornamento.
     * Spring Data JPA genererà automaticamente la query SQL.
     * @param title Il titolo del gioco da cercare.
     * @return Un Optional contenente l'entità Game se trovata, altrimenti Optional.empty().
     */
    Optional<Game> findByTitle(String title);

    // --- Metodi per caricare i giochi con i loro tag (per evitare LazyInitializationException) ---
    // Le relazioni ManyToMany sono spesso caricate in modo LAZY per default.
    // Questi metodi usano JOIN FETCH per garantire che i tag vengano caricati
    // nella stessa query del gioco, prevenendo errori quando i DTO vengono mappati.

    /**
     * Trova un gioco per ID e carica esplicitamente i suoi tag associati.
     * Utilizza una query JPQL con LEFT JOIN FETCH per caricare la collezione 'tags'
     * in una singola query, evitando problemi di N+1 e LazyInitializationException.
     * @param id L'ID (UUID) del gioco da cercare.
     * @return Un Optional contenente il gioco con i tag caricati, se trovato.
     */
    @Query("SELECT g FROM Game g LEFT JOIN FETCH g.tags WHERE g.id = :id")
    Optional<Game> findByIdWithTags(@Param("id") UUID id);

    /**
     * Trova tutti i giochi presenti nel database e carica esplicitamente i loro tag associati.
     * Utilizza una query JPQL con LEFT JOIN FETCH e DISTINCT per ottenere tutti i giochi
     * e i loro tag in modo efficiente, prevenendo duplicati nel risultato se un gioco ha molti tag.
     * @return Una lista di tutti i giochi con i tag caricati.
     */
    @Query("SELECT DISTINCT g FROM Game g LEFT JOIN FETCH g.tags")
    List<Game> findAllWithTags();

    // --- Metodi di ricerca aggiornati per includere il caricamento dei tag ---
    // Questi metodi sono stati adattati per utilizzare query JPQL che caricano i tag
    // insieme ai giochi, rendendo i DTO di risposta più facili da popolare.

    /**
     * Trova giochi il cui titolo contiene la stringa specificata (case-insensitive)
     * e carica i loro tag.
     * @param title La stringa da cercare nel titolo del gioco.
     * @return Una lista di giochi corrispondenti con i tag caricati.
     */
    @Query("SELECT DISTINCT g FROM Game g LEFT JOIN FETCH g.tags WHERE LOWER(g.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Game> findByTitleContainingIgnoreCase(@Param("title") String title);

    // Rimosso findByGenreIgnoreCase(String genre) perché l'entità Game non ha più un campo 'genre'.

    /**
     * Trova giochi associati a un tag specifico tramite il nome del tag (case-insensitive)
     * e carica i loro tag.
     * @param tagName Il nome del tag da cercare.
     * @return Una lista di giochi corrispondenti con i tag caricati.
     */
    @Query("SELECT DISTINCT g FROM Game g JOIN FETCH g.tags t WHERE LOWER(t.name) = LOWER(:tagName)")
    List<Game> findByTags_NameIgnoreCase(@Param("tagName") String tagName);

    /**
     * Trova giochi sviluppati da uno specifico sviluppatore (case-insensitive)
     * e carica i loro tag.
     * @param developer Il nome dello sviluppatore.
     * @return Una lista di giochi corrispondenti con i tag caricati.
     */
    @Query("SELECT DISTINCT g FROM Game g LEFT JOIN FETCH g.tags WHERE LOWER(g.developer) LIKE LOWER(CONCAT('%', :developer, '%'))")
    List<Game> findByDeveloperIgnoreCase(@Param("developer") String developer);

    /**
     * Trova giochi pubblicati da uno specifico editore (case-insensitive)
     * e carica i loro tag.
     * @param publisher Il nome dell'editore.
     * @return Una lista di giochi corrispondenti con i tag caricati.
     */
    @Query("SELECT DISTINCT g FROM Game g LEFT JOIN FETCH g.tags WHERE LOWER(g.publisher) LIKE LOWER(CONCAT('%', :publisher, '%'))")
    List<Game> findByPublisherIgnoreCase(@Param("publisher") String publisher);

    // JpaRepository fornisce già metodi standard come:
    // Optional<Game> findById(UUID id); // Per trovare un gioco per ID (senza caricare i tag)
    // List<Game> findAll();             // Per trovare tutti i giochi (senza caricare i tag)
    // Game save(Game game);             // Per salvare (creare o aggiornare) un gioco
    // void deleteById(UUID id);         // Per eliminare un gioco
    // boolean existsById(UUID id);      // Per verificare se un gioco esiste per ID
}