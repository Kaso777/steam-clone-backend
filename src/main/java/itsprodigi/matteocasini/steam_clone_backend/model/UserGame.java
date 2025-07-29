package itsprodigi.matteocasini.steam_clone_backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "user_games")
public class UserGame {

    @EmbeddedId
    private UserGameId id;

    @ManyToOne
    @MapsId("userUuid")
    @JoinColumn(name = "user_uuid", nullable = false)
    private User user;

    @ManyToOne
    @MapsId("gameUuid")
    @JoinColumn(name = "game_uuid", nullable = false)
    private Game game;

    @Column(name = "purchase_date", nullable = false)
    private LocalDate purchaseDate;

    @Column(name = "playtime_hours", nullable = false)
    private int playtimeHours;

    public UserGame() {}

    public UserGame(User user, Game game, LocalDate purchaseDate, int playtimeHours) {
        this.user = user;
        this.game = game;
        this.purchaseDate = purchaseDate;
        this.playtimeHours = playtimeHours;
        this.id = new UserGameId(user.getId(), game.getId());
    }

    public UserGameId getId() {
        return id;
    }

    public void setId(UserGameId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (this.id == null) this.id = new UserGameId();
        this.id.setUserUuid(user.getId());
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
        if (this.id == null) this.id = new UserGameId();
        this.id.setGameUuid(game.getId());
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public int getPlaytimeHours() {
        return playtimeHours;
    }

    public void setPlaytimeHours(int playtimeHours) {
        this.playtimeHours = playtimeHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserGame that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserGame{" +
                "id=" + id +
                ", user=" + (user != null ? user.getId() : "null") +
                ", game=" + (game != null ? game.getId() : "null") +
                ", purchaseDate=" + purchaseDate +
                ", playtimeHours=" + playtimeHours +
                '}';
    }
}
