package itsprodigi.matteocasini.steam_clone_backend.controller;

import itsprodigi.matteocasini.steam_clone_backend.service.UserService; // Importa l'interfaccia del servizio Utente
import itsprodigi.matteocasini.steam_clone_backend.dto.UserRequestDTO;  // Importa il DTO di richiesta per l'utente
import itsprodigi.matteocasini.steam_clone_backend.dto.UserResponseDTO; // Importa il DTO di risposta per l'utente
import jakarta.validation.Valid; // Per attivare la validazione Bean Validation sui DTO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // Per i codici di stato HTTP
import org.springframework.http.ResponseEntity; // Per costruire le risposte HTTP
import org.springframework.web.bind.annotation.*; // Per le annotazioni REST

import java.util.List; // Per liste di DTO
import java.util.UUID; // Per gli ID univoci degli utenti

/**
 * Controller REST per la gestione delle operazioni sugli Utenti.
 * Espone gli endpoint API per registrare, recuperare, aggiornare ed eliminare gli utenti.
 * Questo controller interagisce con il livello di servizio (UserService) per eseguire la logica di business.
 */
@RestController // Indica a Spring che questa classe è un controller REST e gestisce le richieste HTTP.
@RequestMapping("/api/users") // Definisce il percorso base per tutti gli endpoint di questo controller (es. /api/users).
public class UserController {

    // Inietta l'interfaccia del servizio Utente. Spring si occuperà di iniettare
    // l'implementazione concreta (UserServiceImpl) grazie all'annotazione @Service su di essa.
    private final UserService userService;

    @Autowired // Costruttore per l'iniezione delle dipendenze.
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint POST per la registrazione di un nuovo utente.
     * Esempio di URL: POST /api/users/register
     * Il corpo della richiesta deve contenere un UserRequestDTO con username, email, password e role.
     *
     * @param userRequestDTO Il Data Transfer Object (DTO) contenente i dati di registrazione dell'utente.
     * Annotato con @Valid per attivare la validazione dei campi del DTO in base
     * alle annotazioni definite al suo interno (es. @NotBlank, @Email, @Size).
     * Se la validazione fallisce, Spring MVC genererà automaticamente un errore 400 Bad Request.
     * @return ResponseEntity contenente il UserResponseDTO dell'utente appena registrato
     * e lo stato HTTP 201 CREATED in caso di successo.
     * In caso di errore (es. username o email già in uso), restituisce 400 BAD REQUEST.
     */
    @PostMapping("/register") // Mappa le richieste POST all'URL /api/users/register
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        try {
            // Chiama il servizio per registrare l'utente. Il servizio restituisce un UserResponseDTO.
            UserResponseDTO registeredUser = userService.registerUser(userRequestDTO);
            // Restituisce una risposta HTTP 201 Created con l'utente registrato nel corpo.
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Cattura eccezioni generiche (es. username/email duplicati sollevate dal servizio).
            // In un'applicazione reale, si userebbero eccezioni custom più specifiche
            // e un GlobalExceptionHandler per mappare gli errori a risposte HTTP standardizzate.
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Esempio di risposta per dati non validi o duplicati.
        }
    }

    /**
     * Endpoint GET per recuperare tutti gli utenti registrati nel sistema.
     * Esempio di URL: GET /api/users
     *
     * @return ResponseEntity contenente una lista di UserResponseDTO e lo stato HTTP 200 OK.
     */
    @GetMapping // Mappa le richieste GET all'URL /api/users
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        // Chiama il servizio per ottenere tutti gli utenti, che verranno restituiti come lista di UserResponseDTO.
        List<UserResponseDTO> users = userService.getAllUsers();
        // Restituisce una risposta HTTP 200 OK con la lista degli utenti.
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Endpoint GET per recuperare un utente specifico tramite il suo ID.
     * Esempio di URL: GET /api/users/a1b2c3d4-e5f6-7890-1234-567890abcdef
     *
     * @param id L'ID univoco (UUID) dell'utente, estratto dal percorso URL (PathVariable).
     * @return ResponseEntity contenente il UserResponseDTO e lo stato HTTP (200 OK se trovato, 404 NOT FOUND altrimenti).
     */
    @GetMapping("/{id}") // Mappa le richieste GET all'URL /api/users/{id}
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id) {
        // Chiama il servizio per ottenere l'utente per ID. Il servizio restituisce un Optional<UserResponseDTO>.
        return userService.getUserById(id)
                // Se l'utente è presente (Optional.isPresent()), crea una risposta HTTP 200 OK con il DTO dell'utente.
                .map(userDto -> new ResponseEntity<>(userDto, HttpStatus.OK))
                // Se l'utente non è presente (Optional vuoto), restituisce una risposta HTTP 404 NOT FOUND.
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint PUT per aggiornare i dati di un utente esistente.
     * Esempio di URL: PUT /api/users/a1b2c3d4-e5f6-7890-1234-567890abcdef
     * Il corpo della richiesta deve contenere un UserRequestDTO con i dati aggiornati.
     *
     * @param id L'ID dell'utente da aggiornare, estratto dal percorso URL.
     * @param userRequestDTO Il DTO contenente i nuovi dati dell'utente.
     * Annotato con @Valid per attivare la validazione.
     * @return ResponseEntity contenente il UserResponseDTO dell'utente aggiornato
     * e lo stato HTTP 200 OK in caso di successo.
     * In caso di errore (es. utente non trovato, dati duplicati), restituisce 400 BAD REQUEST o 404 NOT FOUND.
     */
    @PutMapping("/{id}") // Mappa le richieste PUT all'URL /api/users/{id}
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID id, @Valid @RequestBody UserRequestDTO userRequestDTO) {
        try {
            // Chiama il servizio per aggiornare l'utente. Il servizio restituisce un UserResponseDTO.
            UserResponseDTO updatedUser = userService.updateUser(id, userRequestDTO);
            // Restituisce una risposta HTTP 200 OK con l'utente aggiornato.
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Gestione errori (es. utente non trovato, username/email duplicati).
            // Anche qui, si raccomanda l'uso di eccezioni custom e GlobalExceptionHandler.
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Esempio di risposta.
        }
    }

    /**
     * Endpoint DELETE per eliminare un utente specifico.
     * Esempio di URL: DELETE /api/users/a1b2c3d4-e5f6-7890-1234-567890abcdef
     *
     * @param id L'ID dell'utente da eliminare, estratto dal percorso URL.
     * @return ResponseEntity con stato HTTP 204 NO CONTENT se l'eliminazione ha successo.
     * In caso di errore (es. utente non trovato), restituisce 404 NOT FOUND.
     */
    @DeleteMapping("/{id}") // Mappa le richieste DELETE all'URL /api/users/{id}
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        try {
            // Chiama il servizio per eliminare l'utente. Il servizio non restituisce nulla (void).
            userService.deleteUser(id);
            // Restituisce una risposta HTTP 204 No Content, indicando che la richiesta è stata elaborata
            // con successo ma non c'è contenuto da restituire nel corpo della risposta.
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            // Gestione errori (es. utente non trovato).
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

   @ExceptionHandler(RuntimeException.class)
public ResponseEntity<String> fallbackHandler(RuntimeException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato");
}


}