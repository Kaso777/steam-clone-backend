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

    public User getAuthenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || authentication.getName() == null) {
        throw new UsernameNotFoundException("Nessun utente autenticato trovato nel contesto di sicurezza");
    }

    String username = authentication.getName();

    return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Utente autenticato non trovato"));
}


    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

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

    @Override
public UserResponseDTO getUserById(UUID id) {
    User authenticatedUser = getAuthenticatedUser();
    System.out.println(">> getUserById called by: " + authenticatedUser.getUsername() + " (role: " + authenticatedUser.getRole() + ")");
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





    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

   @Override
@Transactional
public UserResponseDTO updateUser(UUID id, UserUpdateDTO dto) {
    User currentUser = getAuthenticatedUser(); // usa il metodo già presente nel tuo UserServiceImpl


    boolean isAdmin = currentUser.getRole() == Role.ROLE_ADMIN;
    boolean isSelf = currentUser.getId().equals(id);

    if (!isAdmin && !isSelf) {
        throw new AccessDeniedException("Non sei autorizzato a modificare questo utente.");
    }

    User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con ID: " + id));

    // ✏️ Se ADMIN → può modificare tutto
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
        // ✏️ Se USER → solo email e password
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



    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con ID: " + id));

        userRepository.delete(user);
    }

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

    private UserResponseDTO convertToResponseDto(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}