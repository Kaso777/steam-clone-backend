package itsprodigi.matteocasini.steam_clone_backend.service;

import itsprodigi.matteocasini.steam_clone_backend.dto.UserRequestDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserResponseDTO;
import itsprodigi.matteocasini.steam_clone_backend.enums.Role;
import itsprodigi.matteocasini.steam_clone_backend.model.User;
import itsprodigi.matteocasini.steam_clone_backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    void registerUser_successfully_creates_user() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setUsername("matteo");
        dto.setEmail("matteo@example.com");
        dto.setPassword("secure123");
        dto.setRole("ROLE_USER");

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
    void registerUser_fails_when_username_already_exists() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setUsername("taken");
        dto.setEmail("new@example.com");
        dto.setPassword("password123");
        dto.setRole("ROLE_USER");

        when(userRepository.existsByUsername("taken")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(dto);
        });

        assertEquals("Nome utente 'taken' già in uso.", ex.getMessage());
    }

    @Test
    void registerUser_fails_when_email_already_exists() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setUsername("newuser");
        dto.setEmail("taken@example.com");
        dto.setPassword("password123");
        dto.setRole("ROLE_USER");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("taken@example.com")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(dto);
        });

        assertEquals("Email 'taken@example.com' già in uso.", ex.getMessage());
    }

    @Test
    void registerUser_fails_when_password_too_short() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setUsername("test");
        dto.setEmail("test@example.com");
        dto.setPassword("123");
        dto.setRole("ROLE_USER");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(dto);
        });

        assertTrue(ex.getMessage().contains("La password deve avere almeno 6 caratteri"));
    }

    @Test
    void registerUser_fails_when_invalid_role() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setUsername("test");
        dto.setEmail("test@example.com");
        dto.setPassword("password123");
        dto.setRole("INVALID_ROLE");

        when(userRepository.existsByUsername("test")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(dto);
        });

        assertTrue(ex.getMessage().startsWith("Ruolo non valido"));
    }

    @Test
    void testGetUserById_notFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.getUserById(userId));
        assertEquals("Utente con ID " + userId + " non trovato", ex.getMessage());
    }

    @Test
    void testGetUserById_success() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setId(id);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setRole(Role.ROLE_USER);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        Optional<UserResponseDTO> result = userService.getUserById(id);

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
        assertEquals("test@example.com", result.get().getEmail());
    }

    @Test
    void testGetAllUsers_success() {
        User user1 = new User();
        user1.setId(UUID.randomUUID());
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");
        user1.setRole(Role.ROLE_USER);

        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        user2.setRole(Role.ROLE_ADMIN);

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<UserResponseDTO> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals("user2", result.get(1).getUsername());
    }

    @Test
    void deleteUser_success() {
        UUID id = UUID.randomUUID();
        when(userRepository.existsById(id)).thenReturn(true);
        doNothing().when(userRepository).deleteById(id);

        assertDoesNotThrow(() -> userService.deleteUser(id));
        verify(userRepository).deleteById(id);
    }

    @Test
    void deleteUser_userNotFound() {
        UUID id = UUID.randomUUID();
        when(userRepository.existsById(id)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.deleteUser(id));
        assertEquals("Utente non trovato con ID: " + id, ex.getMessage());
    }
}
