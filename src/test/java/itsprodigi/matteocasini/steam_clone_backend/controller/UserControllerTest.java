/*
package itsprodigi.matteocasini.steam_clone_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserRequestDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserResponseDTO;
import itsprodigi.matteocasini.steam_clone_backend.enums.Role;
import itsprodigi.matteocasini.steam_clone_backend.exception.InvalidRoleException;
import itsprodigi.matteocasini.steam_clone_backend.model.User;
import itsprodigi.matteocasini.steam_clone_backend.service.UserService;
import itsprodigi.matteocasini.steam_clone_backend.config.TestSecurityConfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(TestSecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID userId;
    private User user;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new User("testuser", "test@example.com", "password", Role.ROLE_USER);
        user.setId(userId);

        userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(userId);
        userResponseDTO.setUsername("testuser");
        userResponseDTO.setEmail("test@example.com");
    }

    @Test
    void shouldRegisterUser() throws Exception {
        UserRequestDTO request = new UserRequestDTO("testuser", "test@example.com", "password", Role.ROLE_USER.name());
        when(userService.registerUser(Mockito.any())).thenReturn(userResponseDTO);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(userResponseDTO));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void shouldGetUserByIdIfAdminOrSelf() throws Exception {
        User admin = new User("admin", "admin@example.com", "password", Role.ROLE_ADMIN);
        admin.setId(UUID.randomUUID());

        when(userService.getAuthenticatedUser()).thenReturn(admin);
        when(userService.getUserById(userId)).thenReturn(userResponseDTO);

        mockMvc.perform(get("/api/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void shouldThrowInvalidRoleExceptionIfUserTriesToAccessAnotherUser() throws Exception {
        User otherUser = new User("another", "another@example.com", "password", Role.ROLE_USER);
        otherUser.setId(UUID.randomUUID());

        when(userService.getAuthenticatedUser()).thenReturn(otherUser);

        mockMvc.perform(get("/api/users/" + userId))
                .andExpect(status().isNotFound()) // fallbackHandler viene chiamato
                .andExpect(content().string("Utente non trovato"));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        UserRequestDTO request = new UserRequestDTO("updatedUser", "updated@example.com", "newpass", Role.ROLE_USER.name());
        userResponseDTO.setUsername("updatedUser");

        when(userService.updateUser(Mockito.eq(userId), Mockito.any())).thenReturn(userResponseDTO);

        mockMvc.perform(put("/api/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updatedUser"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/" + userId))
                .andExpect(status().isNoContent());
    }
}
 */