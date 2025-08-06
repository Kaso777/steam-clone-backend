package itsprodigi.matteocasini.steam_clone_backend.dto;

import itsprodigi.matteocasini.steam_clone_backend.model.Game;
import itsprodigi.matteocasini.steam_clone_backend.model.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GameResponseDTOTest {

    @Test
    void testConstructorAndGetters() {
        UUID id = UUID.randomUUID();
        List<TagDTO> tags = List.of(new TagDTO(1L, "Action"));

        GameResponseDTO dto = new GameResponseDTO(id, "Title", BigDecimal.TEN, LocalDate.now(), "Dev", "Pub", tags);

        assertEquals("Title", dto.getTitle());
        assertEquals(BigDecimal.TEN, dto.getPrice());
        assertEquals("Dev", dto.getDeveloper());
        assertEquals("Pub", dto.getPublisher());
        assertEquals(tags, dto.getTags());
        assertTrue(dto.toString().contains("Title"));
    }

    @Test
    void testConstructorFromEntity() {
        Game game = new Game("Title", BigDecimal.TEN, LocalDate.now(), "Dev", "Pub");
        game.setId(UUID.randomUUID());
        Tag tag = new Tag("Action");
        tag.setId(1L);
        game.addTag(tag);

        GameResponseDTO dto = new GameResponseDTO(game);
        assertEquals("Title", dto.getTitle());
        assertEquals(1, dto.getTags().size());
    }
}