package itsprodigi.matteocasini.steam_clone_backend.dto;


import jakarta.validation.constraints.NotBlank; // Per validare che una stringa non sia nulla, vuota o solo spazi
import jakarta.validation.constraints.Size;    // Per validare la lunghezza di una stringa

/**
 * Data Transfer Object (DTO) per l'entità Tag.
 * Questo DTO è utilizzato sia per le richieste in ingresso (input), quando un client
 * invia dati relativi a un tag (es. per crearne uno nuovo o aggiornarne uno esistente),
 * sia per le risposte in uscita (output), quando il server invia i dati di un tag al client.
 *
 * Contiene i campi essenziali dell'entità Tag. L'ID è incluso in questo DTO in modo
 * che possa essere restituito nelle risposte (es. dopo la creazione di un tag)
 * e, opzionalmente, utilizzato per identificare il tag nelle richieste di aggiornamento.
 */
public class TagDTO {

    private Long id; // L'ID del tag. Sarà null nelle richieste di creazione, popolato nelle risposte.

    @NotBlank(message = "Il nome del tag non può essere vuoto o composto solo da spazi.")
    @Size(min = 1, max = 50, message = "Il nome del tag deve contenere tra 1 e 50 caratteri.")
    private String name;

    // Costruttore senza argomenti (necessario per la deserializzazione JSON/Spring)
    public TagDTO() {
    }

    // Costruttore per creare un TagDTO con ID e nome
    public TagDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Costruttore per creare un TagDTO solo con il nome (utile per le richieste di creazione)
    public TagDTO(String name) {
        this.name = name;
    }

    // Getter
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Setter
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TagDTO{" +
               "id=" + id +
               ", name='" + name + '\'' +
               '}';
    }
}