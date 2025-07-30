package itsprodigi.matteocasini.steam_clone_backend.dto;

import itsprodigi.matteocasini.steam_clone_backend.model.UserGame;

import java.time.LocalDate;

public class UserGameResponseDTO {

    private UserResponseDTO user;
    private GameResponseDTO game;
    private LocalDate purchaseDate;
    private int playtimeHours;

    public UserGameResponseDTO() {
    }

    public UserGameResponseDTO(UserGame userGame) {
        this.user = new UserResponseDTO(userGame.getUser());
        this.game = new GameResponseDTO(userGame.getGame());
        this.purchaseDate = userGame.getPurchaseDate();
        this.playtimeHours = userGame.getPlaytimeHours();
    }

    public UserGameResponseDTO(UserResponseDTO user, GameResponseDTO game, LocalDate purchaseDate, int playtimeHours) {
        this.user = user;
        this.game = game;
        this.purchaseDate = purchaseDate;
        this.playtimeHours = playtimeHours;
    }

    public UserResponseDTO getUser() {
        return user;
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

    public void setUser(UserResponseDTO user) {
        this.user = user;
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
        return "UserGameResponseDTO{" +
                "user=" + user +
                ", game=" + game +
                ", purchaseDate=" + purchaseDate +
                ", playtimeHours=" + playtimeHours +
                '}';
    }
}