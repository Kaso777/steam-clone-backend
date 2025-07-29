package itsprodigi.matteocasini.steam_clone_backend.repository;

import itsprodigi.matteocasini.steam_clone_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository per l'entità User.
 * Fornisce metodi CRUD e personalizzati per la gestione degli utenti.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Trova un utente tramite il suo username.
     * @param username Lo username da cercare.
     * @return Un Optional contenente l'utente se trovato.
     */
    Optional<User> findByUsername(String username);

    /**
     * Verifica se esiste un utente con uno specifico username.
     * Utile per controlli durante la registrazione.
     * @param username Lo username da verificare.
     * @return true se esiste, false altrimenti.
     */
    boolean existsByUsername(String username);

    /**
     * Verifica se esiste un utente con una specifica email.
     * @param email L'email da verificare.
     * @return true se esiste, false altrimenti.
     */
    boolean existsByEmail(String email);

    /**
     * Trova un utente tramite la sua email.
     * @param email L'email da cercare.
     * @return Un Optional contenente l'utente se trovato.
     */
    Optional<User> findByEmail(String email);
}
