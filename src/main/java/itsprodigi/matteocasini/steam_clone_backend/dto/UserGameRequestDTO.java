package itsprodigi.matteocasini.steam_clone_backend.dto;

import jakarta.validation.constraints.NotNull; // Per campi non nulli
import jakarta.validation.constraints.PastOrPresent; // Per date che non sono nel futuro
import java.time.LocalDate; // Per la data di acquisto
import java.util.UUID;      // Per gli UUID di Utente e Gioco

public class UserGameRequestDTO {

    @NotNull(message = "L'UUID dell'utente non può essere nullo")
    private UUID userUuid; // UUID dell'utente che acquista il gioco

    @NotNull(message = "L'UUID del gioco non può essere nullo")
    private UUID gameUuid; // UUID del gioco che viene aggiunto

    @NotNull(message = "La data di acquisto non può essere nulla")
    @PastOrPresent(message = "La data di acquisto non può essere nel futuro")
    private LocalDate purchaseDate; // Data in cui il gioco è stato aggiunto alla libreria

    // Costruttore senza argomenti (necessario per la deserializzazione JSON)
    public UserGameRequestDTO() {
    }

    // Costruttore con argomenti
    public UserGameRequestDTO(UUID userUuid, UUID gameUuid, LocalDate purchaseDate) {
        this.userUuid = userUuid;
        this.gameUuid = gameUuid;
        this.purchaseDate = purchaseDate;
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

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Override
    public String toString() {
        return "UserGameRequestDTO{" +
               "userUuid=" + userUuid +
               ", gameUuid=" + gameUuid +
               ", purchaseDate=" + purchaseDate +
               '}';
    }
}