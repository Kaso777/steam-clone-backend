package itsprodigi.matteocasini.steam_clone_backend.model;

import jakarta.persistence.*;
import java.math.BigDecimal; // Per prezzi precisi
import java.time.LocalDate;   // Per la data di rilascio
import java.util.UUID;        // Per l'identificatore pubblico

@Entity // Indica che questa classe è un'entità JPA e sarà mappata a una tabella di database
@Table(name = "games") // Specifica il nome della tabella nel database
public class Game {

    @Id // Indica che questo campo è la chiave primaria della tabella
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Specifica che il valore dell'ID sarà generato automaticamente dal database (es. auto-increment)
    private Long id; // ID interno del database, solitamente non esposto esternamente

    @Column(name = "uuid", unique = true, nullable = false, updatable = false, columnDefinition = "UUID")
    // Mappa a una colonna 'uuid'
    // unique = true: ogni UUID deve essere unico
    // nullable = false: il campo non può essere nullo
    // updatable = false: il valore non può essere modificato dopo la creazione
    // columnDefinition = "UUID": Suggerisce al database il tipo di colonna (utile per PostgreSQL)
    private UUID uuid; // Identificatore pubblico e immutabile del gioco

    @Column(name = "title", nullable = false, length = 100)
    // nullable = false: Il titolo non può essere nullo
    // length = 100: Limita la lunghezza del titolo a 100 caratteri
    private String title; // Titolo del gioco

    @Column(name = "genre", nullable = false, length = 50)
    private String genre; // Genere del gioco (es. "RPG", "Action", "Strategy")

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    // precision = 10: Il numero totale di cifre è 10
    // scale = 2: Il numero di cifre dopo la virgola è 2 (es. 12345678.99)
    private BigDecimal price; // Prezzo del gioco (BigDecimal è preferibile per valute per evitare problemi di precisione dei float/double)

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate; // Data di rilascio del gioco

    @Column(name = "developer", nullable = false, length = 100)
    private String developer; // Sviluppatore del gioco

    @Column(name = "publisher", nullable = false, length = 100)
    private String publisher; // Editore del gioco

    // --- Costruttori ---
    // Costruttore senza argomenti: richiesto da JPA
    public Game() {
    }

    // Costruttore con argomenti per facilitare la creazione di istanze di Game
    public Game(String title, String genre, BigDecimal price, LocalDate releaseDate, String developer, String publisher) {
        this.title = title;
        this.genre = genre;
        this.price = price;
        this.releaseDate = releaseDate;
        this.developer = developer;
        this.publisher = publisher;
    }

    // --- Metodo per generare l'UUID prima della persistenza ---
    @PrePersist // Questa annotazione fa sì che il metodo venga eseguito prima che l'entità venga salvata per la prima volta nel database
    public void generateUuid() {
        if (this.uuid == null) { // Genera un nuovo UUID solo se non è già stato impostato
            this.uuid = UUID.randomUUID(); // Genera un UUID casuale
        }
    }

    // --- Getter e Setter per tutti i campi ---
    // Questi metodi permettono di accedere e modificare i valori dei campi dell'entità
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @Override
    public String toString() {
        return "Game{" +
               "id=" + id +
               ", uuid=" + uuid +
               ", title='" + title + '\'' +
               ", genre='" + genre + '\'' +
               ", price=" + price +
               ", releaseDate=" + releaseDate +
               ", developer='" + developer + '\'' +
               ", publisher='" + publisher + '\'' +
               '}';
    }
}