package io.github.kaso777.steamclone.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.UUID;

public class UserGameRequestDTO {

    @NotNull(message = "L'UUID del gioco non puo essere nullo")
    private UUID gameUuid;

    @NotNull(message = "La data di acquisto non puo essere nulla")
    @PastOrPresent(message = "La data di acquisto non puo essere nel futuro")
    private LocalDate purchaseDate;

    @Min(value = 0, message = "Le ore giocate non possono essere negative.")
    private int playtimeHours;

    public UserGameRequestDTO() {
    }

    public UserGameRequestDTO(UUID gameUuid, LocalDate purchaseDate, int playtimeHours) {
        this.gameUuid = gameUuid;
        this.purchaseDate = purchaseDate;
        this.playtimeHours = playtimeHours;
    }

    public UUID getGameUuid() {
        return gameUuid;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public int getPlaytimeHours() {
        return playtimeHours;
    }

    public void setGameUuid(UUID gameUuid) {
        this.gameUuid = gameUuid;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public void setPlaytimeHours(int playtimeHours) {
        this.playtimeHours = playtimeHours;
    }

    @Override
    public String toString() {
        return "UserGameRequestDTO{" +
                "gameUuid=" + gameUuid +
                ", purchaseDate=" + purchaseDate +
                ", playtimeHours=" + playtimeHours +
                '}';
    }
}
