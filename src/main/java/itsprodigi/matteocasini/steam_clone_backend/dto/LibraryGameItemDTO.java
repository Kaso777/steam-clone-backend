package itsprodigi.matteocasini.steam_clone_backend.dto;

// Rappresenta ogni singolo gioco ma esportando solo i dati giusti a far parte di una libreria. È simile a UserGameResponseDTO,
// ma non include i dettagli dell'utente, poiché questi sono già forniti in UserLibraryResponseDTO.
// Questo DTO è progettato per essere utilizzato come elemento di una lista senza la ridondanza dei dettagli dell'utente.

import itsprodigi.matteocasini.steam_clone_backend.model.UserGame; // Importa l'entità UserGame per la mappatura
import java.time.LocalDate; // Per la data di acquisto

/**
 * Data Transfer Object (DTO) che rappresenta un singolo gioco all'interno della libreria di un utente.
 * Questo DTO è progettato per essere utilizzato come elemento di una lista (es. in UserLibraryResponseDTO)
 * e include i dettagli del gioco, la data di acquisto e le ore giocate, ma SENZA i dettagli dell'utente.
 * L'esclusione dei dettagli dell'utente evita la ridondanza quando si visualizza l'intera libreria di un utente,
 * dove i dettagli dell'utente sono già forniti a un livello superiore.
 */
public class LibraryGameItemDTO {

    // Dettagli del gioco. Nidifica GameResponseDTO per fornire informazioni complete sul gioco.
    private GameResponseDTO game;
    
    // La data in cui il gioco è stato acquistato o aggiunto alla libreria dell'utente.
    private LocalDate purchaseDate;
    
    // Le ore totali giocate dall'utente a questo specifico gioco.
    private int playtimeHours;

    // Costruttore senza argomenti (necessario per la deserializzazione JSON/Spring)
    public LibraryGameItemDTO() {
    }

    /**
     * Costruttore che mappa i dati da un'entità UserGame a questo DTO.
     * Questo costruttore è utile nel livello di servizio per convertire le entità
     * recuperate dal database in un formato adatto alla risposta API, quando
     * si vuole rappresentare solo il gioco e i suoi attributi nella libreria, senza l'utente.
     * NOTA: Assicurati che l'entità Game all'interno di UserGame sia caricata
     * (non lazy-loaded) quando questo costruttore viene chiamato, altrimenti potresti
     * incorrere in LazyInitializationException.
     *
     * @param userGame L'entità UserGame da cui mappare i dati.
     */
    public LibraryGameItemDTO(UserGame userGame) {
        // Mappa l'entità Game nidificata in un GameResponseDTO.
        // Si assume che l'entità Game sia già caricata.
        this.game = new GameResponseDTO(userGame.getGame());
        
        this.purchaseDate = userGame.getPurchaseDate();
        this.playtimeHours = userGame.getPlaytimeHours();
    }

    /**
     * Costruttore con tutti i campi del DTO.
     * Utile per creare istanze del DTO direttamente nel codice, ad esempio nei test.
     *
     * @param game I dettagli del gioco come GameResponseDTO.
     * @param purchaseDate La data di acquisto.
     * @param playtimeHours Le ore giocate.
     */
    public LibraryGameItemDTO(GameResponseDTO game, LocalDate purchaseDate, int playtimeHours) {
        this.game = game;
        this.purchaseDate = purchaseDate;
        this.playtimeHours = playtimeHours;
    }

    // --- Getter ---
    public GameResponseDTO getGame() {
        return game;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public int getPlaytimeHours() {
        return playtimeHours;
    }

    // --- Setter ---
    public void setGame(GameResponseDTO game) {
        this.game = game;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public void setPlaytimeHours(int playtimeHours) {
        this.playtimeHours = playtimeHours;
    }

    @Override
    public String toString() {
        return "LibraryGameItemDTO{" +
               "game=" + game +
               ", purchaseDate=" + purchaseDate +
               ", playtimeHours=" + playtimeHours +
               '}';
    }
}