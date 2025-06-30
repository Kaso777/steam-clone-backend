package itsprodigi.matteocasini.steam_clone_backend.dto;

import jakarta.validation.constraints.DecimalMin; // Per validare valori minimi decimali
import jakarta.validation.constraints.NotBlank;   // Per campi non vuoti
import jakarta.validation.constraints.NotNull;    // Per campi non nulli
import jakarta.validation.constraints.PastOrPresent; // Per date che non sono nel futuro
import jakarta.validation.constraints.Size;       // Per la lunghezza delle stringhe
import java.math.BigDecimal; // Per il prezzo
import java.time.LocalDate;  // Per la data

public class GameRequestDTO {

    @NotBlank(message = "Il titolo non può essere vuoto")
    @Size(max = 100, message = "Il titolo non può superare i 100 caratteri")
    private String title;

    @NotBlank(message = "Il genere non può essere vuoto")
    @Size(max = 50, message = "Il genere non può superare i 50 caratteri")
    private String genre;

    @NotNull(message = "Il prezzo non può essere nullo")
    @DecimalMin(value = "0.00", inclusive = true, message = "Il prezzo non può essere negativo")
    private BigDecimal price;

    @NotNull(message = "La data di rilascio non può essere nulla")
    @PastOrPresent(message = "La data di rilascio non può essere nel futuro")
    private LocalDate releaseDate;

    @NotBlank(message = "Lo sviluppatore non può essere vuoto")
    @Size(max = 100, message = "Lo sviluppatore non può superare i 100 caratteri")
    private String developer;

    @NotBlank(message = "L'editore non può essere vuoto")
    @Size(max = 100, message = "L'editore non può superare i 100 caratteri")
    private String publisher;

    // Costruttore senza argomenti (necessario per la deserializzazione JSON)
    public GameRequestDTO() {
    }

    // Costruttore con argomenti
    public GameRequestDTO(String title, String genre, BigDecimal price, LocalDate releaseDate, String developer, String publisher) {
        this.title = title;
        this.genre = genre;
        this.price = price;
        this.releaseDate = releaseDate;
        this.developer = developer;
        this.publisher = publisher;
    }

    // Getter e Setter
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
        return "GameRequestDTO{" +
               "title='" + title + '\'' +
               ", genre='" + genre + '\'' +
               ", price=" + price +
               ", releaseDate=" + releaseDate +
               ", developer='" + developer + '\'' +
               ", publisher='" + publisher + '\'' +
               '}';
    }
} 