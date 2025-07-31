package itsprodigi.matteocasini.steam_clone_backend.service;

import itsprodigi.matteocasini.steam_clone_backend.dto.GameResponseDTO;
import itsprodigi.matteocasini.steam_clone_backend.model.Game;
import itsprodigi.matteocasini.steam_clone_backend.repository.GameRepository;
import itsprodigi.matteocasini.steam_clone_backend.repository.TagRepository;
import itsprodigi.matteocasini.steam_clone_backend.exception.ResourceNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameServiceImplTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private GameServiceImpl gameService;

    private Game game;
    private UUID gameId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        gameId = UUID.randomUUID();
        game = new Game();
        game.setId(gameId);
        game.setTitle("Test Game");
        game.setPrice(BigDecimal.valueOf(29.99));
        game.setReleaseDate(LocalDate.of(2023, 1, 1));
        game.setDeveloper("Dev Studio");
        game.setPublisher("Pub House");
    }

    @Test
    void getGameById_existingId_returnsGameResponseDTO() {
        when(gameRepository.findByIdWithTags(gameId)).thenReturn(Optional.of(game));

        Optional<GameResponseDTO> result = gameService.getGameById(gameId);

        assertTrue(result.isPresent());
        assertEquals("Test Game", result.get().getTitle());
    }

    @Test
    void getGameById_notFound_throwsException() {
        UUID id = UUID.randomUUID();
        when(gameRepository.findByIdWithTags(id)).thenReturn(Optional.empty());

        Optional<GameResponseDTO> result = gameService.getGameById(id);
        assertFalse(result.isPresent());
    }

    @Test
    void getAllGames_returnsListOfGameResponseDTO() {
        when(gameRepository.findAllWithTags()).thenReturn(List.of(game));

        List<GameResponseDTO> games = gameService.getAllGames();

        assertEquals(1, games.size());
        assertEquals("Test Game", games.get(0).getTitle());
    }

    @Test
    void deleteGame_existingGame_deletesSuccessfully() {
        when(gameRepository.existsById(gameId)).thenReturn(true);
        doNothing().when(gameRepository).deleteById(gameId);

        assertDoesNotThrow(() -> gameService.deleteGame(gameId));
        verify(gameRepository, times(1)).deleteById(gameId);
    }

    @Test
    void deleteGame_nonExistent_throwsException() {
        UUID id = UUID.randomUUID();
        when(gameRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> gameService.deleteGame(id));
    }

    @Test
    void findGamesByTitle_returnsListOfGames() {
        when(gameRepository.findByTitleContainingIgnoreCase("Test")).thenReturn(List.of(game));

        List<GameResponseDTO> result = gameService.findGamesByTitle("Test");

        assertEquals(1, result.size());
        assertEquals("Test Game", result.get(0).getTitle());
    }

    @Test
    void findGamesByDeveloper_returnsListOfGames() {
        when(gameRepository.findByDeveloperIgnoreCase("Dev Studio")).thenReturn(List.of(game));

        List<GameResponseDTO> result = gameService.findGamesByDeveloper("Dev Studio");

        assertEquals(1, result.size());
    }

    @Test
    void findGamesByPublisher_returnsListOfGames() {
        when(gameRepository.findByPublisherIgnoreCase("Pub House")).thenReturn(List.of(game));

        List<GameResponseDTO> result = gameService.findGamesByPublisher("Pub House");

        assertEquals(1, result.size());
    }

    @Test
    void findGamesByTagName_returnsListOfGames() {
        when(gameRepository.findByTags_NameIgnoreCase("Action")).thenReturn(List.of(game));

        List<GameResponseDTO> result = gameService.findGamesByTagName("Action");

        assertEquals(1, result.size());
    }
}
