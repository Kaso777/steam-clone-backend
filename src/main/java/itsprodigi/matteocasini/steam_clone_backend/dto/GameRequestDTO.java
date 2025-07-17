package itsprodigi.matteocasini.steam_clone_backend.dto;

import jakarta.validation.constraints.DecimalMin;    // Per validare valori minimi decimali
import jakarta.validation.constraints.NotBlank;     // Per campi non vuoti o solo spazi
import jakarta.validation.constraints.NotNull;      // Per campi non nulli
import jakarta.validation.constraints.PastOrPresent; // Per date che non sono nel futuro
import jakarta.validation.constraints.Size;         // Per la lunghezza delle stringhe
import java.math.BigDecimal;    // Per il prezzo
import java.time.LocalDate;     // Per la data
import java.util.List;          // Per la lista di tag

/**
 * Data Transfer Object (DTO) per le richieste in ingresso (input) relative a Game.
 * Questo DTO è utilizzato quando un client invia dati al server per creare o aggiornare un gioco.
 * Contiene tutti i campi necessari per definire un gioco, inclusa la sua associazione con i tag.
 */
public class GameRequestDTO {

    @NotBlank(message = "Il titolo non può essere vuoto o composto solo da spazi.")
    @Size(max = 100, message = "Il titolo non può superare i 100 caratteri.")
    private String title;

    @NotNull(message = "Il prezzo non può essere nullo.")
    @DecimalMin(value = "0.00", inclusive = true, message = "Il prezzo non può essere negativo.")
    private BigDecimal price;

    @NotNull(message = "La data di rilascio non può essere nulla.")
    @PastOrPresent(message = "La data di rilascio non può essere nel futuro.")
    private LocalDate releaseDate;

    @NotBlank(message = "Lo sviluppatore non può essere vuoto o composto solo da spazi.")
    @Size(max = 100, message = "Lo sviluppatore non può superare i 100 caratteri.")
    private String developer;

    @NotBlank(message = "L'editore non può essere vuoto o composto solo da spazi.")
    @Size(max = 100, message = "L'editore non può superare i 100 caratteri.")
    private String publisher;

    // Nuovo campo: lista dei nomi dei tag associati al gioco.
    // Non è @NotNull o @NotBlank sulla lista stessa per permettere giochi senza tag,
    // ma i singoli nomi all'interno della lista possono essere validati nel servizio.
    private List<String> tagNames;

    // Costruttore senza argomenti (necessario per la deserializzazione JSON)
    public GameRequestDTO() {
    }

    // Costruttore con tutti gli argomenti, incluso il nuovo campo 'tagNames'
    public GameRequestDTO(String title, BigDecimal price, LocalDate releaseDate, String developer, String publisher, List<String> tagNames) {
        this.title = title;
        this.price = price;
        this.releaseDate = releaseDate;
        this.developer = developer;
        this.publisher = publisher;
        this.tagNames = tagNames;
    }

    // --- Getter ---
    public String getTitle() {
        return title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public String getDeveloper() {
        return developer;
    }

    public String getPublisher() {
        return publisher;
    }

    // Nuovo Getter per 'tagNames'
    public List<String> getTagNames() {
        return tagNames;
    }

    // --- Setter ---
    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    // Nuovo Setter per 'tagNames'
    public void setTagNames(List<String> tagNames) {
        this.tagNames = tagNames;
    }

    @Override
    public String toString() {
        return "GameRequestDTO{" +
               "title='" + title + '\'' +
               ", price=" + price +
               ", releaseDate=" + releaseDate +
               ", developer='" + developer + '\'' +
               ", publisher='" + publisher + '\'' +
               ", tagNames=" + tagNames + // Includi il nuovo campo nel toString
               '}';
    }
}