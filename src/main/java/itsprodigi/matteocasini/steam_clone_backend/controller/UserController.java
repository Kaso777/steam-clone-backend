package itsprodigi.matteocasini.steam_clone_backend.controller;

import itsprodigi.matteocasini.steam_clone_backend.service.UserService;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserRequestDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserResponseDTO;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller REST per la gestione degli utenti.
 * Espone endpoint per registrazione, recupero, aggiornamento ed eliminazione.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registra un nuovo utente.
     * POST /api/users/register
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO registeredUser = userService.registerUser(userRequestDTO);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    /**
     * Recupera tutti gli utenti registrati.
     * GET /api/users
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Recupera un utente tramite ID.
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /**
     * Aggiorna i dati di un utente esistente.
     * PUT /api/users/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID id,
            @Valid @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO updatedUser = userService.updateUser(id, userRequestDTO);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    /**
     * Elimina un utente tramite ID.
     * DELETE /api/users/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Fallback generico per errori non gestiti nei metodi sopra.
     * Utile per test o fallback semplice durante lo sviluppo.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> fallbackHandler(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato");
    }
}
