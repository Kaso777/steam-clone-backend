package itsprodigi.matteocasini.steam_clone_backend.repository;

import itsprodigi.matteocasini.steam_clone_backend.model.UserGame; // Importa l'entità UserGame
import itsprodigi.matteocasini.steam_clone_backend.model.UserGameId; // Importa la chiave composita UserGameId
import org.springframework.data.jpa.repository.JpaRepository; // Importa JpaRepository
import org.springframework.stereotype.Repository; // Importa l'annotazione @Repository

import java.util.List; // Per liste di UserGame
import java.util.Optional; // Per gestire la possibile assenza di un valore
import java.util.UUID;     // Per gli ID univoci

/**
 * Interfaccia Repository per l'entità UserGame.
 * Estende JpaRepository per fornire automaticamente metodi CRUD (Create, Read, Update, Delete)
 * e funzionalità di paginazione e ordinamento per l'entità UserGame.
 * L'ID dell'entità UserGame è di tipo UserGameId (chiave composita).
 *
 * Questa interfaccia è responsabile dell'interazione diretta con il database
 * per le operazioni relative alle associazioni utente-gioco.
 */
@Repository // Indica a Spring che questa interfaccia è un componente Repository e deve essere gestita.
public interface UserGameRepository extends JpaRepository<UserGame, UserGameId> {

    /**
     * Trova tutte le voci UserGame (associazioni utente-gioco) associate a un utente specifico
     * tramite il suo ID. Questo metodo è utile per recuperare l'intera libreria di un utente.
     * Spring Data JPA genererà automaticamente la query SQL basandosi sul nome del metodo
     * (interpreta "ByUserId" come una ricerca basata sul campo 'user' della relazione e il suo 'id').
     *
     * @param userId L'UUID dell'utente di cui si vogliono trovare i giochi nella libreria.
     * @return Una lista di entità UserGame associate all'utente. Restituisce una lista vuota se l'utente non ha giochi.
     */
    List<UserGame> findByUserId(UUID userId);

    // JpaRepository fornisce già metodi standard come:
    // Optional<UserGame> findById(UserGameId id); // Per trovare una specifica associazione tramite la sua chiave composta
    // boolean existsById(UserGameId id);         // Per verificare se una specifica associazione esiste
    // UserGame save(UserGame userGame);          // Per salvare (creare o aggiornare) un'associazione
    // void deleteById(UserGameId id);            // Per eliminare una specifica associazione
    // List<UserGame> findAll();                 // Per trovare tutte le associazioni UserGame
}