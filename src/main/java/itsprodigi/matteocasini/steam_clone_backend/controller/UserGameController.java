package itsprodigi.matteocasini.steam_clone_backend.controller;

import itsprodigi.matteocasini.steam_clone_backend.service.UserGameService;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserGameRequestDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserGameResponseDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserLibraryResponseDTO;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller REST per la gestione della libreria giochi degli utenti.
 */
@RestController
@RequestMapping("/api")
public class UserGameController {

    private final UserGameService userGameService;

    @Autowired
    public UserGameController(UserGameService userGameService) {
        this.userGameService = userGameService;
    }

    /**
     * Aggiunge un gioco alla libreria di un utente.
     * POST /api/user-games
     */
    @PostMapping("/users/{userUuid}/library/addgame")
    public ResponseEntity<UserGameResponseDTO> addGameToUserLibrary(@Valid @RequestBody UserGameRequestDTO userGameRequestDTO) {
        UserGameResponseDTO newUserGame = userGameService.addGameToUserLibrary(userGameRequestDTO);
        return new ResponseEntity<>(newUserGame, HttpStatus.CREATED);
    }

    /*
    
     * Recupera un'associazione gioco-utente specifica.
     * GET /api/users/{userUuid}/games/{gameUuid}
     
    @GetMapping("/users/{userUuid}/games/{gameUuid}")
    public ResponseEntity<UserGameResponseDTO> getUserGameByIds(@PathVariable UUID userUuid, @PathVariable UUID gameUuid) {
        return userGameService.getUserGameByIds(userUuid, gameUuid)
                .map(userGameDto -> new ResponseEntity<>(userGameDto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
 */

    /**
     * Recupera la libreria completa di un utente.
     * GET /api/users/{userUuid}/library
     */
    @GetMapping("/users/{userUuid}/library")
    public ResponseEntity<UserLibraryResponseDTO> getUserLibrary(@PathVariable UUID userUuid) {
        UserLibraryResponseDTO userLibrary = userGameService.getUserLibrary(userUuid);
        return new ResponseEntity<>(userLibrary, HttpStatus.OK);
    }

    /**
     * Aggiorna i dati di un gioco nella libreria di un utente.
     * PUT /api/users/{userUuid}/games/{gameUuid}
     
    @PutMapping("/users/{userUuid}/games/{gameUuid}")
    public ResponseEntity<UserGameResponseDTO> updateUserGame(
            @PathVariable UUID userUuid,
            @PathVariable UUID gameUuid,
            @Valid @RequestBody UserGameRequestDTO userGameRequestDTO) {
        UserGameResponseDTO updatedUserGame = userGameService.updateUserGame(userUuid, gameUuid, userGameRequestDTO);
        return new ResponseEntity<>(updatedUserGame, HttpStatus.OK);
    }
        */

    /**
     * Rimuove un gioco dalla libreria di un utente.
     * DELETE /api/users/{userUuid}/games/{gameUuid}
     */
    @DeleteMapping("/users/{userUuid}/library/{gameUuid}")
    public ResponseEntity<Void> removeGameFromUserLibrary(@PathVariable UUID userUuid, @PathVariable UUID gameUuid) {
        userGameService.removeGameFromUserLibrary(userUuid, gameUuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    
}
