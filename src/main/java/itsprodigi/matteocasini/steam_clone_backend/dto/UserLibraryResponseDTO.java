package itsprodigi.matteocasini.steam_clone_backend.dto;

import itsprodigi.matteocasini.steam_clone_backend.model.User; // Importa l'entità User per il costruttore
import itsprodigi.matteocasini.steam_clone_backend.model.UserGame; // Importa l'entità UserGame per la mappatura
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Data Transfer Object (DTO) per le risposte in uscita (output) che rappresentano l'intera libreria di un utente.
 * Questa classe aggrega le informazioni di base dell'utente e una lista dettagliata
 * di tutti i giochi presenti nella sua libreria.
 *
 * È progettata per evitare la ridondanza dei dati dell'utente, presentando
 * le informazioni dell'utente una sola volta e poi elencando i giochi associati
 * tramite LibraryGameItemDTO.
 */
public class UserLibraryResponseDTO {
    // Dettagli dell'utente proprietario della libreria.
    // Appare una sola volta in questo DTO aggregato.
    private UserResponseDTO user;
    
    // La lista dei giochi nella libreria dell'utente.
    // Ogni elemento è un LibraryGameItemDTO, che include i dettagli del gioco
    // e gli attributi della relazione (es. data di acquisto, ore giocate),
    // ma NON include i dettagli dell'utente per evitare ripetizioni.
    private List<LibraryGameItemDTO> gamesInLibrary;

    // Costruttore senza argomenti (necessario per la deserializzazione JSON/Spring)
    public UserLibraryResponseDTO() {
    }

    /**
     * Costruttore che accetta un'entità User e una lista di entità UserGame,
     * e le mappa in questo DTO. Questo è il costruttore preferito per costruire
     * la risposta della libreria utente a partire dai dati recuperati dal database.
     *
     * NOTA: Assicurati che le entità User e Game all'interno di ogni UserGame
     * siano caricate (non lazy-loaded) quando questo costruttore viene chiamato,
     * altrimenti potresti incorrere in LazyInitializationException.
     *
     * @param user L'entità User proprietaria della libreria.
     * @param userGames Una lista di entità UserGame appartenenti a questo utente.
     */
    public UserLibraryResponseDTO(User user, List<UserGame> userGames) {
        // Mappa l'entità User a un UserResponseDTO per i dettagli dell'utente.
        this.user = new UserResponseDTO(user);
        
        // Mappa ogni entità UserGame a un LibraryGameItemDTO.
        // LibraryGameItemDTO non contiene i dettagli dell'utente, risolvendo la ridondanza.
        this.gamesInLibrary = userGames.stream()
                                        .map(LibraryGameItemDTO::new) // Utilizza il costruttore di LibraryGameItemDTO
                                        .collect(Collectors.toList());
    }

    /**
     * Costruttore con tutti i campi del DTO.
     * Utile per creare istanze del DTO direttamente nel codice, ad esempio nei test,
     * o quando i sub-DTO sono già stati preparati.
     *
     * @param user I dettagli dell'utente come UserResponseDTO.
     * @param gamesInLibrary La lista dei giochi nella libreria come List<LibraryGameItemDTO>.
     */
    public UserLibraryResponseDTO(UserResponseDTO user, List<LibraryGameItemDTO> gamesInLibrary) {
        this.user = user;
        this.gamesInLibrary = gamesInLibrary;
    }

    // --- Getter ---
    public UserResponseDTO getUser() {
        return user;
    }

    public List<LibraryGameItemDTO> getGamesInLibrary() { // Tipo cambiato a List<LibraryGameItemDTO>
        return gamesInLibrary;
    }

    // --- Setter ---
    public void setUser(UserResponseDTO user) {
        this.user = user;
    }

    public void setGamesInLibrary(List<LibraryGameItemDTO> gamesInLibrary) { // Tipo cambiato a List<LibraryGameItemDTO>
        this.gamesInLibrary = gamesInLibrary;
    }

    // --- Metodi equals, hashCode, toString ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserLibraryResponseDTO that = (UserLibraryResponseDTO) o;
        return Objects.equals(user, that.user) && Objects.equals(gamesInLibrary, that.gamesInLibrary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, gamesInLibrary);
    }

    @Override
    public String toString() {
        return "UserLibraryResponseDTO{" +
               "user=" + user +
               ", gamesInLibrary=" + gamesInLibrary +
               '}';
    }
}