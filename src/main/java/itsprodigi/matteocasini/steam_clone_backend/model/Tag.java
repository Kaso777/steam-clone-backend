package itsprodigi.matteocasini.steam_clone_backend.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID auto-incrementante
    private Long id;

    @Column(name = "name", unique = true, nullable = false, length = 50)
    private String name; // Es. "Multiplayer", "Single-Player", "Indie", "Fantasy"

    // Relazione ManyToMany con Game
    // Questo è il lato non proprietario della relazione (usa mappedBy)
    // Significa che la tabella di join è definita sull'altro lato (Game)
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY) // Usa LAZY per evitare il caricamento di tutti i giochi quando si carica un tag
    private Set<Game> games = new HashSet<>(); // Inizializza per evitare NullPointer

    // Costruttori
    public Tag() {}

    public Tag(String name) {
        this.name = name;
    }

    // Getter e Setter per tutti i campi
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Game> getGames() {
        return games;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }

    // È buona pratica implementare equals() e hashCode() per entità in Set
    // Puoi generarle automaticamente con l'IDE, basandoti sull'ID o su un campo unico come 'name'
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return name != null && name.equals(tag.name); // Usa 'name' se vuoi unicità logica
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}