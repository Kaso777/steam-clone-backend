package itsprodigi.matteocasini.steam_clone_backend.controller;

import itsprodigi.matteocasini.steam_clone_backend.dto.GameRequestDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.GameResponseDTO;
import itsprodigi.matteocasini.steam_clone_backend.service.GameService;

import jakarta.validation.Valid; // Per la validazione dei DTO in ingresso
import org.springframework.http.HttpStatus; // Per gli stati HTTP (es. 201 Created)
import org.springframework.http.ResponseEntity; // Per costruire le risposte HTTP
import org.springframework.web.bind.annotation.*; // Include annotazioni come @RestController, @RequestMapping, ecc.

import java.util.List;
import java.util.UUID;

@RestController // Indica a Spring che questa è una classe Controller REST
@RequestMapping("/api/games") // Mappa tutte le richieste che iniziano con /api/games a questo controller
public class GameController {

    private final GameService gameService; // Inietta il GameService

    // Costruttore per l'iniezione delle dipendenze
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Endpoint per la creazione di un nuovo gioco.
     * Metodo HTTP: POST
     * URL: /api/games
     * @param gameRequestDTO DTO contenente i dati del gioco da creare. La validazione (@Valid) è applicata.
     * @return ResponseEntity<GameResponseDTO> con il gioco creato e stato HTTP 201 Created.
     */
    @PostMapping
    public ResponseEntity<GameResponseDTO> createGame(@Valid @RequestBody GameRequestDTO gameRequestDTO) {
        GameResponseDTO createdGame = gameService.createGame(gameRequestDTO);
        return new ResponseEntity<>(createdGame, HttpStatus.CREATED);
    }

    /**
     * Endpoint per recuperare un singolo gioco tramite il suo UUID.
     * Metodo HTTP: GET
     * URL: /api/games/{uuid}
     * @param uuid L'UUID del gioco da recuperare, estratto dal path dell'URL.
     * @return ResponseEntity<GameResponseDTO> con il gioco trovato e stato HTTP 200 OK.
     * Se non trovato, ResourceNotFoundException sarà gestita dal GlobalExceptionHandler.
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<GameResponseDTO> getGameByUuid(@PathVariable UUID uuid) {
        GameResponseDTO game = gameService.getGameByUuid(uuid);
        return ResponseEntity.ok(game);
    }

    /**
     * Endpoint per recuperare tutti i giochi.
     * Metodo HTTP: GET
     * URL: /api/games
     * @return ResponseEntity<List<GameResponseDTO>> con la lista di tutti i giochi e stato HTTP 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<GameResponseDTO>> getAllGames() {
        List<GameResponseDTO> games = gameService.getAllGames();
        return ResponseEntity.ok(games);
    }

    /**
     * Endpoint per aggiornare i dati di un gioco esistente.
     * Metodo HTTP: PUT
     * URL: /api/games/{uuid}
     * @param uuid L'UUID del gioco da aggiornare.
     * @param gameRequestDTO DTO contenente i nuovi dati del gioco. La validazione (@Valid) è applicata.
     * @return ResponseEntity<GameResponseDTO> con il gioco aggiornato e stato HTTP 200 OK.
     * Se non trovato o dati non validi, le eccezioni saranno gestite dal GlobalExceptionHandler.
     */
    @PutMapping("/{uuid}")
    public ResponseEntity<GameResponseDTO> updateGame(@PathVariable UUID uuid, @Valid @RequestBody GameRequestDTO gameRequestDTO) {
        GameResponseDTO updatedGame = gameService.updateGame(uuid, gameRequestDTO);
        return ResponseEntity.ok(updatedGame);
    }

    /**
     * Endpoint per eliminare un gioco.
     * Metodo HTTP: DELETE
     * URL: /api/games/{uuid}
     * @param uuid L'UUID del gioco da eliminare.
     * @return ResponseEntity<Void> con stato HTTP 204 No Content.
     * Se non trovato, ResourceNotFoundException sarà gestita dal GlobalExceptionHandler.
     */
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteGame(@PathVariable UUID uuid) {
        gameService.deleteGame(uuid);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}