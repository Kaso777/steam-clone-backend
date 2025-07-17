package itsprodigi.matteocasini.steam_clone_backend.controller;

import itsprodigi.matteocasini.steam_clone_backend.service.GameService;         // Importa l'interfaccia del servizio Game
import itsprodigi.matteocasini.steam_clone_backend.dto.GameRequestDTO;       // DTO per le richieste (input) di un Game
import itsprodigi.matteocasini.steam_clone_backend.dto.GameResponseDTO;      // DTO per la risposta (output) di un Game
import itsprodigi.matteocasini.steam_clone_backend.exception.ResourceNotFoundException; // Importa l'eccezione personalizzata
import jakarta.validation.Valid; // Per attivare la validazione Bean Validation sui DTO
import org.springframework.beans.factory.annotation.Autowired; // Per l'iniezione delle dipendenze
import org.springframework.http.HttpStatus; // Per i codici di stato HTTP
import org.springframework.http.ResponseEntity; // Per costruire le risposte HTTP
import org.springframework.web.bind.annotation.*; // Per le annotazioni REST

import java.util.List; // Per liste di DTO
import java.util.UUID; // Per gli ID univoci (UUID) dei giochi

/**
 * Controller REST per la gestione delle operazioni relative ai giochi.
 * Questo controller espone gli endpoint API per creare, recuperare, aggiornare e eliminare giochi,
 * nonché per eseguire ricerche basate su vari criteri (titolo, tag, sviluppatore, editore).
 * Interagisce con il livello di servizio (GameService) per eseguire la logica di business.
 */
@RestController // Indica a Spring che questa classe è un controller REST.
@RequestMapping("/api/games") // Definisce il percorso base per tutti gli endpoint di questo controller.
public class GameController {

    private final GameService gameService; // Inietta l'interfaccia del servizio Game.

    @Autowired // Costruttore per l'iniezione delle dipendenze.
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Endpoint POST per creare un nuovo gioco.
     * Esempio di URL: POST /api/games
     * Il corpo della richiesta deve contenere un GameRequestDTO con i dettagli del gioco e i nomi dei tag.
     *
     * @param gameRequestDTO Il DTO contenente i dati per il nuovo gioco.
     * Annotato con @Valid per attivare la validazione Bean Validation.
     * @return ResponseEntity contenente il GameResponseDTO del gioco creato e lo stato HTTP 201 CREATED.
     * In caso di errore (es. titolo duplicato), restituisce 400 BAD REQUEST.
     */
    @PostMapping // Mappa le richieste POST all'URL base /api/games
    public ResponseEntity<GameResponseDTO> createGame(@Valid @RequestBody GameRequestDTO gameRequestDTO) {
        try {
            // Chiama il servizio per creare il gioco.
            GameResponseDTO newGame = gameService.createGame(gameRequestDTO);
            // Restituisce una risposta HTTP 201 Created con il gioco creato nel corpo.
            return new ResponseEntity<>(newGame, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Gestisce eccezioni come titolo duplicato.
            // In un'applicazione reale, si userebbero eccezioni custom e un GlobalExceptionHandler per risposte più specifiche.
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint GET per recuperare un gioco tramite il suo ID.
     * Esempio di URL: GET /api/games/{id}
     *
     * @param id L'ID (UUID) del gioco da recuperare.
     * @return ResponseEntity contenente il GameResponseDTO e lo stato HTTP (200 OK se trovato, 404 NOT FOUND altrimenti).
     */
    @GetMapping("/{id}") // Mappa le richieste GET a /api/games/{id}
    public ResponseEntity<GameResponseDTO> getGameById(@PathVariable UUID id) {
        // Chiama il servizio per recuperare il gioco per ID.
        return gameService.getGameById(id)
                // Se il gioco è presente, restituisce 200 OK con il DTO.
                .map(gameDto -> new ResponseEntity<>(gameDto, HttpStatus.OK))
                // Se non trovato, restituisce 404 NOT FOUND.
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint GET per recuperare tutti i giochi presenti nel sistema.
     * Esempio di URL: GET /api/games
     *
     * @return ResponseEntity contenente una lista di GameResponseDTO e lo stato HTTP 200 OK.
     */
    @GetMapping // Mappa le richieste GET all'URL base /api/games
    public ResponseEntity<List<GameResponseDTO>> getAllGames() {
        // Chiama il servizio per recuperare tutti i giochi.
        List<GameResponseDTO> games = gameService.getAllGames();
        // Restituisce 200 OK con la lista dei giochi.
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    /**
     * Endpoint PUT per aggiornare i dati di un gioco esistente.
     * Esempio di URL: PUT /api/games/{id}
     * Il corpo della richiesta deve contenere un GameRequestDTO con i dati aggiornati e i nomi dei tag.
     *
     * @param id L'ID (UUID) del gioco da aggiornare.
     * @param gameRequestDTO Il DTO contenente i dati aggiornati del gioco.
     * Annotato con @Valid per attivare la validazione.
     * @return ResponseEntity contenente il GameResponseDTO del gioco aggiornato e lo stato HTTP 200 OK.
     * In caso di errore (es. gioco non trovato o titolo duplicato), restituisce 404 NOT FOUND o 400 BAD REQUEST.
     */
    @PutMapping("/{id}") // Mappa le richieste PUT a /api/games/{id}
    public ResponseEntity<GameResponseDTO> updateGame(
            @PathVariable UUID id,
            @Valid @RequestBody GameRequestDTO gameRequestDTO) {
        try {
            // Chiama il servizio per aggiornare il gioco.
            GameResponseDTO updatedGame = gameService.updateGame(id, gameRequestDTO);
            // Restituisce 200 OK con il gioco aggiornato.
            return new ResponseEntity<>(updatedGame, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            // Gestisce il caso in cui il gioco da aggiornare non venga trovato.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            // Gestisce altri errori come il titolo duplicato.
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint DELETE per eliminare un gioco tramite il suo ID.
     * Esempio di URL: DELETE /api/games/{id}
     *
     * @param id L'ID (UUID) del gioco da eliminare.
     * @return ResponseEntity con stato HTTP 204 NO CONTENT se l'eliminazione ha successo.
     * In caso di errore (es. gioco non trovato), restituisce 404 NOT FOUND.
     */
    @DeleteMapping("/{id}") // Mappa le richieste DELETE a /api/games/{id}
    public ResponseEntity<Void> deleteGame(@PathVariable UUID id) {
        try {
            // Chiama il servizio per eliminare il gioco.
            gameService.deleteGame(id);
            // Restituisce 204 No Content, indicando successo senza contenuto di risposta.
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            // Gestisce il caso in cui il gioco da eliminare non venga trovato.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint GET per trovare giochi per titolo (ricerca case-insensitive).
     * Esempio di URL: GET /api/games/search/title?query=valorant
     *
     * @param query Il titolo o parte del titolo da cercare.
     * @return ResponseEntity contenente una lista di GameResponseDTO e lo stato HTTP 200 OK.
     */
    @GetMapping("/search/title") // Mappa le richieste GET a /api/games/search/title
    public ResponseEntity<List<GameResponseDTO>> findGamesByTitle(@RequestParam("query") String query) {
        // Chiama il servizio per cercare giochi per titolo.
        List<GameResponseDTO> games = gameService.findGamesByTitle(query);
        // Restituisce 200 OK con la lista dei giochi trovati.
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    /**
     * Endpoint GET per trovare giochi associati a un tag specifico (ricerca case-insensitive).
     * Esempio di URL: GET /api/games/search/tag?name=action
     *
     * @param name Il nome del tag da cercare.
     * @return ResponseEntity contenente una lista di GameResponseDTO e lo stato HTTP 200 OK.
     */
    @GetMapping("/search/tag") // Mappa le richieste GET a /api/games/search/tag
    public ResponseEntity<List<GameResponseDTO>> findGamesByTagName(@RequestParam("name") String name) {
        // Chiama il servizio per cercare giochi per nome di tag.
        List<GameResponseDTO> games = gameService.findGamesByTagName(name);
        // Restituisce 200 OK con la lista dei giochi trovati.
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    /**
     * Endpoint GET per trovare giochi per sviluppatore (ricerca case-insensitive).
     * Esempio di URL: GET /api/games/search/developer?query=riot%20games
     *
     * @param query Lo sviluppatore da cercare.
     * @return ResponseEntity contenente una lista di GameResponseDTO e lo stato HTTP 200 OK.
     */
    @GetMapping("/search/developer") // Mappa le richieste GET a /api/games/search/developer
    public ResponseEntity<List<GameResponseDTO>> findGamesByDeveloper(@RequestParam("query") String query) {
        // Chiama il servizio per cercare giochi per sviluppatore.
        List<GameResponseDTO> games = gameService.findGamesByDeveloper(query);
        // Restituisce 200 OK con la lista dei giochi trovati.
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    /**
     * Endpoint GET per trovare giochi per editore (ricerca case-insensitive).
     * Esempio di URL: GET /api/games/search/publisher?query=valve
     *
     * @param query L'editore da cercare.
     * @return ResponseEntity contenente una lista di GameResponseDTO e lo stato HTTP 200 OK.
     */
    @GetMapping("/search/publisher") // Mappa le richieste GET a /api/games/search/publisher
    public ResponseEntity<List<GameResponseDTO>> findGamesByPublisher(@RequestParam("query") String query) {
        // Chiama il servizio per cercare giochi per editore.
        List<GameResponseDTO> games = gameService.findGamesByPublisher(query);
        // Restituisce 200 OK con la lista dei giochi trovati.
        return new ResponseEntity<>(games, HttpStatus.OK);
    }
}