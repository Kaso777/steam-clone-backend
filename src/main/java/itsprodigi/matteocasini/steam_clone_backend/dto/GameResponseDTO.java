package itsprodigi.matteocasini.steam_clone_backend.dto;

import itsprodigi.matteocasini.steam_clone_backend.model.Game; // Importa l'entità Game
import java.math.BigDecimal; // Per il prezzo
import java.time.LocalDate; // Per la data
import java.util.List; // Per la lista di tag
import java.util.UUID; // Per l'ID univoco del gioco
import java.util.stream.Collectors; // Per mappare liste

/**
 * Data Transfer Object (DTO) per le risposte in uscita (output) relative a Game.
 * Questo DTO è utilizzato quando il server invia i dati di un gioco al client
 * (es. in risposta a un metodo GET, o dopo una POST/PUT di successo).
 *
 * Contiene i campi essenziali del gioco e include una lista dei tag associati.
 */
public class GameResponseDTO {
    // ID univoco del gioco. Rinominato da 'uuid' a 'id' per coerenza con l'entità Game.
    private UUID id;
    private String title;
    private BigDecimal price;
    private LocalDate releaseDate;
    private String developer;
    private String publisher;

    // Nuovo campo: lista dei tag associati al gioco.
    // Utilizza TagDTO per fornire ID e nome di ogni tag.
    private List<TagDTO> tags;

    // Costruttore senza argomenti (necessario per la deserializzazione JSON)
    public GameResponseDTO() {
    }

    /**
     * Costruttore che prende un'entità Game e la mappa al DTO.
     * Questo costruttore è utile nel Service per convertire le entità
     * recuperate dal database in un formato adatto alla risposta API.
     * NOTA: Assicurati che la collezione di tag dell'entità Game sia caricata
     * (non lazy-loaded) quando questo costruttore viene chiamato, altrimenti potresti
     * incorrere in LazyInitializationException.
     *
     * @param game L'entità Game da cui mappare i dati.
     */
    public GameResponseDTO(Game game) {
        this.id = game.getId(); // Mappa l'ID dell'entità al campo 'id' del DTO
        this.title = game.getTitle();
        this.price = game.getPrice();
        this.releaseDate = game.getReleaseDate();
        this.developer = game.getDeveloper();
        this.publisher = game.getPublisher();
        
        // Mappa la lista di Tag dell'entità Game a una lista di TagDTO.
        // Si assume che la collezione di tag sia già caricata.
        if (game.getTags() != null) {
            this.tags = game.getTags().stream()
                                    .map(tag -> new TagDTO(tag.getId(), tag.getName())) // Mappa ogni Tag a un TagDTO
                                    .collect(Collectors.toList());
        } else {
            this.tags = List.of(); // Inizializza a lista vuota se non ci sono tag
        }
    }

    /**
     * Costruttore con tutti i campi del DTO.
     * Utile per creare un DTO direttamente con i valori, ad esempio nei test.
     *
     * @param id L'ID univoco del gioco.
     * @param title Il titolo del gioco.
     * @param price Il prezzo del gioco.
     * @param releaseDate La data di rilascio del gioco.
     * @param developer Lo sviluppatore del gioco.
     * @param publisher L'editore del gioco.
     * @param tags La lista dei tag associati al gioco.
     */
    public GameResponseDTO(UUID id, String title, BigDecimal price, LocalDate releaseDate, String developer, String publisher, List<TagDTO> tags) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.releaseDate = releaseDate;
        this.developer = developer;
        this.publisher = publisher;
        this.tags = tags;
    }

    // --- Getters ---
    public UUID getId() { // Getter per 'id'
        return id;
    }

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

    public List<TagDTO> getTags() { // Getter per 'tags'
        return tags;
    }

    // --- Setters ---
    public void setId(UUID id) { // Setter per 'id'
        this.id = id;
    }

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

    public void setTags(List<TagDTO> tags) { // Setter per 'tags'
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "GameResponseDTO{" +
               "id=" + id +
               ", title='" + title + '\'' +
               ", price=" + price +
               ", releaseDate=" + releaseDate +
               ", developer='" + developer + '\'' +
               ", publisher='" + publisher + '\'' +
               ", tags=" + tags + // Includi il nuovo campo nel toString
               '}';
    }
}