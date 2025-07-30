package itsprodigi.matteocasini.steam_clone_backend.repository;

import itsprodigi.matteocasini.steam_clone_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository per l'entità User.
 * Fornisce metodi CRUD e query personalizzate per la gestione degli utenti.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Trova un utente tramite lo username.
     *
     * @param username lo username da cercare.
     * @return un Optional contenente l'utente se trovato.
     */
    Optional<User> findByUsername(String username);

    /**
     * Verifica se esiste un utente con lo username specificato.
     *
     * @param username lo username da verificare.
     * @return true se esiste, false altrimenti.
     */
    boolean existsByUsername(String username);

    /**
     * Verifica se esiste un utente con l'email specificata.
     *
     * @param email l'email da verificare.
     * @return true se esiste, false altrimenti.
     */
    boolean existsByEmail(String email);

    /**
     * Trova un utente tramite l'email.
     *
     * @param email l'email da cercare.
     * @return un Optional contenente l'utente se trovato.
     */
    Optional<User> findByEmail(String email);
}