package itsprodigi.matteocasini.steam_clone_backend.service;

import itsprodigi.matteocasini.steam_clone_backend.model.User;
import itsprodigi.matteocasini.steam_clone_backend.repository.UserRepository;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserRequestDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import itsprodigi.matteocasini.steam_clone_backend.enums.Role;
import itsprodigi.matteocasini.steam_clone_backend.exception.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO) {
        if (userRepository.existsByUsername(userRequestDTO.getUsername())) {
            throw new DuplicateUsernameException("Nome utente '" + userRequestDTO.getUsername() + "' già in uso.");
        }
        if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
            throw new DuplicateEmailException("Email '" + userRequestDTO.getEmail() + "' già in uso.");
        }
        if (userRequestDTO.getPassword() == null || userRequestDTO.getPassword().length() < 6) {
            throw new IllegalArgumentException("La password deve avere almeno 6 caratteri");
        }

        User user = new User();
        user.setUsername(userRequestDTO.getUsername());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        try {
            user.setRole(Role.valueOf(userRequestDTO.getRole()));
        } catch (IllegalArgumentException e) {
            throw new InvalidRoleException("Ruolo non valido: " + userRequestDTO.getRole() +
                    ". Ruoli consentiti: " + java.util.Arrays.toString(Role.values()));
        }

        User savedUser = userRepository.save(user);
        return convertToResponseDto(savedUser);
    }

    @Override
    public Optional<UserResponseDTO> getUserById(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Utente con ID " + id + " non trovato"));
        return Optional.of(convertToResponseDto(user));
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(UUID id, UserRequestDTO userRequestDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();

        boolean isAdmin = currentUser.getRole() == Role.ROLE_ADMIN;
        boolean isSelf = currentUser.getId().equals(id);

        if (!isAdmin && !isSelf) {
            throw new AccessDeniedException("Non sei autorizzato a modificare questo utente.");
        }

        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con ID: " + id));

        if (!isAdmin) {
            if (!userToUpdate.getEmail().equals(userRequestDTO.getEmail())) {
                userRepository.findByEmail(userRequestDTO.getEmail())
                        .ifPresent(existingUser -> {
                            if (!existingUser.getId().equals(id)) {
                                throw new DuplicateEmailException("Email '" + userRequestDTO.getEmail() + "' già in uso.");
                            }
                        });
                userToUpdate.setEmail(userRequestDTO.getEmail());
            }

            if (userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().isBlank()) {
                userToUpdate.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
            }

            return convertToResponseDto(userRepository.save(userToUpdate));
        }

        userRepository.findByUsername(userRequestDTO.getUsername())
                .ifPresent(existingUser -> {
                    if (!existingUser.getId().equals(id)) {
                        throw new DuplicateUsernameException("Username '" + userRequestDTO.getUsername() + "' già in uso.");
                    }
                });

        userRepository.findByEmail(userRequestDTO.getEmail())
                .ifPresent(existingUser -> {
                    if (!existingUser.getId().equals(id)) {
                        throw new DuplicateEmailException("Email '" + userRequestDTO.getEmail() + "' già in uso.");
                    }
                });

        userToUpdate.setUsername(userRequestDTO.getUsername());
        userToUpdate.setEmail(userRequestDTO.getEmail());

        if (userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().isBlank()) {
            userToUpdate.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        }

        try {
            userToUpdate.setRole(Role.valueOf(userRequestDTO.getRole()));
        } catch (IllegalArgumentException e) {
            throw new InvalidRoleException("Ruolo non valido: " + userRequestDTO.getRole());
        }

        return convertToResponseDto(userRepository.save(userToUpdate));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public void deleteUser(UUID id) {
        User userToDelete = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con ID: " + id));

        userRepository.delete(userToDelete);
    }

    private UserResponseDTO convertToResponseDto(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());

        return dto;
    }
}
