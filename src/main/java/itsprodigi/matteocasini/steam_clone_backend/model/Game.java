package itsprodigi.matteocasini.steam_clone_backend.model;

import jakarta.persistence.*; // Importa tutte le annotazioni JPA/Hibernate
import java.math.BigDecimal; // Per prezzi precisi
import java.time.LocalDate;   // Per la data di rilascio
import java.util.HashSet; // Per le collezioni Set (utile per relazioni ManyToMany e OneToMany)
import java.util.UUID;        // Per l'ID del gioco (UUID)
import org.hibernate.annotations.UuidGenerator; // Per la generazione UUID degli ID
import java.util.Set; // Interfaccia Set per la relazione ManyToMany con Tag

@Entity // Indica che questa classe è un'entità JPA e sarà mappata a una tabella di database
@Table(name = "games") // Specifica il nome della tabella nel database
public class Game {

    @Id // Indica che questo campo è la chiave primaria della tabella
    @GeneratedValue // Permette a Hibernate di generare il valore per questo ID
    @UuidGenerator // Specifica che il generatore di valori sarà per UUID (usato da Hibernate)
    @Column(name = "id", updatable = false, nullable = false) // Mappa il campo alla colonna 'id', non aggiornabile e non null
    private UUID id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @Column(name = "developer", nullable = false, length = 100)
    private String developer;

    @Column(name = "publisher", nullable = false, length = 100)
    private String publisher;


    // --- RELAZIONI JPA ---

    // Relazione ManyToMany con Tag
    // Un gioco può avere molti Tag (es. "Azione", "Multiplayer", "Indie").
    // Un Tag può essere associato a molti giochi.
    // Questo è il "lato proprietario" della relazione, dove viene definita la tabella di join.
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    // fetch = FetchType.LAZY: I Tag verranno caricati solo quando vengono esplicitamente richiesti (es. game.getTags()).
    //                        Questo migliora le performance non caricando dati non necessari.
    // cascade = {CascadeType.PERSIST, CascadeType.MERGE}: Le operazioni di persistenza e merge su Game
    //                                                     si propagano ai Tag associati. Ad esempio, se salvi un Game
    //                                                     e gli associ un nuovo Tag che non esiste ancora nel DB,
    //                                                     anche il Tag verrà salvato automaticamente.
    @JoinTable(
        name = "game_tags", // Nome della tabella di join che Hibernate creerà nel database (es. game_tags)
        joinColumns = @JoinColumn(name = "game_id"), // Definisce la colonna della chiave esterna per l'ID del Game nella tabella di join
        inverseJoinColumns = @JoinColumn(name = "tag_id") // Definisce la colonna della chiave esterna per l'ID del Tag nella tabella di join
    )
    private Set<Tag> tags = new HashSet<>(); // La collezione di Tag associati a questo gioco. Inizializzata per prevenire NullPointerException.

    // Relazione OneToMany con UserGame (l'entità intermedia per la libreria Utente-Gioco)
    // Un gioco può apparire in molte "entry" di libreria degli utenti.
    // Questo è il "lato non proprietario" della relazione, poiché la FK è in UserGame.
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    // mappedBy = "game": Indica che la relazione è mappata dal campo 'game' nell'entità UserGame.
    //                    Significa che UserGame è il lato proprietario di questa parte della relazione (contiene la FK).
    // cascade = CascadeType.ALL: Tutte le operazioni su Game (persist, merge, remove, ecc.) si propagano a UserGame.
    // orphanRemoval = true: Se un'istanza di UserGame viene disassociata da un Game (es. rimossa dal set 'userGames'),
    //                       verrà eliminata automaticamente dal database.
    // fetch = FetchType.LAZY: Le UserGame associate verranno caricate solo quando esplicitamente richieste.
    private Set<UserGame> userGames = new HashSet<>(); // La collezione di UserGame associate a questo gioco.

    // --- COSTRUTTORI ---
    public Game() {
        // Costruttore di default richiesto da JPA
    }

    // Costruttore con i campi essenziali per la creazione di un gioco.
    // NOTA: 'genre' rimosso da qui. I tag verranno aggiunti separatamente dopo la creazione.
    public Game(String title, BigDecimal price, LocalDate releaseDate, String developer, String publisher) {
        this.title = title;
        this.price = price;
        this.releaseDate = releaseDate;
        this.developer = developer;
        this.publisher = publisher;
    }

    // --- GETTER E SETTER ---
    // Questi metodi consentono l'accesso e la modifica ai campi dell'entità.

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

    // Getter e Setter per la relazione ManyToMany con Tag
    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    // Metodi helper per aggiungere/rimuovere Tag.
    // Essenziali per mantenere la bidirezionalità della relazione negli oggetti Java in memoria.
    public void addTag(Tag tag) {
        this.tags.add(tag); // Aggiunge il tag a questo gioco
        // Assicurati che anche il lato del Tag punti a questo gioco per coerenza in memoria.
        // La condizione 'if' evita loop infiniti in caso di aggiunte ricorsive.
        if (!tag.getGames().contains(this)) {
            tag.getGames().add(this);
        }
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag); // Rimuove il tag da questo gioco
        // Assicurati che anche il lato del Tag non punti più a questo gioco per coerenza in memoria.
        // La condizione 'if' evita loop infiniti in caso di rimozioni ricorsive.
        if (tag.getGames().contains(this)) {
            tag.getGames().remove(this);
        }
    }

    // Getter e Setter per la relazione OneToMany con UserGame
    public Set<UserGame> getUserGames() {
        return userGames;
    }

    public void setUserGames(Set<UserGame> userGames) {
        this.userGames = userGames;
    }

    // Metodi helper per aggiungere/rimuovere UserGame.
    // Essenziali per mantenere la bidirezionalità della relazione negli oggetti Java in memoria.
    public void addUserGame(UserGame userGame) {
        this.userGames.add(userGame); // Aggiunge l'entry della libreria a questo gioco
        // Assicurati che l'entry della libreria punti a questo gioco per coerenza.
        userGame.setGame(this);
    }

    public void removeUserGame(UserGame userGame) {
        this.userGames.remove(userGame); // Rimuove l'entry della libreria da questo gioco
        // Scollega l'entry della libreria da questo gioco. Con orphanRemoval=true, questo potrebbe portare all'eliminazione dell'entry.
        userGame.setGame(null);
    }

    // --- METODI OVERRIDDEN ---
    // È buona pratica implementare equals() e hashCode() per le entità JPA,
    // specialmente quando le si usano in collezioni (Set) o in relazioni bidirezionali.
    // Si basano tipicamente sull'ID dell'entità per l'uguaglianza.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return id != null && id.equals(game.id); // L'uguaglianza si basa sull'ID
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0; // L'hash code si basa sull'ID
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", releaseDate=" + releaseDate +
                ", developer='" + developer + '\'' +
                ", publisher='" + publisher + '\'' +
                '}';
    }
}