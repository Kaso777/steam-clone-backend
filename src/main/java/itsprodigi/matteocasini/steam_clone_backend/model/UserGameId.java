//Questa classe rappresenta una chiave primaria composta per l'entità UserGame.
// È utilizzata per identificare univocamente una relazione tra un utente e un gioco.
// Per semplificare è una tabella di join tra User e Game.

package itsprodigi.matteocasini.steam_clone_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable; // Indica che la classe è incorporabile in altre entità
import java.io.Serializable;          // Necessario per le chiavi primarie composte
import java.util.Objects;             // Per implementare equals() e hashCode()
import java.util.UUID;                // Per gli UUID di User e Game

@Embeddable // Indica che questa classe può essere usata come parte di una chiave primaria in un'altra entità
public class UserGameId implements Serializable {

    @Column(name = "user_uuid", columnDefinition = "UUID")
    // UUID dell'utente come parte della chiave composta
    private UUID userUuid;

    @Column(name = "game_uuid", columnDefinition = "UUID")
    // UUID del gioco come parte della chiave composta
    private UUID gameUuid;

    // Costruttore senza argomenti (richiesto da JPA)
    public UserGameId() {
    }

    // Costruttore con argomenti
    public UserGameId(UUID userUuid, UUID gameUuid) {
        this.userUuid = userUuid;
        this.gameUuid = gameUuid;
    }

    // Getter e Setter
    public UUID getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(UUID userUuid) {
        this.userUuid = userUuid;
    }

    public UUID getGameUuid() {
        return gameUuid;
    }

    public void setGameUuid(UUID gameUuid) {
        this.gameUuid = gameUuid;
    }

    // Metodi equals() e hashCode() sono FONDAMENTALI per le chiavi primarie composte.
    // Garantiscono che due oggetti UserGameId siano considerati uguali se i loro UUID corrispondenti sono uguali.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserGameId that = (UserGameId) o;
        return Objects.equals(userUuid, that.userUuid) &&
               Objects.equals(gameUuid, that.gameUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userUuid, gameUuid);
    }
} 