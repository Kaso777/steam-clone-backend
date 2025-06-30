package itsprodigi.matteocasini.steam_clone_backend.model;

import jakarta.persistence.*; // Importa tutte le annotazioni JPA
import java.time.LocalDate;   // Per la data di acquisto

@Entity // Indica che questa classe è un'entità JPA
@Table(name = "user_games") // Specifica il nome della tabella di giunzione nel database
public class UserGame {

    @EmbeddedId // Indica che l'ID di questa entità è un oggetto incorporato (UserGameId)
    private UserGameId id;

    @ManyToOne // Relazione Many-to-One con User
    @MapsId("userUuid") // Mappa la parte 'userUuid' della chiave composta `UserGameId` all'ID di User
    // CAMBIAMENTO QUI: `referencedColumnName = "id"` punta al campo ID (UUID) dell'entità User
    @JoinColumn(name = "user_uuid", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne // Relazione Many-to-One con Game
    @MapsId("gameUuid") // Mappa la parte 'gameUuid' della chiave composta `UserGameId` all'ID di Game
    // CAMBIAMENTO QUI: `referencedColumnName = "id"` punta al campo ID (UUID) dell'entità Game
    @JoinColumn(name = "game_uuid", referencedColumnName = "id", nullable = false)
    private Game game;

    @Column(name = "purchase_date", nullable = false)
    private LocalDate purchaseDate;

    public UserGame() {
    }

    public UserGame(User user, Game game, LocalDate purchaseDate) {
        this.user = user;
        this.game = game;
        this.purchaseDate = purchaseDate;
        // Inizializza l'ID composto usando gli ID (ora UUID) degli oggetti User e Game
        this.id = new UserGameId(user.getId(), game.getId());
    }

    // Getter e Setter
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
        if (this.id == null) {
            this.id = new UserGameId();
        }
        this.id.setUserUuid(user.getId()); // Usa getId() del User (che ora è UUID)
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
        if (this.id == null) {
            this.id = new UserGameId();
        }
        this.id.setGameUuid(game.getId()); // Usa getId() del Game (che ora è UUID)
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Override
    public String toString() {
        return "UserGame{" +
               "id=" + id +
               ", user=" + (user != null ? user.getId() : "null") + // Mostra l'UUID dell'utente
               ", game=" + (game != null ? game.getId() : "null") + // Mostra l'UUID del gioco
               ", purchaseDate=" + purchaseDate +
               '}';
    }
}