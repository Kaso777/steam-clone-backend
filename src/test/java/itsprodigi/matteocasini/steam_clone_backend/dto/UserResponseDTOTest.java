package itsprodigi.matteocasini.steam_clone_backend.dto;

import itsprodigi.matteocasini.steam_clone_backend.enums.Role;
import itsprodigi.matteocasini.steam_clone_backend.model.User;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserResponseDTOTest {

    @Test
    void testConstructorFromEntity() {
        User user = new User("user", "email@email.com", "password", Role.ROLE_USER);
        user.setId(UUID.randomUUID());

        UserResponseDTO dto = new UserResponseDTO(user);
        assertEquals("user", dto.getUsername());
        assertEquals("ROLE_USER", dto.getRole());
    }

    @Test
    void testGettersAndSetters() {
        UUID id = UUID.randomUUID();
        UserResponseDTO dto = new UserResponseDTO(id, "user", "mail", "ADMIN");

        assertEquals("user", dto.getUsername());
        assertEquals("ADMIN", dto.getRole());
        assertEquals(id, dto.getId());
        assertEquals("mail", dto.getEmail());

        dto.setUsername("newuser");
        assertEquals("newuser", dto.getUsername());
        assertTrue(dto.toString().contains("newuser"));
    }
}
