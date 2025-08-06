package itsprodigi.matteocasini.steam_clone_backend.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void testGameGettersSetters() {
        Game game = new Game();
        UUID id = UUID.randomUUID();

        game.setId(id);
        game.setTitle("Test Game");
        game.setPrice(BigDecimal.valueOf(19.99));
        game.setReleaseDate(LocalDate.of(2023, 1, 1));
        game.setDeveloper("Test Dev");
        game.setPublisher("Test Pub");

        assertEquals(id, game.getId());
        assertEquals("Test Game", game.getTitle());
        assertEquals(BigDecimal.valueOf(19.99), game.getPrice());
        assertEquals(LocalDate.of(2023, 1, 1), game.getReleaseDate());
        assertEquals("Test Dev", game.getDeveloper());
        assertEquals("Test Pub", game.getPublisher());
        assertNotNull(game.getTags());
        assertNotNull(game.getUserGames());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        Game game1 = new Game();
        game1.setId(id);

        Game game2 = new Game();
        game2.setId(id);

        assertEquals(game1, game2);
        assertEquals(game1.hashCode(), game2.hashCode());
    }

    @Test
    void testToString() {
        Game game = new Game();
        game.setTitle("Test");
        assertTrue(game.toString().contains("Test"));
    }
}