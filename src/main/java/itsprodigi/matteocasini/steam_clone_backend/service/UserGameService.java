package itsprodigi.matteocasini.steam_clone_backend.service;

import itsprodigi.matteocasini.steam_clone_backend.dto.UserGameRequestDTO;  // DTO per le richieste (aggiunta/aggiornamento di un gioco nella libreria)
import itsprodigi.matteocasini.steam_clone_backend.dto.UserGameResponseDTO; // DTO per la risposta di una singola voce UserGame
import itsprodigi.matteocasini.steam_clone_backend.dto.UserLibraryResponseDTO; // DTO per la risposta dell'intera libreria utente
import java.util.List;
import java.util.Optional;
import java.util.UUID; // Per gli ID di utente e gioco

/**
 * Interfaccia per il servizio di gestione delle relazioni Utente-Gioco (libreria).
 * Definisce il contratto dei metodi disponibili per interagire con le voci della libreria degli utenti,
 * garantendo il disaccoppiamento tra l'implementazione concreta e i componenti che la utilizzano.
 */
public interface UserGameService {

    /**
     * Aggiunge un gioco alla libreria di un utente.
     * Questo metodo crea una nuova voce UserGame basandosi sui dati forniti.
     * @param userGameRequestDTO DTO contenente gli UUID dell'utente e del gioco, e la data di acquisto/ore giocate.
     * @return Il UserGameResponseDTO della voce della libreria appena creata.
     */
    UserGameResponseDTO addGameToUserLibrary(UserGameRequestDTO userGameRequestDTO);

    /**
     * Recupera una specifica voce UserGame tramite l'ID dell'utente e l'ID del gioco.
     * @param userUuid L'UUID dell'utente.
     * @param gameUuid L'UUID del gioco.
     * @return Un Optional contenente il UserGameResponseDTO se la voce esiste, altrimenti Optional.empty().
     */
    Optional<UserGameResponseDTO> getUserGameByIds(UUID userUuid, UUID gameUuid);

    /**
     * Recupera l'intera libreria di giochi per un utente specifico.
     * @param userUuid L'UUID dell'utente di cui recuperare la libreria.
     * @return Un UserLibraryResponseDTO contenente i dettagli dell'utente e la lista dei suoi giochi.
     * Restituisce un DTO con lista vuota se l'utente non ha giochi o non esiste.
     */
    UserLibraryResponseDTO getUserLibrary(UUID userUuid);

    /**
     * Aggiorna gli attributi di una voce UserGame esistente (es. ore giocate).
     * @param userUuid L'UUID dell'utente.
     * @param gameUuid L'UUID del gioco.
     * @param userGameRequestDTO DTO contenente i dati aggiornati (es. nuove ore giocate).
     * @return Il UserGameResponseDTO della voce della libreria aggiornata.
     */
    UserGameResponseDTO updateUserGame(UUID userUuid, UUID gameUuid, UserGameRequestDTO userGameRequestDTO);

    /**
     * Rimuove un gioco dalla libreria di un utente.
     * @param userUuid L'UUID dell'utente.
     * @param gameUuid L'UUID del gioco da rimuovere.
     */
    void removeGameFromUserLibrary(UUID userUuid, UUID gameUuid);

    /**
     * Recupera tutti i giochi presenti in tutte le librerie (tutte le voci UserGame).
     * @return Una lista di UserGameResponseDTO.
     */
    List<UserGameResponseDTO> getAllUserGames();
}