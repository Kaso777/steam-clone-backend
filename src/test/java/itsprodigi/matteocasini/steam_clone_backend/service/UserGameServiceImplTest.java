package itsprodigi.matteocasini.steam_clone_backend.service;

import itsprodigi.matteocasini.steam_clone_backend.dto.UserGameRequestDTO;
import itsprodigi.matteocasini.steam_clone_backend.enums.Role;
import itsprodigi.matteocasini.steam_clone_backend.model.*;
import itsprodigi.matteocasini.steam_clone_backend.repository.GameRepository;
import itsprodigi.matteocasini.steam_clone_backend.repository.UserGameRepository;
import itsprodigi.matteocasini.steam_clone_backend.repository.UserRepository;
import itsprodigi.matteocasini.steam_clone_backend.service.UserGameServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserGameServiceImplTest {

    @Mock
    private UserGameRepository userGameRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private UserGameServiceImpl userGameService;

    private UUID userId;
    private UUID gameId;
    private User user;
    private Game game;
    private UserGame userGame;
    private UserGameRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userId = UUID.randomUUID();
        gameId = UUID.randomUUID();

        user = new User();
        user.setId(userId);
        user.setRole(Role.ROLE_USER);

        game = new Game();
        game.setId(gameId);

        requestDTO = new UserGameRequestDTO();
        requestDTO.setUserUuid(userId);
        requestDTO.setGameUuid(gameId);
        requestDTO.setPurchaseDate(LocalDate.now());
        requestDTO.setPlaytimeHours(10);

        userGame = new UserGame(user, game, requestDTO.getPurchaseDate(), requestDTO.getPlaytimeHours());
    }

    @Test
    void testAddGameToUserLibrary() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(userGameRepository.existsById(any())).thenReturn(false);
        when(userGameRepository.save(any())).thenReturn(userGame);

        var result = userGameService.addGameToUserLibrary(requestDTO);

        assertNotNull(result);
        assertEquals(userId, result.getUser().getId());
        assertEquals(gameId, result.getGame().getId());
    }

    @Test
    void testGetUserGameByIds() {
        UserGameId id = new UserGameId(userId, gameId);
        when(userGameRepository.findById(id)).thenReturn(Optional.of(userGame));

        var result = userGameService.getUserGameByIds(userId, gameId);

        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getUser().getId());
    }

    @Test
    void testGetAllUserGames() {
        when(userGameRepository.findAll()).thenReturn(List.of(userGame));

        var result = userGameService.getAllUserGames();

        assertEquals(1, result.size());
        assertEquals(userId, result.get(0).getUser().getId());
    }

    @Test
    void testRemoveGameFromUserLibrary() {
        UserGameId id = new UserGameId(userId, gameId);
        when(userGameRepository.existsById(id)).thenReturn(true);
        doNothing().when(userGameRepository).deleteById(id);

        assertDoesNotThrow(() -> userGameService.removeGameFromUserLibrary(userId, gameId));
    }

    @Test
    void testUpdateUserGame() {
        UserGameId id = new UserGameId(userId, gameId);
        when(userGameRepository.findById(id)).thenReturn(Optional.of(userGame));
        when(userGameRepository.save(any())).thenReturn(userGame);

        var result = userGameService.updateUserGame(userId, gameId, requestDTO);

        assertNotNull(result);
        assertEquals(userId, result.getUser().getId());
        assertEquals(10, result.getPlaytimeHours());
    }

    @Test
    void testGetUserLibrary() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userGameRepository.findByUserId(userId)).thenReturn(List.of(userGame));

        var result = userGameService.getUserLibrary(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUser().getId());
        assertEquals(1, result.getGamesInLibrary().size());
    }
}
