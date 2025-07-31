package itsprodigi.matteocasini.steam_clone_backend.service;

import itsprodigi.matteocasini.steam_clone_backend.dto.UserRequestDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserResponseDTO;
import itsprodigi.matteocasini.steam_clone_backend.enums.Role;
import itsprodigi.matteocasini.steam_clone_backend.exception.ResourceNotFoundException;
import itsprodigi.matteocasini.steam_clone_backend.model.User;
import itsprodigi.matteocasini.steam_clone_backend.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext(); // Pulisce eventuali contesti precedenti
    }

    @Test
    void registerUser_successfully_creates_user() {
        UserRequestDTO dto = new UserRequestDTO("matteo", "matteo@example.com", "secure123", "ROLE_USER");

        when(userRepository.existsByUsername("matteo")).thenReturn(false);
        when(userRepository.existsByEmail("matteo@example.com")).thenReturn(false);
        when(passwordEncoder.encode("secure123")).thenReturn("encodedPassword");

        User savedUser = new User();
        savedUser.setId(UUID.randomUUID());
        savedUser.setUsername("matteo");
        savedUser.setEmail("matteo@example.com");
        savedUser.setPassword("encodedPassword");
        savedUser.setRole(Role.ROLE_USER);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponseDTO response = userService.registerUser(dto);

        assertNotNull(response);
        assertEquals("matteo", response.getUsername());
        assertEquals("matteo@example.com", response.getEmail());
        assertEquals("ROLE_USER", response.getRole());
    }

    @Test
    void registerUser_fails_when_username_exists() {
        UserRequestDTO dto = new UserRequestDTO("existing", "new@example.com", "pass123", "ROLE_USER");

        when(userRepository.existsByUsername("existing")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.registerUser(dto));
        assertEquals("Username 'existing' già in uso.", ex.getMessage());
    }

    @Test
    void registerUser_fails_when_email_exists() {
        UserRequestDTO dto = new UserRequestDTO("newuser", "taken@example.com", "pass123", "ROLE_USER");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("taken@example.com")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.registerUser(dto));
        assertEquals("Email 'taken@example.com' già in uso.", ex.getMessage());
    }

    @Test
    void registerUser_fails_when_password_too_short() {
        UserRequestDTO dto = new UserRequestDTO("shortpass", "short@example.com", "123", "ROLE_USER");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.registerUser(dto));
        assertTrue(ex.getMessage().contains("La password deve avere almeno 6 caratteri"));
    }

    @Test
    void registerUser_fails_when_role_invalid() {
        UserRequestDTO dto = new UserRequestDTO("test", "test@example.com", "password123", "INVALID_ROLE");

        when(userRepository.existsByUsername("test")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.registerUser(dto));
        assertTrue(ex.getMessage().startsWith("Ruolo non valido"));
    }

    @Test
    void getUserById_notFound_throwsException() {
        UUID userId = UUID.randomUUID();

        // Simula utente autenticato
        User authenticatedUser = new User();
        authenticatedUser.setId(userId); // lo stesso ID che stiamo cercando
        authenticatedUser.setUsername("testuser");
        authenticatedUser.setRole(Role.ROLE_ADMIN);

        // Imposta il contesto di sicurezza con "testuser"
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("testuser", null)
        );

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(authenticatedUser));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                userService.getUserById(userId)
        );

        assertEquals("Utente non trovato con ID: " + userId, ex.getMessage());

        SecurityContextHolder.clearContext();
    }

    @Test
    void getUserById_success_returnsUser() {
        UUID id = UUID.randomUUID();

        User user = new User();
        user.setId(id);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("pass");
        user.setRole(Role.ROLE_USER);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("testuser", null)
        );

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserResponseDTO result = userService.getUserById(id);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());

        SecurityContextHolder.clearContext();
    }

    @Test
    void getAllUsers_returnsList() {
        User user1 = new User();
        user1.setId(UUID.randomUUID());
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");
        user1.setPassword("pass1");
        user1.setRole(Role.ROLE_USER);

        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        user2.setPassword("pass2");
        user2.setRole(Role.ROLE_ADMIN);

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<UserResponseDTO> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals("user2", result.get(1).getUsername());
    }
}
