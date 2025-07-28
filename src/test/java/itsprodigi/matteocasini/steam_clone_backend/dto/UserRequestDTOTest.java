package itsprodigi.matteocasini.steam_clone_backend.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRequestDTOTest {

    @Test
    void testGettersAndSetters() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setUsername("user");
        dto.setEmail("user@email.com");
        dto.setPassword("password");
        dto.setRole("USER");

        assertEquals("user", dto.getUsername());
        assertEquals("user@email.com", dto.getEmail());
        assertEquals("password", dto.getPassword());
        assertEquals("USER", dto.getRole());
        assertTrue(dto.toString().contains("user"));
    }
}
