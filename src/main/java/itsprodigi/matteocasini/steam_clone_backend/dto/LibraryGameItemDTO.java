package itsprodigi.matteocasini.steam_clone_backend.dto;

import itsprodigi.matteocasini.steam_clone_backend.model.UserGame;
import java.time.LocalDate;

public class LibraryGameItemDTO {

    private GameResponseDTO game;
    private LocalDate purchaseDate;
    private int playtimeHours;

    public LibraryGameItemDTO() {
    }

    public LibraryGameItemDTO(UserGame userGame) {
        this.game = new GameResponseDTO(userGame.getGame());
        this.purchaseDate = userGame.getPurchaseDate();
        this.playtimeHours = userGame.getPlaytimeHours();
    }

    public LibraryGameItemDTO(GameResponseDTO game, LocalDate purchaseDate, int playtimeHours) {
        this.game = game;
        this.purchaseDate = purchaseDate;
        this.playtimeHours = playtimeHours;
    }

    public GameResponseDTO getGame() {
        return game;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public int getPlaytimeHours() {
        return playtimeHours;
    }

    public void setGame(GameResponseDTO game) {
        this.game = game;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public void setPlaytimeHours(int playtimeHours) {
        this.playtimeHours = playtimeHours;
    }

    @Override
    public String toString() {
        return "LibraryGameItemDTO{" +
                "game=" + game +
                ", purchaseDate=" + purchaseDate +
                ", playtimeHours=" + playtimeHours +
                '}';
    }
}
