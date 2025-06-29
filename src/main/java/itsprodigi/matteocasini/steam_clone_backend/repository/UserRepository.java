package itsprodigi.matteocasini.steam_clone_backend.repository;

import itsprodigi.matteocasini.steam_clone_backend.model.User; // Importa la tua entità User
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository // Indica a Spring che questa è un'interfaccia repository
public interface UserRepository extends JpaRepository<User, Long> {
    // JpaRepository fornisce già metodi come save(), findById(), findAll(), deleteById(), count()

    // Metodo personalizzato per trovare un utente tramite il suo UUID
    Optional<User> findByUuid(UUID uuid);

    // Metodo personalizzato per verificare se un utente esiste tramite il suo UUID
    boolean existsByUuid(UUID uuid);

    // Metodo personalizzato per eliminare un utente tramite il suo UUID
    void deleteByUuid(UUID uuid);
}