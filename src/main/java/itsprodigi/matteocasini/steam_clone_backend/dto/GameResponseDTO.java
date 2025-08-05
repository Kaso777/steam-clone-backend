package itsprodigi.matteocasini.steam_clone_backend.dto;

import itsprodigi.matteocasini.steam_clone_backend.model.Game;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class GameResponseDTO {

    private UUID id;
    private String title;
    private BigDecimal price;
    private LocalDate releaseDate;
    private String developer;
    private String publisher;

    /**
     * Lista dei tag associati al gioco.
     * Ogni tag è rappresentato come un oggetto TagDTO (contenente id e nome).
     */
    private List<TagDTO> tags;

    // Costruttori
    public GameResponseDTO() {
    }

    /**
     * Costruttore che converte un'entità Game in un GameResponseDTO.
     * Usato solitamente nei Service per restituire i dati al client.
     *
     * @param game L'entità Game da cui estrarre i dati.
     */
    public GameResponseDTO(Game game) {
        this.id = game.getId();
        this.title = game.getTitle();
        this.price = game.getPrice();
        this.releaseDate = game.getReleaseDate();
        this.developer = game.getDeveloper();
        this.publisher = game.getPublisher();

        // Conversione della lista di Tag (entità) in lista di TagDTO
        if (game.getTags() != null) {
            this.tags = game.getTags().stream()
                    .map(tag -> new TagDTO(tag.getId(), tag.getName()))
                    .collect(Collectors.toList());
        } else {
            this.tags = List.of(); // Lista vuota se non ci sono tag
        }
    }

    /**
     * Costruttore completo con tutti i campi del DTO.
     * Utile, ad esempio, per test o creazione manuale.
     *
     * @param id          ID del gioco
     * @param title       Titolo
     * @param price       Prezzo
     * @param releaseDate Data di rilascio
     * @param developer   Sviluppatore
     * @param publisher   Editore
     * @param tags        Lista di tag (come TagDTO)
     */
    public GameResponseDTO(UUID id, String title, BigDecimal price, LocalDate releaseDate,
            String developer, String publisher, List<TagDTO> tags) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.releaseDate = releaseDate;
        this.developer = developer;
        this.publisher = publisher;
        this.tags = tags;
    }

    // Getter

    public UUID getId() {
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

    public List<TagDTO> getTags() {
        return tags;
    }

    // Setter

    public void setId(UUID id) {
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

    public void setTags(List<TagDTO> tags) {
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
                ", tags=" + tags +
                '}';
    }
}