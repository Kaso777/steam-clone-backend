package itsprodigi.matteocasini.steam_clone_backend.controller;

import itsprodigi.matteocasini.steam_clone_backend.service.UserGameService; // Importa l'interfaccia del servizio UserGame
import itsprodigi.matteocasini.steam_clone_backend.dto.UserGameRequestDTO;  // DTO per le richieste (input) relative a UserGame
import itsprodigi.matteocasini.steam_clone_backend.dto.UserGameResponseDTO; // DTO per la risposta (output) di una singola voce UserGame
import itsprodigi.matteocasini.steam_clone_backend.dto.UserLibraryResponseDTO; // DTO per la risposta (output) dell'intera libreria utente
import jakarta.validation.Valid; // Per attivare la validazione Bean Validation sui DTO
import org.springframework.beans.factory.annotation.Autowired; // Per l'iniezione delle dipendenze
import org.springframework.http.HttpStatus; // Per i codici di stato HTTP
import org.springframework.http.ResponseEntity; // Per costruire le risposte HTTP
import org.springframework.web.bind.annotation.*; // Per le annotazioni REST (RestController, RequestMapping, GetMapping, PostMapping, PutMapping, DeleteMapping)

import java.util.List; // Per liste di DTO
import java.util.UUID; // Per gli ID univoci (UUID) di utente e gioco

/**
 * Controller REST per la gestione delle operazioni relative alla libreria di giochi degli utenti.
 * Questo controller espone gli endpoint API per aggiungere, recuperare, aggiornare e rimuovere
 * giochi dalla libreria di un utente, nonché per visualizzare l'intera libreria.
 * Interagisce con il livello di servizio (UserGameService) per eseguire la logica di business.
 */
@RestController // Indica a Spring che questa classe è un controller REST e gestisce le richieste HTTP.
@RequestMapping("/api") // Definisce il percorso base per gli endpoint di questo controller.
public class UserGameController {

    // Inietta l'interfaccia del servizio UserGame. Spring si occuperà di iniettare
    // l'implementazione concreta (UserGameServiceImpl) grazie all'annotazione @Service su di essa.
    private final UserGameService userGameService;

    @Autowired // Costruttore per l'iniezione delle dipendenze.
    public UserGameController(UserGameService userGameService) {
        this.userGameService = userGameService;
    }

    /**
     * Endpoint POST per aggiungere un gioco alla libreria di un utente.
     * Esempio di URL: POST /api/user-games
     * Il corpo della richiesta deve contenere un UserGameRequestDTO con userUuid, gameUuid, purchaseDate e playtimeHours.
     *
     * @param userGameRequestDTO Il DTO contenente i dati per la nuova voce UserGame.
     * Annotato con @Valid per attivare la validazione dei campi del DTO.
     * @return ResponseEntity contenente il UserGameResponseDTO della voce creata e lo stato HTTP 201 CREATED.
     * In caso di errore (es. utente/gioco non trovati, associazione già esistente), restituisce 400 BAD REQUEST.
     */
    @PostMapping("/user-games") // Mappa le richieste POST all'URL /api/user-games
    public ResponseEntity<UserGameResponseDTO> addGameToUserLibrary(@Valid @RequestBody UserGameRequestDTO userGameRequestDTO) {
        try {
            // Chiama il servizio per aggiungere il gioco alla libreria.
            UserGameResponseDTO newUserGame = userGameService.addGameToUserLibrary(userGameRequestDTO);
            // Restituisce una risposta HTTP 201 Created con la nuova voce UserGame nel corpo.
            return new ResponseEntity<>(newUserGame, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Gestisce eccezioni come utente/gioco non trovati o associazione duplicata.
            // In un'applicazione reale, si userebbero eccezioni custom e un GlobalExceptionHandler.
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint GET per recuperare una specifica voce UserGame tramite gli ID di utente e gioco.
     * Esempio di URL: GET /api/users/{userUuid}/games/{gameUuid}
     *
     * @param userUuid L'UUID dell'utente.
     * @param gameUuid L'UUID del gioco.
     * @return ResponseEntity contenente il UserGameResponseDTO e lo stato HTTP (200 OK se trovato, 404 NOT FOUND altrimenti).
     */
    @GetMapping("/users/{userUuid}/games/{gameUuid}") // Mappa le richieste GET a /api/users/{userUuid}/games/{gameUuid}
    public ResponseEntity<UserGameResponseDTO> getUserGameByIds(@PathVariable UUID userUuid, @PathVariable UUID gameUuid) {
        // Chiama il servizio per recuperare la specifica associazione UserGame.
        return userGameService.getUserGameByIds(userUuid, gameUuid)
                // Se l'associazione è presente, restituisce 200 OK con il DTO.
                .map(userGameDto -> new ResponseEntity<>(userGameDto, HttpStatus.OK))
                // Se non trovata, restituisce 404 NOT FOUND.
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint GET per recuperare l'intera libreria di giochi di un utente specifico.
     * Esempio di URL: GET /api/users/{userUuid}/library
     *
     * @param userUuid L'UUID dell'utente di cui recuperare la libreria.
     * @return ResponseEntity contenente il UserLibraryResponseDTO (con dettagli utente e lista giochi)
     * e lo stato HTTP 200 OK.
     */
    @GetMapping("/users/{userUuid}/library") // Mappa le richieste GET a /api/users/{userUuid}/library
    public ResponseEntity<UserLibraryResponseDTO> getUserLibrary(@PathVariable UUID userUuid) {
        // Chiama il servizio per recuperare l'intera libreria dell'utente.
        UserLibraryResponseDTO userLibrary = userGameService.getUserLibrary(userUuid);
        // Restituisce 200 OK. La lista dei giochi sarà vuota se l'utente non ne ha.
        return new ResponseEntity<>(userLibrary, HttpStatus.OK);
    }

    /**
     * Endpoint PUT per aggiornare gli attributi di una voce UserGame esistente (es. ore giocate).
     * Esempio di URL: PUT /api/users/{userUuid}/games/{gameUuid}
     * Il corpo della richiesta deve contenere un UserGameRequestDTO con i dati aggiornati (es. playtimeHours).
     *
     * @param userUuid L'UUID dell'utente.
     * @param gameUuid L'UUID del gioco.
     * @param userGameRequestDTO Il DTO contenente i dati aggiornati.
     * @Valid: Attiva la validazione sui campi del DTO di input.
     * @return ResponseEntity contenente il UserGameResponseDTO della voce aggiornata e lo stato HTTP 200 OK.
     * In caso di errore (es. associazione non trovata), restituisce 404 NOT FOUND.
     */
    @PutMapping("/users/{userUuid}/games/{gameUuid}") // Mappa le richieste PUT a /api/users/{userUuid}/games/{gameUuid}
    public ResponseEntity<UserGameResponseDTO> updateUserGame(
            @PathVariable UUID userUuid,
            @PathVariable UUID gameUuid,
            @Valid @RequestBody UserGameRequestDTO userGameRequestDTO) {
        try {
            // Chiama il servizio per aggiornare la voce UserGame.
            UserGameResponseDTO updatedUserGame = userGameService.updateUserGame(userUuid, gameUuid, userGameRequestDTO);
            // Restituisce 200 OK con il DTO aggiornato.
            return new ResponseEntity<>(updatedUserGame, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Gestisce errori come associazione non trovata.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint DELETE per rimuovere un gioco dalla libreria di un utente.
     * Esempio di URL: DELETE /api/users/{userUuid}/games/{gameUuid}
     *
     * @param userUuid L'UUID dell'utente.
     * @param gameUuid L'UUID del gioco da rimuovere.
     * @return ResponseEntity con stato HTTP 204 NO CONTENT se l'eliminazione ha successo.
     * In caso di errore (es. associazione non trovata), restituisce 404 NOT FOUND.
     */
    @DeleteMapping("/users/{userUuid}/games/{gameUuid}") // Mappa le richieste DELETE a /api/users/{userUuid}/games/{gameUuid}
    public ResponseEntity<Void> removeGameFromUserLibrary(@PathVariable UUID userUuid, @PathVariable UUID gameUuid) {
        try {
            // Chiama il servizio per rimuovere l'associazione UserGame.
            userGameService.removeGameFromUserLibrary(userUuid, gameUuid);
            // Restituisce 204 No Content, indicando successo senza contenuto di risposta.
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            // Gestisce errori come associazione non trovata.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint GET per recuperare tutte le voci UserGame esistenti in tutte le librerie.
     * Esempio di URL: GET /api/user-games/all
     * Utile per scopi amministrativi o di debugging.
     *
     * @return ResponseEntity contenente una lista di UserGameResponseDTO e lo stato HTTP 200 OK.
     */
    @GetMapping("/user-games/all") // Mappa le richieste GET a /api/user-games/all
    public ResponseEntity<List<UserGameResponseDTO>> getAllUserGames() {
        // Chiama il servizio per recuperare tutte le associazioni UserGame.
        List<UserGameResponseDTO> userGames = userGameService.getAllUserGames();
        // Restituisce 200 OK con la lista delle associazioni.
        return new ResponseEntity<>(userGames, HttpStatus.OK);
    }
}