package itsprodigi.matteocasini.steam_clone_backend.model;

import itsprodigi.matteocasini.steam_clone_backend.enums.Role;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserGettersSetters() {
        User user = new User();
        UUID id = UUID.randomUUID();

        user.setId(id);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setRole(Role.ROLE_USER);

        assertEquals(id, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals(Role.ROLE_USER, user.getRole());
        assertNotNull(user.getUserGames());
    }

    @Test
    void testUserDetailsMethods() {
        User user = new User("test", "email", "pass", Role.ROLE_ADMIN);
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
        assertNotNull(user.getAuthorities());
    }

    @Test
    void testToString() {
        User user = new User();
        user.setUsername("toStringUser");
        assertTrue(user.toString().contains("toStringUser"));
    }
}