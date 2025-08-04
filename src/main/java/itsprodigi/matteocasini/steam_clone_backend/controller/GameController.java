package itsprodigi.matteocasini.steam_clone_backend.controller;

import itsprodigi.matteocasini.steam_clone_backend.dto.GameRequestDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.GameResponseDTO;
import itsprodigi.matteocasini.steam_clone_backend.exception.ResourceNotFoundException;
import itsprodigi.matteocasini.steam_clone_backend.service.GameService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import itsprodigi.matteocasini.steam_clone_backend.dto.GameUpdateDTO;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<GameResponseDTO> createGame(@Valid @RequestBody GameRequestDTO gameRequestDTO) {
        try {
            GameResponseDTO newGame = gameService.createGame(gameRequestDTO);
            return new ResponseEntity<>(newGame, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameResponseDTO> getGameById(@PathVariable UUID id) {
        return gameService.getGameById(id)
                .map(gameDto -> new ResponseEntity<>(gameDto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<GameResponseDTO>> getAllGames() {
        List<GameResponseDTO> games = gameService.getAllGames();
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @PutMapping("/{id}")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public ResponseEntity<GameResponseDTO> updateGame(
        @PathVariable UUID id,
        @RequestBody GameUpdateDTO gameupdateDTO) {
    try {
        GameResponseDTO updatedGame = gameService.updateGame(id, gameupdateDTO);
        return new ResponseEntity<>(updatedGame, HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (RuntimeException e) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteGame(@PathVariable UUID id) {
        try {
            gameService.deleteGame(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<GameResponseDTO>> findGamesByTitle(@RequestParam("query") String query) {
        List<GameResponseDTO> games = gameService.findGamesByTitle(query);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @GetMapping("/search/tag")
    public ResponseEntity<List<GameResponseDTO>> findGamesByTagName(@RequestParam("name") String name) {
        List<GameResponseDTO> games = gameService.findGamesByTagName(name);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @GetMapping("/search/developer")
    public ResponseEntity<List<GameResponseDTO>> findGamesByDeveloper(@RequestParam("query") String query) {
        List<GameResponseDTO> games = gameService.findGamesByDeveloper(query);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @GetMapping("/search/publisher")
    public ResponseEntity<List<GameResponseDTO>> findGamesByPublisher(@RequestParam("query") String query) {
        List<GameResponseDTO> games = gameService.findGamesByPublisher(query);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }
}
