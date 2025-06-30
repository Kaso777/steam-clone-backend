package itsprodigi.matteocasini.steam_clone_backend.controller;

// Importa le classi necessarie dai pacchetti del tuo progetto
import itsprodigi.matteocasini.steam_clone_backend.dto.UserRequestDTO; // DTO per i dati in ingresso
import itsprodigi.matteocasini.steam_clone_backend.dto.UserResponseDTO; // DTO per i dati in uscita
import itsprodigi.matteocasini.steam_clone_backend.service.UserService; // L'interfaccia del servizio utente

// Importa le annotazioni e classi di Spring Framework
import jakarta.validation.Valid; // Per abilitare la validazione sui DTO
import org.springframework.http.HttpStatus; // Per definire lo stato della risposta HTTP
import org.springframework.http.ResponseEntity; // Per costruire la risposta HTTP
import org.springframework.web.bind.annotation.*; // Include @RestController, @RequestMapping, @PostMapping, etc.

// Importa classi standard di Java
import java.util.List; // Per lavorare con liste
import java.util.UUID; // Per lavorare con gli UUID

/**
 * Controller REST per la gestione delle operazioni sugli utenti.
 * Espone endpoint HTTP per creare, leggere, aggiornare ed eliminare utenti.
 */
@RestController // Questa annotazione combina @Controller (rende la classe un bean Spring MVC)
                // e @ResponseBody (indica che i valori di ritorno dei metodi devono essere
                // serializzati direttamente nel corpo della risposta HTTP, es. come JSON).
@RequestMapping("/api/users") // Mappa tutte le richieste HTTP che iniziano con "/api/users" a questo controller.
public class UserController {

    private final UserService userService; // Dichiarazione di una dipendenza dal UserService

    /**
     * Costruttore per l'iniezione delle dipendenze (Dependency Injection).
     * Spring inietterà automaticamente un'istanza di UserServiceImpl (che implementa UserService).
     * @param userService L'istanza del servizio utente.
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint per la creazione di un nuovo utente.
     * Metodo HTTP: POST
     * URL: /api/users
     * @param userRequestDTO I dati dell'utente da creare, ricevuti nel corpo della richiesta.
     * @Valid: attiva le regole di validazione definite in UserRequestDTO.
     * @RequestBody: indica che il corpo della richiesta HTTP deve essere
     * deserializzato in un oggetto UserRequestDTO.
     * @return ResponseEntity<UserResponseDTO> La risposta HTTP contenente i dati dell'utente creato e lo stato 201 Created.
     */
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        // Chiama il metodo createUser del UserService per gestire la logica di business e persistenza.
        UserResponseDTO createdUser = userService.createUser(userRequestDTO);
        // Restituisce una risposta HTTP con lo stato 201 Created e il DTO dell'utente creato nel corpo.
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    /**
     * Endpoint per recuperare tutti gli utenti.
     * Metodo HTTP: GET
     * URL: /api/users
     * @return ResponseEntity<List<UserResponseDTO>> Una lista di tutti gli utenti e lo stato 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        // Chiama il metodo getAllUsers del UserService per recuperare tutti gli utenti.
        List<UserResponseDTO> users = userService.getAllUsers();
        // Restituisce una risposta HTTP con lo stato 200 OK e la lista di DTO nel corpo.
        return ResponseEntity.ok(users); // ResponseEntity.ok() è una scorciatoia per uno stato 200 OK
    }

    /**
     * Endpoint per recuperare un singolo utente tramite il suo UUID.
     * Metodo HTTP: GET
     * URL: /api/users/{uuid}
     * @param uuid L'UUID dell'utente da cercare, estratto dal path dell'URL.
     * @PathVariable: lega il valore del segmento di URI "uuid" al parametro del metodo.
     * @return ResponseEntity<UserResponseDTO> L'utente trovato e lo stato 200 OK.
     * Se l'utente non esiste, ResourceNotFoundException verrà lanciata
     * dal UserService e gestita dal GlobalExceptionHandler (risposta 404).
     */
    @GetMapping("/{uuid}") // {uuid} è una variabile di percorso
    public ResponseEntity<UserResponseDTO> getUserByUuid(@PathVariable UUID uuid) {
        // Chiama il metodo getUserByUuid del UserService.
        UserResponseDTO user = userService.getUserByUuid(uuid);
        // Restituisce una risposta HTTP con lo stato 200 OK e il DTO dell'utente nel corpo.
        return ResponseEntity.ok(user);
    }

    /**
     * Endpoint per aggiornare un utente esistente.
     * Metodo HTTP: PUT
     * URL: /api/users/{uuid}
     * @param uuid L'UUID dell'utente da aggiornare.
     * @param userRequestDTO I dati aggiornati dell'utente, ricevuti nel corpo della richiesta.
     * @return ResponseEntity<UserResponseDTO> L'utente aggiornato e lo stato 200 OK.
     * Se l'utente non esiste, ResourceNotFoundException verrà lanciata
     * e gestita dal GlobalExceptionHandler (risposta 404).
     * Errori di validazione sui dati in ingresso saranno gestiti dal GlobalExceptionHandler (risposta 400).
     */
    @PutMapping("/{uuid}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID uuid, @Valid @RequestBody UserRequestDTO userRequestDTO) {
        // Chiama il metodo updateUser del UserService per applicare le modifiche.
        UserResponseDTO updatedUser = userService.updateUser(uuid, userRequestDTO);
        // Restituisce una risposta HTTP con lo stato 200 OK e il DTO dell'utente aggiornato.
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Endpoint per eliminare un utente.
     * Metodo HTTP: DELETE
     * URL: /api/users/{uuid}
     * @param uuid L'UUID dell'utente da eliminare.
     * @return ResponseEntity<Void> Una risposta senza corpo e lo stato 204 No Content (indica successo senza contenuto).
     * Se l'utente non esiste, ResourceNotFoundException verrà lanciata
     * e gestita dal GlobalExceptionHandler (risposta 404).
     */
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID uuid) {
        // Chiama il metodo deleteUser del UserService.
        userService.deleteUser(uuid);
        // Restituisce una risposta HTTP con lo stato 204 No Content.
        // Questo stato è appropriato per le eliminazioni che non restituiscono dati nel corpo.
        return ResponseEntity.noContent().build(); // ResponseEntity.noContent().build() crea una risposta 204
    }
} 