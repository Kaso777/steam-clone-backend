package itsprodigi.matteocasini.steam_clone_backend.model;

import jakarta.persistence.*; // Importa tutte le annotazioni JPA
import java.time.LocalDate;   // Per la data di acquisto

/**
 * Entità che rappresenta la relazione Many-to-Many tra un Utente e un Gioco,
 * con attributi aggiuntivi specifici di questa relazione (es. data di acquisto, ore giocate).
 * Questo è il pattern per una relazione Many-to-Many con attributi extra, utilizzando una chiave composita.
 */
@Entity // Indica che questa classe è un'entità JPA e sarà mappata a una tabella del database.
@Table(name = "user_games") // Specifica il nome della tabella di giunzione nel database.
public class UserGame {

    @EmbeddedId // Indica che l'ID di questa entità è un oggetto incorporato (UserGameId),
                // che rappresenta la chiave primaria composita (user_uuid, game_uuid).
    private UserGameId id;

    @ManyToOne // Relazione Many-to-One con l'entità User. Molte voci UserGame appartengono a un singolo User.
    @MapsId("userUuid") // Mappa la parte 'userUuid' della chiave composta `UserGameId` all'ID dell'entità User.
    @JoinColumn(name = "user_uuid", referencedColumnName = "id", nullable = false)
    // @JoinColumn definisce la colonna della chiave esterna nella tabella 'user_games'.
    // 'name' è il nome della colonna nel DB ('user_uuid').
    // 'referencedColumnName' è il nome della colonna nella tabella 'users' a cui si riferisce ('id').
    // 'nullable = false' indica che questa chiave esterna non può essere nulla.
    private User user;

    @ManyToOne // Relazione Many-to-One con l'entità Game. Molte voci UserGame appartengono a un singolo Game.
    @MapsId("gameUuid") // Mappa la parte 'gameUuid' della chiave composta `UserGameId` all'ID dell'entità Game.
    @JoinColumn(name = "game_uuid", referencedColumnName = "id", nullable = false)
    // 'name' è il nome della colonna nel DB ('game_uuid').
    // 'referencedColumnName' è il nome della colonna nella tabella 'games' a cui si riferisce ('id').
    // 'nullable = false' indica che questa chiave esterna non può essere nulla.
    private Game game;

    @Column(name = "purchase_date", nullable = false)
    private LocalDate purchaseDate;

    @Column(name = "playtime_hours", nullable = false) // Nuovo campo per le ore giocate
    private int playtimeHours; // Usiamo int per un numero intero di ore

    // Costruttore senza argomenti (necessario per JPA)
    public UserGame() {
    }

    /**
     * Costruttore per creare una nuova voce UserGame.
     * Inizializza anche la chiave composita UserGameId.
     * @param user L'utente associato.
     * @param game Il gioco associato.
     * @param purchaseDate La data di acquisto.
     * @param playtimeHours Le ore giocate.
     */
    public UserGame(User user, Game game, LocalDate purchaseDate, int playtimeHours) {
        this.user = user;
        this.game = game;
        this.purchaseDate = purchaseDate;
        this.playtimeHours = playtimeHours;
        // Inizializza l'ID composto usando gli ID degli oggetti User e Game.
        this.id = new UserGameId(user.getId(), game.getId());
    }

    // --- GETTER ---

    public UserGameId getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Game getGame() {
        return game;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public int getPlaytimeHours() {
        return playtimeHours;
    }

    // --- SETTER ---

    public void setId(UserGameId id) {
        this.id = id;
    }

    /**
     * Imposta l'utente associato a questa voce UserGame.
     * Aggiorna anche la parte 'userUuid' della chiave composita.
     * @param user L'entità User da associare.
     */
    public void setUser(User user) {
        this.user = user;
        // Assicurati che l'oggetto ID non sia nullo prima di impostare la sua parte.
        if (this.id == null) {
            this.id = new UserGameId();
        }
        this.id.setUserUuid(user.getId());
    }

    /**
     * Imposta il gioco associato a questa voce UserGame.
     * Aggiorna anche la parte 'gameUuid' della chiave composita.
     * @param game L'entità Game da associare.
     */
    public void setGame(Game game) {
        this.game = game;
        // Assicurati che l'oggetto ID non sia nullo prima di impostare la sua parte.
        if (this.id == null) {
            this.id = new UserGameId();
        }
        this.id.setGameUuid(game.getId());
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public void setPlaytimeHours(int playtimeHours) {
        this.playtimeHours = playtimeHours;
    }

    // --- Metodi equals, hashCode, toString (best practice) ---
    // Questi metodi sono cruciali per le entità con chiave composita.
    // Si basano sull'oggetto UserGameId per l'uguaglianza e l'hashing.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserGame userGame = (UserGame) o;
        return id != null && id.equals(userGame.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
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