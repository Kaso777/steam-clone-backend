package itsprodigi.matteocasini.steam_clone_backend.controller;

import itsprodigi.matteocasini.steam_clone_backend.dto.UserGameRequestDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserGameResponseDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserLibraryResponseDTO;
import itsprodigi.matteocasini.steam_clone_backend.model.UserGame;
import itsprodigi.matteocasini.steam_clone_backend.service.UserGameService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController // Indica a Spring che questa è una classe Controller REST
@RequestMapping("/api/library") // Mappa tutte le richieste che iniziano con /api/library a questo controller
public class UserGameController {

    private final UserGameService userGameService; // Inietta il UserGameService

    // Costruttore per l'iniezione delle dipendenze
    public UserGameController(UserGameService userGameService) {
        this.userGameService = userGameService;
    }

    /**
     * Endpoint per aggiungere un gioco alla libreria di un utente.
     * Metodo HTTP: POST
     * URL: /api/library
     * @param userGameRequestDTO DTO contenente gli UUID dell'utente e del gioco, più la data di acquisto.
     * @return ResponseEntity<UserGameResponseDTO> con la relazione creata e stato HTTP 201 Created.
     */
    @PostMapping
    public ResponseEntity<UserGameResponseDTO> addGameToUserLibrary(@Valid @RequestBody UserGameRequestDTO userGameRequestDTO) {
        UserGameResponseDTO userGame = userGameService.addGameToUserLibrary(userGameRequestDTO);
        return new ResponseEntity<>(userGame, HttpStatus.CREATED);
    }

    /**
     * Endpoint per recuperare tutti i giochi nella libreria di un utente specifico.
     * Metodo HTTP: GET
     * URL: /api/library/user/{userUuid}
     * @param userUuid L'UUID dell'utente di cui recuperare la libreria.
     * @return ResponseEntity<UserLibraryResponseDTO> con la libreria formattata e stato HTTP 200 OK.
     */
    @GetMapping("/user/{userUuid}")
    public ResponseEntity<UserLibraryResponseDTO> getUserLibrary(@PathVariable UUID userUuid) {
        // Il service ora restituisce una lista di entità UserGame
        List<UserGame> userGames = userGameService.getUserLibrary(userUuid);
        // Crea il nuovo DTO di risposta usando la lista di entità
        UserLibraryResponseDTO response = new UserLibraryResponseDTO(userGames);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint per recuperare una specifica entry della libreria (un utente che possiede un gioco specifico).
     * Metodo HTTP: GET
     * URL: /api/library/user/{userUuid}/game/{gameUuid}
     * @param userUuid L'UUID dell'utente.
     * @param gameUuid L'UUID del gioco.
     * @return ResponseEntity<UserGameResponseDTO> con la relazione trovata e stato HTTP 200 OK.
     */
    @GetMapping("/user/{userUuid}/game/{gameUuid}")
    public ResponseEntity<UserGameResponseDTO> getUserGameEntry(@PathVariable UUID userUuid, @PathVariable UUID gameUuid) {
        UserGameResponseDTO userGame = userGameService.getUserGameEntry(userUuid, gameUuid);
        return ResponseEntity.ok(userGame);
    }

    /**
     * Endpoint per rimuovere un gioco dalla libreria di un utente.
     * Metodo HTTP: DELETE
     * URL: /api/library/user/{userUuid}/game/{gameUuid}
     * @param userUuid L'UUID dell'utente.
     * @param gameUuid L'UUID del gioco da rimuovere.
     * @return ResponseEntity<Void> con stato HTTP 204 No Content.
     */
    @DeleteMapping("/user/{userUuid}/game/{gameUuid}")
    public ResponseEntity<Void> removeGameFromUserLibrary(@PathVariable UUID userUuid, @PathVariable UUID gameUuid) {
        userGameService.removeGameFromUserLibrary(userUuid, gameUuid);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
} 