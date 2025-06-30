package itsprodigi.matteocasini.steam_clone_backend.model;

import jakarta.persistence.*;
import java.math.BigDecimal; // Per prezzi precisi
import java.time.LocalDate;   // Per la data di rilascio
import java.util.UUID;        // Per l'identificatore pubblico

@Entity // Indica che questa classe è un'entità JPA e sarà mappata a una tabella di database
@Table(name = "games") // Specifica il nome della tabella nel database
public class Game {

    @Id // Indica che questo campo è la chiave primaria
    // `columnDefinition = "UUID"` è importante per H2 e PostgreSQL
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "genre", nullable = false, length = 50)
    private String genre;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @Column(name = "developer", nullable = false, length = 100)
    private String developer;

    @Column(name = "publisher", nullable = false, length = 100)
    private String publisher;

    public Game() {
    }

    public Game(String title, String genre, BigDecimal price, LocalDate releaseDate, String developer, String publisher) {
        this.title = title;
        this.genre = genre;
        this.price = price;
        this.releaseDate = releaseDate;
        this.developer = developer;
        this.publisher = publisher;
    }

    @PrePersist // Questo metodo viene eseguito automaticamente prima di salvare una nuova entità nel DB
    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID(); // Genera un UUID per l'ID primario
        }
    }

    // Getter e Setter
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
               ", title='" + title + '\'' +
               ", genre='" + genre + '\'' +
               ", price=" + price +
               ", releaseDate=" + releaseDate +
               ", developer='" + developer + '\'' +
               ", publisher='" + publisher + '\'' +
               '}';
    }
} 