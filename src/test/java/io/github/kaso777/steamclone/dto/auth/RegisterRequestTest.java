package io.github.kaso777.steamclone.dto.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RegisterRequestTest {

    @Test
    void testGettersAndSetters() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("user");
        request.setEmail("user@email.com");
        request.setPassword("password");

        assertEquals("user", request.getUsername());
        assertEquals("user@email.com", request.getEmail());
        assertEquals("password", request.getPassword());
    }
}
