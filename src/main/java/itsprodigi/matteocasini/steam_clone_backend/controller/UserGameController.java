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
     */
    @PostMapping("/users/{userUuid}/library/addgame")
    public ResponseEntity<UserGameResponseDTO> addGameToUserLibrary(@Valid @RequestBody UserGameRequestDTO userGameRequestDTO) {
        UserGameResponseDTO newUserGame = userGameService.addGameToUserLibrary(userGameRequestDTO);
        return new ResponseEntity<>(newUserGame, HttpStatus.CREATED);
    }

    /**
     * Recupera la libreria completa di un utente.
     */
    @GetMapping("/users/{userUuid}/library")
    public ResponseEntity<UserLibraryResponseDTO> getUserLibrary(@PathVariable UUID userUuid) {
        UserLibraryResponseDTO userLibrary = userGameService.getUserLibrary(userUuid);
        return new ResponseEntity<>(userLibrary, HttpStatus.OK);
    }

    /**
     * Rimuove un gioco dalla libreria di un utente.
     */
    @DeleteMapping("/users/{userUuid}/library/{gameUuid}")
    public ResponseEntity<Void> removeGameFromUserLibrary(@PathVariable UUID userUuid, @PathVariable UUID gameUuid) {
        userGameService.removeGameFromUserLibrary(userUuid, gameUuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}