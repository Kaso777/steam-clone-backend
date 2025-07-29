    package itsprodigi.matteocasini.steam_clone_backend.service;

    import itsprodigi.matteocasini.steam_clone_backend.dto.UserRequestDTO;
    import itsprodigi.matteocasini.steam_clone_backend.dto.UserResponseDTO;
    import itsprodigi.matteocasini.steam_clone_backend.enums.Role;
    import itsprodigi.matteocasini.steam_clone_backend.exception.ResourceNotFoundException;
    import itsprodigi.matteocasini.steam_clone_backend.model.User; // Import corretto per la tua classe User
    import itsprodigi.matteocasini.steam_clone_backend.repository.UserRepository;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.access.AccessDeniedException;

    // Import per test di integrazione
    import org.springframework.boot.test.context.SpringBootTest;
    import org.springframework.boot.test.mock.mockito.MockBean;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.test.context.support.WithMockUser; // Per simulare utenti autenticati
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Import per UsernamePasswordAuthenticationToken

    import java.util.Optional;
    import java.util.List;
    import java.util.UUID;
    import java.util.stream.Collectors;

    import static org.junit.jupiter.api.Assertions.*;
    import static org.mockito.Mockito.*;

    // ABILITA IL CONTESTO DI SPRING BOOT PER IL TEST
    @SpringBootTest
    class UserServiceImplTest {

        @Autowired
        private UserServiceImpl userService;

        @MockBean
        private UserRepository userRepository;
        @MockBean
        private PasswordEncoder passwordEncoder;

        @BeforeEach
        void setUp() {
            SecurityContextHolder.clearContext();
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

            ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));
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
        @WithMockUser(username = "adminUser", roles = {"ADMIN"}) // Simula un utente ADMIN per questo test
        void updateUser_adminCanUpdateAnyUser() {
            UUID userIdToUpdate = UUID.randomUUID();
            UserRequestDTO updateDto = new UserRequestDTO();
            updateDto.setUsername("updatedUser");
            updateDto.setEmail("updated@example.com");
            updateDto.setPassword("newPassword");
            updateDto.setRole("ROLE_USER");

            User existingUser = new User();
            existingUser.setId(userIdToUpdate);
            existingUser.setUsername("oldUser");
            existingUser.setEmail("old@example.com");
            existingUser.setPassword("oldEncodedPassword");
            existingUser.setRole(Role.ROLE_USER);

            when(userRepository.findById(userIdToUpdate)).thenReturn(Optional.of(existingUser));
            when(userRepository.findByUsername("updatedUser")).thenReturn(Optional.empty());
            when(userRepository.findByEmail("updated@example.com")).thenReturn(Optional.empty());
            when(passwordEncoder.encode("newPassword")).thenReturn("newEncodedPassword");
            when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

            UserResponseDTO result = userService.updateUser(userIdToUpdate, updateDto);

            assertNotNull(result);
            assertEquals("updatedUser", result.getUsername());
            assertEquals("updated@example.com", result.getEmail());
            assertEquals("ROLE_USER", result.getRole());
            verify(userRepository, times(1)).save(any(User.class));
        }

        @Test
        @WithMockUser(username = "selfUser", roles = {"USER"}) // Simula un utente normale
        void updateUser_userCanUpdateSelf() {
            UUID userId = UUID.randomUUID(); // Questo sarà l'ID dell'utente che si aggiorna
            UserRequestDTO updateDto = new UserRequestDTO();
            updateDto.setUsername("selfUser"); // Username deve corrispondere a @WithMockUser
            updateDto.setEmail("updated_self@example.com");
            updateDto.setPassword("newSelfPassword");
            updateDto.setRole("ROLE_ADMIN"); // Questo ruolo non dovrebbe cambiare per un utente normale

            // Creiamo un'istanza reale di User e impostiamo i suoi valori
            User existingUser = new User();
            existingUser.setId(userId);
            existingUser.setUsername("selfUser");
            existingUser.setEmail("original_self@example.com");
            existingUser.setPassword("oldEncodedPassword");
            existingUser.setRole(Role.ROLE_USER);

            // Imposta il principal nel SecurityContextHolder usando l'istanza reale di User
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                existingUser, // Il tuo oggetto User come principal
                null,
                existingUser.getAuthorities() // Le autorità del tuo User
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
            when(userRepository.findByEmail("updated_self@example.com")).thenReturn(Optional.empty());
            when(passwordEncoder.encode("newSelfPassword")).thenReturn("newEncodedSelfPassword");
            when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

            UserResponseDTO result = userService.updateUser(userId, updateDto);

            assertNotNull(result);
            assertEquals("selfUser", result.getUsername());
            assertEquals("updated_self@example.com", result.getEmail());
            assertEquals("ROLE_USER", result.getRole());
            verify(userRepository, times(1)).save(any(User.class));
        }

        @Test
        @WithMockUser(username = "otherUser", roles = {"USER"}) // Simula un utente normale
        void updateUser_userCannotUpdateOtherUser() {
            UUID userIdToUpdate = UUID.randomUUID(); // ID di un utente diverso
            UUID authenticatedUserId = UUID.randomUUID(); // ID dell'utente autenticato
            UserRequestDTO updateDto = new UserRequestDTO();
            updateDto.setEmail("updated@example.com");

            // Creiamo un'istanza reale di User per l'utente autenticato
            User otherUser = new User();
            otherUser.setId(authenticatedUserId);
            otherUser.setRole(Role.ROLE_USER);
            otherUser.setUsername("otherUser");

            // Imposta il principal nel SecurityContextHolder
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                otherUser, // Il tuo oggetto User come principal
                null,
                otherUser.getAuthorities() // Le autorità del tuo User
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            AccessDeniedException ex = assertThrows(AccessDeniedException.class,
                    () -> userService.updateUser(userIdToUpdate, updateDto));

            // CORREZIONE: Messaggio atteso aggiornato per corrispondere a quello lanciato dal servizio
            assertEquals("Non sei autorizzato a modificare questo utente.", ex.getMessage());
            verify(userRepository, never()).findById(any(UUID.class));
        }

        @Test
        @WithMockUser(username = "adminTest", roles = {"ADMIN"}) // Simula un utente ADMIN
        void updateUser_userNotFound() {
            UUID userId = UUID.randomUUID();
            UserRequestDTO updateDto = new UserRequestDTO();
            updateDto.setUsername("test");
            updateDto.setEmail("test@example.com");
            updateDto.setPassword("password123");
            updateDto.setRole("ROLE_USER");

            // Creiamo un'istanza reale di User per l'admin
            User adminUser = new User();
            adminUser.setId(UUID.randomUUID());
            adminUser.setRole(Role.ROLE_ADMIN);
            adminUser.setUsername("adminTest");

            // Imposta il principal nel SecurityContextHolder
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                adminUser, // Il tuo oggetto User come principal
                null,
                adminUser.getAuthorities() // Le autorità del tuo User
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            when(userRepository.findById(userId)).thenReturn(Optional.empty()); // Utente non trovato

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> userService.updateUser(userId, updateDto));

            assertEquals("Utente non trovato con ID: " + userId, ex.getMessage());
        }


        @Test
        @WithMockUser(username = "testuser", roles = {"USER"})
        void deleteUser_success() {
            UUID id = UUID.randomUUID();
            // Creiamo un'istanza reale di User per l'utente da eliminare
            User userToDelete = new User();
            userToDelete.setId(id);
            userToDelete.setUsername("testuser");
            userToDelete.setEmail("test@example.com");
            userToDelete.setRole(Role.ROLE_USER);

            // Imposta il principal nel SecurityContextHolder
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                userToDelete, // Il tuo oggetto User come principal
                null,
                userToDelete.getAuthorities() // Le autorità del tuo User
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            when(userRepository.findById(id)).thenReturn(Optional.of(userToDelete));
            doNothing().when(userRepository).delete(userToDelete);

            assertDoesNotThrow(() -> userService.deleteUser(id));

            verify(userRepository).delete(userToDelete);
        }

        @Test
        @WithMockUser(username = "someUser", roles = {"USER"})
        void deleteUser_userNotFound() {
            UUID id = UUID.randomUUID();
            // Creiamo un'istanza reale di User per l'utente autenticato
            User authenticatedUser = new User();
            authenticatedUser.setId(UUID.randomUUID());
            authenticatedUser.setRole(Role.ROLE_USER);
            authenticatedUser.setUsername("someUser");

            // Imposta il principal nel SecurityContextHolder
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                authenticatedUser, // Il tuo oggetto User come principal
                null,
                authenticatedUser.getAuthorities() // Le autorità del tuo User
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            when(userRepository.findById(id)).thenReturn(Optional.empty());

            ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(id));
            assertEquals("Utente non trovato con ID: " + id, ex.getMessage());

            verify(userRepository, never()).delete(any(User.class));
        }

        @Test
        @WithMockUser(username = "adminUser", roles = {"ADMIN"}) // Simula un utente ADMIN
        void deleteUser_adminCanDeleteAnyUser() {
            UUID idToDelete = UUID.randomUUID();
            // Creiamo un'istanza reale di User per l'utente da eliminare
            User userToDelete = new User();
            userToDelete.setId(idToDelete);
            userToDelete.setUsername("userToDelete");
            userToDelete.setRole(Role.ROLE_USER);

            // Creiamo un'istanza reale di User per l'admin
            User adminUser = new User();
            adminUser.setId(UUID.randomUUID()); // ID casuale per l'admin
            adminUser.setRole(Role.ROLE_ADMIN);
            adminUser.setUsername("adminUser");

            // Imposta il principal nel SecurityContextHolder
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                adminUser, // Il tuo oggetto User come principal
                null,
                adminUser.getAuthorities() // Le autorità del tuo User
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            when(userRepository.findById(idToDelete)).thenReturn(Optional.of(userToDelete));
            doNothing().when(userRepository).delete(userToDelete);

            assertDoesNotThrow(() -> userService.deleteUser(idToDelete));
            verify(userRepository).delete(userToDelete);
        }

        @Test
        @WithMockUser(username = "normalUser", roles = {"USER"})
        void deleteUser_userCannotDeleteOtherUser() {
            UUID idToDelete = UUID.randomUUID();
            UUID authenticatedUserId = UUID.randomUUID();

            // Creiamo un'istanza reale di User per l'utente da eliminare
            User userToDelete = new User();
            userToDelete.setId(idToDelete);
            userToDelete.setUsername("userToDelete");
            userToDelete.setRole(Role.ROLE_USER);

            // Creiamo un'istanza reale di User per l'utente autenticato
            User authenticatedUser = new User();
            authenticatedUser.setId(authenticatedUserId);
            authenticatedUser.setUsername("normalUser");
            authenticatedUser.setRole(Role.ROLE_USER);

            // Imposta il principal nel SecurityContextHolder
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                authenticatedUser, // Il tuo oggetto User come principal
                null,
                authenticatedUser.getAuthorities() // Le autorità del tuo User
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            when(userRepository.findById(idToDelete)).thenReturn(Optional.of(userToDelete));

            AccessDeniedException ex = assertThrows(AccessDeniedException.class, () -> userService.deleteUser(idToDelete));
            // CORREZIONE: Il messaggio di default di @PreAuthorize è "Access Denied"
            assertEquals("Access Denied", ex.getMessage()); // Corretto da ex.Message() a ex.getMessage()

            verify(userRepository, never()).delete(any(User.class));
        }
    }
