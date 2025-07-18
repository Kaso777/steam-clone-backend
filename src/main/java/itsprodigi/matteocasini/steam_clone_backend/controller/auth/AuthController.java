package itsprodigi.matteocasini.steam_clone_backend.controller.auth;


import itsprodigi.matteocasini.steam_clone_backend.dto.auth.LoginRequest; // Il DTO della richiesta di login
import itsprodigi.matteocasini.steam_clone_backend.repository.UserRepository; // Il tuo UserRepository
import itsprodigi.matteocasini.steam_clone_backend.service.security.JwtUtil; // Il nostro JwtUtil
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller per la gestione dell'autenticazione degli utenti.
 * Espone un endpoint per il login che restituisce un JWT in caso di successo.
 */
@RestController // Indica che questa è una classe Controller REST.
@RequestMapping("/api/auth") // Mappa tutte le richieste a /api/auth
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository; // Aggiungiamo UserRepository

    // Costruttore per l'iniezione delle dipendenze.
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    /**
     * Gestisce la richiesta di login dell'utente.
     * Autentica l'utente con username e password e, in caso di successo,
     * genera e restituisce un JWT.
     *
     * @param loginRequest Il DTO contenente username e password per il login.
     * @return ResponseEntity contenente il JWT in caso di successo, o uno stato di errore.
     */
    @PostMapping("/login") // Mappa le richieste POST a /api/auth/login
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Autentica l'utente usando l'AuthenticationManager di Spring Security.
            // Questo processo usa il CustomUserDetailsService per caricare l'utente
            // e il PasswordEncoder per verificare la password.
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            // Se l'autenticazione ha successo, recupera i dettagli dell'utente.
            // L'oggetto 'authentication.getPrincipal()' sarà il nostro oggetto User (UserDetails).
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Genera il token JWT per l'utente autenticato.
            String jwt = jwtUtil.generateToken(userDetails);

            // Recupera l'UUID dell'utente dal database per includerlo nella risposta
            // (Assumiamo che il tuo UserDetails sia la tua entità User)
            // Se la tua entità User implementa UserDetails, puoi castare direttamente:
            // User loggedInUser = (User) userDetails;
            // UUID userId = loggedInUser.getId();

            // Per semplicità, recuperiamo l'utente dal repository per ottenere l'ID,
            // dato che userDetails non ha direttamente getId().
            // Assicurati che il tuo UserDetails sia la tua entità User,
            // altrimenti dovrai modificare qui per recuperare l'ID correttamente.
            String userId = userRepository.findByUsername(loginRequest.getUsername())
                            .map(user -> user.getId().toString()) // Converti UUID in String
                            .orElse(null); // O gestisci l'errore se l'utente non viene trovato (improbabile qui)


            // Crea una mappa per la risposta contenente il JWT e l'ID utente.
            Map<String, String> response = new HashMap<>();
            response.put("jwt", jwt);
            response.put("userId", userId); // Aggiungi l'UUID dell'utente nella risposta

            // Restituisce la risposta con il JWT e l'ID utente.
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Cattura eventuali eccezioni durante l'autenticazione (es. credenziali errate)
            // e restituisce una risposta di errore.
            return ResponseEntity.badRequest().body("Errore di autenticazione: " + e.getMessage());
        }
    }
}