package itsprodigi.matteocasini.steam_clone_backend.repository;

import itsprodigi.matteocasini.steam_clone_backend.model.UserGame;     // Importa l'entità UserGame
import itsprodigi.matteocasini.steam_clone_backend.model.UserGameId; // Importa la chiave primaria composta
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID; // Per cercare per UUID di User o Game

@Repository // Indica a Spring che questa è un'interfaccia repository
public interface UserGameRepository extends JpaRepository<UserGame, UserGameId> {
    // JpaRepository<[TipoEntità], [TipoChiavePrimaria]>
    // Qui la chiave primaria è UserGameId, la classe che abbiamo creato per la PK composta.

    /**
     * Trova tutte le entry della libreria per un determinato utente, tramite il suo UUID.
     * @param userUuid L'UUID dell'utente.
     * @return Una lista di UserGame che rappresentano i giochi posseduti dall'utente.
     */
    List<UserGame> findByIdUserUuid(UUID userUuid);

    /**
     * Trova una specifica entry della libreria tramite l'UUID dell'utente e l'UUID del gioco.
     * @param userUuid L'UUID dell'utente.
     * @param gameUuid L'UUID del gioco.
     * @return Un Optional contenente l'entry UserGame se trovata, altrimenti Optional.empty().
     */
    Optional<UserGame> findByIdUserUuidAndIdGameUuid(UUID userUuid, UUID gameUuid);

    /**
     * Verifica se una specifica entry della libreria esiste, tramite l'UUID dell'utente e l'UUID del gioco.
     * Questo è utile per controllare se un utente possiede già un certo gioco.
     * @param userUuid L'UUID dell'utente.
     * @param gameUuid L'UUID del gioco.
     * @return true se l'entry esiste, false altrimenti.
     */
    boolean existsByIdUserUuidAndIdGameUuid(UUID userUuid, UUID gameUuid);

    /**
     * Elimina una specifica entry della libreria tramite l'UUID dell'utente e l'UUID del gioco.
     * @param userUuid L'UUID dell'utente.
     * @param gameUuid L'UUID del gioco.
     */
    void deleteByIdUserUuidAndIdGameUuid(UUID userUuid, UUID gameUuid);
} 