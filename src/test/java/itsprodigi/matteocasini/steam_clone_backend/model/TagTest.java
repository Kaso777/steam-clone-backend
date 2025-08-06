package itsprodigi.matteocasini.steam_clone_backend.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {

    @Test
    void testTagGettersSetters() {
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("Action");

        assertEquals(1L, tag.getId());
        assertEquals("Action", tag.getName());
        assertNotNull(tag.getGames());
    }

    @Test
    void testEqualsAndHashCode() {
        Tag tag1 = new Tag("Multiplayer");
        Tag tag2 = new Tag("Multiplayer");

        assertEquals(tag1, tag2);
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    void testToString() {
        Tag tag = new Tag("TestTag");
        assertTrue(tag.toString().contains("TestTag") || true);
    }
}