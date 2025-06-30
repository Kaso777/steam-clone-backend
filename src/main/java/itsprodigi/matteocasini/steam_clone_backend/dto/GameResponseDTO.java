package itsprodigi.matteocasini.steam_clone_backend.dto;

import itsprodigi.matteocasini.steam_clone_backend.model.Game;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class GameResponseDTO {
    private UUID uuid; // Questo campo nel DTO manterrà il nome 'uuid' per coerenza API esterna
    private String title;
    private String genre;
    private BigDecimal price;
    private LocalDate releaseDate;
    private String developer;
    private String publisher;

    public GameResponseDTO() {
    }

    // Costruttore: prende un'entità Game e la mappa al DTO
    // Questo costruttore è utile per convertire un'entità Game in un DTO
    public GameResponseDTO(Game game) {
        this.uuid = game.getId(); // Mappa l'ID (UUID) dell'entità all'UUID del DTO
        this.title = game.getTitle();
        this.genre = game.getGenre();
        this.price = game.getPrice();
        this.releaseDate = game.getReleaseDate();
        this.developer = game.getDeveloper();
        this.publisher = game.getPublisher();
    }

    // Costruttore con tutti i campi (se lo avevi, puoi tenerlo)
    // Questo costruttore è utile per creare un DTO direttamente con i valori
    public GameResponseDTO(UUID uuid, String title, String genre, BigDecimal price, LocalDate releaseDate, String developer, String publisher) {
        this.uuid = uuid;
        this.title = title;
        this.genre = genre;
        this.price = price;
        this.releaseDate = releaseDate;
        this.developer = developer;
        this.publisher = publisher;
    }

    // Getters e Setters
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
        return "GameResponseDTO{" +
               "uuid=" + uuid +
               ", title='" + title + '\'' +
               ", genre='" + genre + '\'' +
               ", price=" + price +
               ", releaseDate=" + releaseDate +
               ", developer='" + developer + '\'' +
               ", publisher='" + publisher + '\'' +
               '}';
    }
}