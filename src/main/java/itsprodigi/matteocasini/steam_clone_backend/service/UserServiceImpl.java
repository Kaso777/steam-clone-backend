package itsprodigi.matteocasini.steam_clone_backend.service;

import itsprodigi.matteocasini.steam_clone_backend.dto.UserRegistrationDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserResponseDTO;
import itsprodigi.matteocasini.steam_clone_backend.enums.Role;
import itsprodigi.matteocasini.steam_clone_backend.exception.*;
import itsprodigi.matteocasini.steam_clone_backend.model.User;
import itsprodigi.matteocasini.steam_clone_backend.repository.UserRepository;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserUpdateDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Recupera l'utente attualmente autenticato dal contesto di sicurezza.
     * Lancia eccezione se nessun utente è autenticato.
     */
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new UsernameNotFoundException("Nessun utente autenticato trovato");
        }

        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utente autenticato non trovato"));
    }

    /**
     * Registra un nuovo utente.
     * Verifica duplicati username e email, e lunghezza password minima.
     * Codifica la password prima di salvare.
     * Validazione ruolo utente.
     */
    @Override
    @Transactional
    public UserResponseDTO registerUser(UserRegistrationDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateUsernameException("Username '" + dto.getUsername() + "' già in uso.");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateEmailException("Email '" + dto.getEmail() + "' già in uso.");
        }

        if (dto.getPassword() == null || dto.getPassword().length() < 6) {
            throw new IllegalArgumentException("La password deve avere almeno 6 caratteri.");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        try {
            user.setRole(Role.valueOf(dto.getRole()));
        } catch (IllegalArgumentException e) {
            throw new InvalidRoleException("Ruolo non valido: " + dto.getRole() +
                    ". Ruoli consentiti: " + java.util.Arrays.toString(Role.values()));
        }

        return convertToResponseDto(userRepository.save(user));
    }

    /**
     * Recupera un utente tramite ID.
     * Consente l'accesso solo se ADMIN o se si tratta dell'utente stesso.
     */
    @Override
    public UserResponseDTO getUserById(UUID id) {
        User authenticatedUser = getAuthenticatedUser();
        System.out.println(">> getUserById called by: " + authenticatedUser.getUsername() + " (role: "
                + authenticatedUser.getRole() + ")");
        System.out.println(">> Target user ID requested: " + id);

        if (!authenticatedUser.getRole().equals(Role.ROLE_ADMIN) && !authenticatedUser.getId().equals(id)) {
            System.out.println(">> Access denied: " + authenticatedUser.getUsername() + " tried to access ID: " + id);
            throw new AccessDeniedException("Non hai i permessi per eseguire questa azione");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        System.out.println(">> Found user in DB: " + user.getUsername() + " (ID: " + user.getId() + ")");

        return convertToResponseDto(user);
    }

    /**
     * Recupera tutti gli utenti registrati.
     */
    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Aggiorna i dati di un utente esistente.
     * ADMIN può modificare tutti i campi; USER solo email e password.
     * Controllo autorizzazione: solo ADMIN o l'utente stesso.
     */
    @Override
    @Transactional
    public UserResponseDTO updateUser(UUID id, UserUpdateDTO dto) {
        User currentUser = getAuthenticatedUser();

        boolean isAdmin = currentUser.getRole() == Role.ROLE_ADMIN;
        boolean isSelf = currentUser.getId().equals(id);

        if (!isAdmin && !isSelf) {
            throw new AccessDeniedException("Non sei autorizzato a modificare questo utente.");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con ID: " + id));

        if (isAdmin) {
            if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
                validateDuplicateUsername(dto.getUsername(), id);
                user.setUsername(dto.getUsername());
            }

            if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
                validateDuplicateEmail(dto.getEmail(), id);
                user.setEmail(dto.getEmail());
            }

            if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
            }

            if (dto.getRole() != null && !dto.getRole().isBlank()) {
                try {
                    user.setRole(Role.valueOf(dto.getRole()));
                } catch (IllegalArgumentException e) {
                    throw new InvalidRoleException("Ruolo non valido: " + dto.getRole());
                }
            }

        } else {
            // USER può modificare solo email e password
            if (dto.getEmail() != null && !dto.getEmail().isBlank() && !user.getEmail().equals(dto.getEmail())) {
                validateDuplicateEmail(dto.getEmail(), id);
                user.setEmail(dto.getEmail());
            }

            if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
            }
        }

        return convertToResponseDto(userRepository.save(user));
    }

    /**
     * Elimina un utente tramite ID.
     * Consentito solo ad ADMIN o all'utente stesso.
     */
    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con ID: " + id));

        userRepository.delete(user);
    }

    // Metodi di validazione per duplicati username ed email

    private void validateDuplicateUsername(String username, UUID id) {
        userRepository.findByUsername(username).ifPresent(existingUser -> {
            if (!existingUser.getId().equals(id)) {
                throw new DuplicateUsernameException("Username '" + username + "' già in uso.");
            }
        });
    }

    private void validateDuplicateEmail(String email, UUID id) {
        userRepository.findByEmail(email).ifPresent(existingUser -> {
            if (!existingUser.getId().equals(id)) {
                throw new DuplicateEmailException("Email '" + email + "' già in uso.");
            }
        });
    }

    // Metodo di conversione da Entity a DTO

    private UserResponseDTO convertToResponseDto(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name());
    }
}