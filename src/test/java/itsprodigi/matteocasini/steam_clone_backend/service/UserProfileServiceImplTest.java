package itsprodigi.matteocasini.steam_clone_backend.service;

import itsprodigi.matteocasini.steam_clone_backend.dto.UserProfileRequestDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserProfileResponseDTO;
import itsprodigi.matteocasini.steam_clone_backend.exception.UserProfileNotFoundException;
import itsprodigi.matteocasini.steam_clone_backend.model.User;
import itsprodigi.matteocasini.steam_clone_backend.model.UserProfile;
import itsprodigi.matteocasini.steam_clone_backend.repository.UserProfileRepository;
import itsprodigi.matteocasini.steam_clone_backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserProfileServiceImplTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserProfileServiceImpl userProfileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserProfileById_returnsProfile() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setUsername("matteo");

        UserProfile profile = new UserProfile();
        profile.setId(userId);
        profile.setUser(user);
        profile.setNickname("Matty");
        profile.setAvatarUrl("http://img.com/avatar.png");
        profile.setBio("bio test");

        when(userProfileRepository.findById(userId)).thenReturn(Optional.of(profile));

        UserProfileResponseDTO result = userProfileService.getUserProfileById(userId);

        assertNotNull(result);
        assertEquals("Matty", result.getNickname());
        assertEquals("http://img.com/avatar.png", result.getAvatarUrl());
        assertEquals("bio test", result.getBio());
    }

    @Test
    void createOrUpdateUserProfile_createsNewProfile() {
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        UserProfileRequestDTO dto = new UserProfileRequestDTO();
        dto.setNickname(Optional.of("TestNick"));
        dto.setAvatarUrl(Optional.of("http://test.url/img.png"));
        dto.setBio(Optional.of("Test bio"));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userProfileRepository.findById(userId)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userProfileRepository.save(any(UserProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserProfileResponseDTO result = userProfileService.createOrUpdateUserProfile(userId, dto);

        assertNotNull(result);
        assertEquals("TestNick", result.getNickname());
        assertEquals("http://test.url/img.png", result.getAvatarUrl());
        assertEquals("Test bio", result.getBio());
    }

    @Test
    void deleteUserProfile_removesProfile() {
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setUserProfile(new UserProfile());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userProfileRepository.findById(userId)).thenReturn(Optional.of(new UserProfile()));

        assertDoesNotThrow(() -> userProfileService.deleteUserProfile(userId));
        verify(userRepository).save(user);
        assertNull(user.getUserProfile());
    }

    @Test
    void getUserProfileById_notFound_throwsException() {
        UUID userId = UUID.randomUUID();
        when(userProfileRepository.findById(userId)).thenReturn(Optional.empty());

        UserProfileNotFoundException ex = assertThrows(
                UserProfileNotFoundException.class,
                () -> userProfileService.getUserProfileById(userId));

        assertEquals("Profilo utente non trovato per l'utente con ID: " + userId, ex.getMessage());
    }

    @Test
    void deleteUserProfile_userNotFound_throwsException() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userProfileService.deleteUserProfile(userId);
        });

        assertEquals("Utente non trovato con ID: " + userId, ex.getMessage());
    }

    @Test
    void deleteUserProfile_profileNotFound_throwsException() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userProfileRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userProfileService.deleteUserProfile(userId);
        });

        assertEquals("Profilo utente non trovato per l'utente con ID: " + userId, ex.getMessage());
    }
}
