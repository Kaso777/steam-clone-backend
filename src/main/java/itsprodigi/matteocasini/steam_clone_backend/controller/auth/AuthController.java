package itsprodigi.matteocasini.steam_clone_backend.controller.auth;

import itsprodigi.matteocasini.steam_clone_backend.dto.auth.LoginRequest;
import itsprodigi.matteocasini.steam_clone_backend.dto.auth.RegisterRequest;
import itsprodigi.matteocasini.steam_clone_backend.model.User;
import itsprodigi.matteocasini.steam_clone_backend.repository.UserRepository;
import itsprodigi.matteocasini.steam_clone_backend.service.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import itsprodigi.matteocasini.steam_clone_backend.enums.Role; // <-- NUOVO IMPORT

/**
 * Controller per la gestione dell'autenticazione e registrazione degli utenti.
 * Espone endpoint per login e registrazione.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

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

    @PostMapping("/register")
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
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        // --- MODIFICATO QUI ---
        newUser.setRole(Role.ROLE_USER); // <-- Assegna il ruolo usando l'enum
        // ----------------------

        // 4. Salva l'utente nel database
        userRepository.save(newUser);

        // 5. Restituisce una risposta di successo
        return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
    }
}