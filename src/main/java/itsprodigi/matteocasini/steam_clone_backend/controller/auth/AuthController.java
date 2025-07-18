package itsprodigi.matteocasini.steam_clone_backend.controller.auth;

import itsprodigi.matteocasini.steam_clone_backend.dto.auth.LoginRequest;
import itsprodigi.matteocasini.steam_clone_backend.dto.auth.RegisterRequest; // Nuovo import
import itsprodigi.matteocasini.steam_clone_backend.model.User; // Import dell'entità User
import itsprodigi.matteocasini.steam_clone_backend.repository.UserRepository;
import itsprodigi.matteocasini.steam_clone_backend.service.security.JwtUtil;
import org.springframework.http.HttpStatus; // Nuovo import
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder; // Nuovo import
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller per la gestione dell'autenticazione e registrazione degli utenti.
 * Espone endpoint per login e registrazione.
 */
@RestController
@RequestMapping("/api/auth") // Lasciamo /api/auth come da tua decisione
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Iniezione di PasswordEncoder

    // Costruttore per l'iniezione delle dipendenze.
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder; // Assegna PasswordEncoder
    }

    /**
     * Gestisce la richiesta di login dell'utente.
     * Autentica l'utente con username e password e, in caso di successo,
     * genera e restituisce un JWT.
     *
     * @param loginRequest Il DTO contenente username e password per il login.
     * @return ResponseEntity contenente il JWT in caso di successo, o uno stato di errore.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String jwt = jwtUtil.generateToken(userDetails);

            String userId = userRepository.findByUsername(loginRequest.getUsername())
                            .map(user -> user.getId().toString())
                            .orElse(null);

            Map<String, String> response = new HashMap<>();
            response.put("jwt", jwt);
            response.put("userId", userId);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore di autenticazione: " + e.getMessage());
        }
    }

    /**
     * Gestisce la richiesta di registrazione di un nuovo utente.
     * Verifica l'unicità di username ed email, cripta la password e salva il nuovo utente.
     *
     * @param registerRequest Il DTO contenente username, email e password per la registrazione.
     * @return ResponseEntity che indica il successo o il fallimento della registrazione.
     */
    @PostMapping("/register") // Nuovo endpoint per la registrazione
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        // 1. Controlla se username è già in uso
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            return new ResponseEntity<>("Username already taken!", HttpStatus.BAD_REQUEST);
        }

        // 2. Controlla se email è già in uso
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            return new ResponseEntity<>("Email already in use!", HttpStatus.BAD_REQUEST);
        }

        // 3. Crea un nuovo utente
        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword())); // Cripta la password
        newUser.setRole("USER"); // Assegna il ruolo predefinito "USER"

        // 4. Salva l'utente nel database
        userRepository.save(newUser);

        // 5. Restituisce una risposta di successo
        return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
    }
}