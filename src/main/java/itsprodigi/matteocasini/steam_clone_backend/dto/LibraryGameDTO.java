package itsprodigi.matteocasini.steam_clone_backend.dto;

import itsprodigi.matteocasini.steam_clone_backend.model.UserGame;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID; // Aggiungi questo import se non c'è

public class LibraryGameDTO {
    private GameResponseDTO game;
    private LocalDate purchaseDate;

    // Costruttore che accetta un'entità UserGame e la mappa in questo DTO
    public LibraryGameDTO(UserGame userGame) {
        this.game = new GameResponseDTO(userGame.getGame()); // Converte Game in GameResponseDTO
        this.purchaseDate = userGame.getPurchaseDate();
    }

    // Costruttore vuoto (necessario per la deserializzazione JSON se usi Jackson)
    public LibraryGameDTO() {
    }

    // --- Getters ---
    public GameResponseDTO getGame() {
        return game;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    // --- Setters ---
    public void setGame(GameResponseDTO game) {
        this.game = game;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    // --- Metodi equals, hashCode, toString (opzionali ma buone pratiche) ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LibraryGameDTO that = (LibraryGameDTO) o;
        return Objects.equals(game, that.game) && Objects.equals(purchaseDate, that.purchaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(game, purchaseDate);
    }

    @Override
    public String toString() {
        return "LibraryGameDTO{" +
               "game=" + game +
               ", purchaseDate=" + purchaseDate +
               '}';
    }
}