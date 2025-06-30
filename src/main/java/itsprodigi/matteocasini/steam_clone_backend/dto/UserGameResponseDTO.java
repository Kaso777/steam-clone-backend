package itsprodigi.matteocasini.steam_clone_backend.dto;

import itsprodigi.matteocasini.steam_clone_backend.model.UserGame; // Importa l'entità UserGame
import java.time.LocalDate; // Per la data di acquisto

public class UserGameResponseDTO {

    private UserResponseDTO user; // Include i dettagli dell'utente
    private GameResponseDTO game; // Include i dettagli del gioco
    private LocalDate purchaseDate; // La data di acquisto

    // Costruttore senza argomenti
    public UserGameResponseDTO() {
    }

    // Costruttore che mappa da un'entità UserGame
    public UserGameResponseDTO(UserGame userGame) {
        // Mappa l'entità User all'UserResponseDTO
        this.user = new UserResponseDTO(userGame.getUser());
        // Mappa l'entità Game al GameResponseDTO
        this.game = new GameResponseDTO(userGame.getGame());
        this.purchaseDate = userGame.getPurchaseDate();
    }

    // Costruttore con tutti i campi
    public UserGameResponseDTO(UserResponseDTO user, GameResponseDTO game, LocalDate purchaseDate) {
        this.user = user;
        this.game = game;
        this.purchaseDate = purchaseDate;
    }

    // Getter e Setter
    public UserResponseDTO getUser() {
        return user;
    }

    public void setUser(UserResponseDTO user) {
        this.user = user;
    }

    public GameResponseDTO getGame() {
        return game;
    }

    public void setGame(GameResponseDTO game) {
        this.game = game;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Override
    public String toString() {
        return "UserGameResponseDTO{" +
               "user=" + user +
               ", game=" + game +
               ", purchaseDate=" + purchaseDate +
               '}';
    }
} 