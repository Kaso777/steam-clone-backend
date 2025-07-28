package itsprodigi.matteocasini.steam_clone_backend.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameRequestDTOTest {

    @Test
    void testGettersAndSetters() {
        GameRequestDTO dto = new GameRequestDTO();
        dto.setTitle("Test Game");
        dto.setPrice(BigDecimal.valueOf(19.99));
        dto.setReleaseDate(LocalDate.now());
        dto.setDeveloper("Test Dev");
        dto.setPublisher("Test Pub");
        dto.setTagNames(List.of("Action", "Multiplayer"));

        assertEquals("Test Game", dto.getTitle());
        assertEquals(BigDecimal.valueOf(19.99), dto.getPrice());
        assertNotNull(dto.getReleaseDate());
        assertEquals("Test Dev", dto.getDeveloper());
        assertEquals("Test Pub", dto.getPublisher());
        assertEquals(List.of("Action", "Multiplayer"), dto.getTagNames());
        assertTrue(dto.toString().contains("Test Game"));
    }
}
