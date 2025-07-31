/*

package itsprodigi.matteocasini.steam_clone_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserRequestDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserResponseDTO;
import itsprodigi.matteocasini.steam_clone_backend.enums.Role;
import itsprodigi.matteocasini.steam_clone_backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerUser_success() throws Exception {
        UserRequestDTO request = new UserRequestDTO("matteo", "matteo@example.com", "password123", "ROLE_USER");
        UserResponseDTO response = new UserResponseDTO(UUID.randomUUID(), "matteo", "matteo@example.com", "ROLE_USER");

        Mockito.when(userService.registerUser(any(UserRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("matteo"));
    }

    @Test
    void getUserById_success() throws Exception {
        UUID userId = UUID.randomUUID();
        UserResponseDTO response = new UserResponseDTO(userId, "matteo", "matteo@example.com", "ROLE_USER");

        Mockito.when(userService.getUserById(userId)).thenReturn(response);

        mockMvc.perform(get("/api/users/" + userId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("matteo"));
    }

    @Test
    void deleteUserById_success() throws Exception {
        UUID userId = UUID.randomUUID();

        Mockito.doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(delete("/api/users/" + userId))
            .andExpect(status().isNoContent());
    }
}
 */