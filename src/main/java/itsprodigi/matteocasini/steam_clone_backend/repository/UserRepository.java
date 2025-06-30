package itsprodigi.matteocasini.steam_clone_backend.repository;

import itsprodigi.matteocasini.steam_clone_backend.model.User; // Importa la tua entità User
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository // Indica a Spring che questa è un'interfaccia repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // Usa i metodi standard di JpaRepository: findById(UUID), existsById(UUID), deleteById(UUID).

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
    /*È estremamente utile durante la fase di registrazione di un nuovo utente.
    Prima che un utente scelga uno username, puoi chiamare questo metodo per controllare rapidamente se lo username è già stato preso, impedendo duplicati.
    È più efficiente di findByUsername se ti serve solo sapere se esiste, perché il database può ottimizzare la ricerca
    non dovendo recuperare tutti i dati dell'utente.
    */

    boolean existsByEmail(String email);
    /*Molto simile al caso dello username, lo useresti nella fase di registrazione per assicurarti
    che un indirizzo email non sia già stato utilizzato da un altro account.
    Questo è un controllo comune per garantire l'unicità dell'email per utente.
    */
}