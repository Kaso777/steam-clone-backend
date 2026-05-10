package io.github.kaso777.steamclone.service;

import io.github.kaso777.steamclone.dto.UserResponseDTO;
import io.github.kaso777.steamclone.enums.Role;
import io.github.kaso777.steamclone.exception.*;
import io.github.kaso777.steamclone.model.User;
import io.github.kaso777.steamclone.repository.UserRepository;
import io.github.kaso777.steamclone.dto.UserUpdateDTO;
import io.github.kaso777.steamclone.dto.auth.RegisterRequest;

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
     * Lancia eccezione se nessun utente e autenticato.
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
     * Registra un nuovo utente con ruolo standard ROLE_USER.
     */
    @Override
    @Transactional
    public UserResponseDTO registerUser(RegisterRequest request) {
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new IllegalArgumentException("La password deve avere almeno 6 caratteri.");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateUsernameException("Username '" + request.getUsername() + "' gia in uso.");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Email '" + request.getEmail() + "' gia in uso.");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ROLE_USER);

        return convertToResponseDto(userRepository.save(user));
    }

    /**
     * Recupera un utente tramite ID.
     * Consente l'accesso solo se ADMIN o se si tratta dell'utente stesso.
     */
    @Override
    public UserResponseDTO getUserById(UUID id) {
        User authenticatedUser = getAuthenticatedUser();

        if (!authenticatedUser.getRole().equals(Role.ROLE_ADMIN) && !authenticatedUser.getId().equals(id)) {
            throw new AccessDeniedException("Non hai i permessi per eseguire questa azione");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

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
     * ADMIN puo modificare tutti i campi; USER solo email e password.
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
            // USER puo modificare solo email e password
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
                throw new DuplicateUsernameException("Username '" + username + "' gia in uso.");
            }
        });
    }

    private void validateDuplicateEmail(String email, UUID id) {
        userRepository.findByEmail(email).ifPresent(existingUser -> {
            if (!existingUser.getId().equals(id)) {
                throw new DuplicateEmailException("Email '" + email + "' gia in uso.");
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
