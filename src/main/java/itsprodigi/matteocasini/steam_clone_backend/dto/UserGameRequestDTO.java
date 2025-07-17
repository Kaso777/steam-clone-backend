package itsprodigi.matteocasini.steam_clone_backend.dto;

import jakarta.validation.constraints.Min; // Per validare un valore minimo (es. ore giocate non negative)
import jakarta.validation.constraints.NotNull; // Per campi non nulli
import jakarta.validation.constraints.PastOrPresent; // Per date che non sono nel futuro
import java.time.LocalDate; // Per la data di acquisto
import java.util.UUID;      // Per gli UUID di Utente e Gioco

/**
 * Data Transfer Object (DTO) per le richieste in ingresso (input) relative a UserGame.
 * Questo DTO è utilizzato quando un client invia dati al server per operazioni come
 * l'aggiunta di un gioco alla libreria di un utente o l'aggiornamento degli attributi
 * di una voce esistente nella libreria (es. ore giocate).
 *
 * Contiene gli UUID dell'utente e del gioco per identificare l'associazione,
 * e gli attributi specifici della relazione come la data di acquisto e le ore giocate.
 */
public class UserGameRequestDTO {

    @NotNull(message = "L'UUID dell'utente non può essere nullo")
    private UUID userUuid; // UUID dell'utente che acquista/possiede il gioco

    @NotNull(message = "L'UUID del gioco non può essere nullo")
    private UUID gameUuid; // UUID del gioco che viene aggiunto/modificato

    @NotNull(message = "La data di acquisto non può essere nulla")
    @PastOrPresent(message = "La data di acquisto non può essere nel futuro")
    private LocalDate purchaseDate; // Data in cui il gioco è stato aggiunto alla libreria

    // Nuovo campo: ore giocate.
    @Min(value = 0, message = "Le ore giocate non possono essere negative.") // Assicura che le ore siano >= 0
    private int playtimeHours; // Le ore totali giocate dall'utente a questo specifico gioco

    // Costruttore senza argomenti (necessario per la deserializzazione JSON)
    public UserGameRequestDTO() {
    }

    // Costruttore con tutti gli argomenti, incluso il nuovo campo 'playtimeHours'
    public UserGameRequestDTO(UUID userUuid, UUID gameUuid, LocalDate purchaseDate, int playtimeHours) {
        this.userUuid = userUuid;
        this.gameUuid = gameUuid;
        this.purchaseDate = purchaseDate;
        this.playtimeHours = playtimeHours;
    }

    // --- Getter ---
    public UUID getUserUuid() {
        return userUuid;
    }

    public UUID getGameUuid() {
        return gameUuid;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    // Nuovo Getter per 'playtimeHours'
    public int getPlaytimeHours() {
        return playtimeHours;
    }

    // --- Setter ---
    public void setUserUuid(UUID userUuid) {
        this.userUuid = userUuid;
    }

    public void setGameUuid(UUID gameUuid) {
        this.gameUuid = gameUuid;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    // Nuovo Setter per 'playtimeHours'
    public void setPlaytimeHours(int playtimeHours) {
        this.playtimeHours = playtimeHours;
    }

    @Override
    public String toString() {
        return "UserGameRequestDTO{" +
               "userUuid=" + userUuid +
               ", gameUuid=" + gameUuid +
               ", purchaseDate=" + purchaseDate +
               ", playtimeHours=" + playtimeHours +
               '}';
    }
}