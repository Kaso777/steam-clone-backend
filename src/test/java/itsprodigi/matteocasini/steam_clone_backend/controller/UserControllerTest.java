package itsprodigi.matteocasini.steam_clone_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import itsprodigi.matteocasini.steam_clone_backend.dto.UserRequestDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserResponseDTO;
import itsprodigi.matteocasini.steam_clone_backend.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;



import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    controllers = UserController.class,
    excludeFilters = {
        @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
            itsprodigi.matteocasini.steam_clone_backend.config.SecurityConfig.class,
            itsprodigi.matteocasini.steam_clone_backend.filter.JwtAuthFilter.class
        })
    }
)
@AutoConfigureMockMvc(addFilters = false) // 🔴 Disattiva i filtri Spring Security
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/users - deve restituire lista utenti")
    void getAllUsers_success() throws Exception {
        UserResponseDTO user1 = new UserResponseDTO(UUID.randomUUID(), "user1", "user1@email.com", "ROLE_USER");
        UserResponseDTO user2 = new UserResponseDTO(UUID.randomUUID(), "user2", "user2@email.com", "ROLE_ADMIN");
        List<UserResponseDTO> fakeUsers = Arrays.asList(user1, user2);

        Mockito.when(userService.getAllUsers()).thenReturn(fakeUsers);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].username").value("user2"));
    }

   @Test
@DisplayName("GET /api/users/{id} - deve restituire l'utente se esiste")
void getUserById_success() throws Exception {
    UUID userId = UUID.randomUUID();
    UserResponseDTO user = new UserResponseDTO(userId, "user1", "user1@email.com", "ROLE_USER");

    Mockito.when(userService.getUserById(userId)).thenReturn(Optional.of(user)); // ✅ FIXATO

    mockMvc.perform(get("/api/users/" + userId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userId.toString()))
            .andExpect(jsonPath("$.username").value("user1"))
            .andExpect(jsonPath("$.email").value("user1@email.com"))
            .andExpect(jsonPath("$.role").value("ROLE_USER"));
}

@Test
@DisplayName("POST /api/users/register - deve registrare un utente con successo")
void registerUser_success() throws Exception {
    // Dati finti
    UserRequestDTO requestDTO = new UserRequestDTO();
    requestDTO.setUsername("newuser");
    requestDTO.setEmail("newuser@email.com");
    requestDTO.setPassword("password123");
    requestDTO.setRole("ROLE_USER");

    UUID generatedId = UUID.randomUUID();
    UserResponseDTO responseDTO = new UserResponseDTO(
            generatedId,
            "newuser",
            "newuser@email.com",
            "ROLE_USER"
    );

    // Mock del service
    Mockito.when(userService.registerUser(Mockito.any(UserRequestDTO.class)))
            .thenReturn(responseDTO);

    mockMvc.perform(
            post("/api/users/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDTO))
    )
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(generatedId.toString()))
            .andExpect(jsonPath("$.username").value("newuser"))
            .andExpect(jsonPath("$.email").value("newuser@email.com"))
            .andExpect(jsonPath("$.role").value("ROLE_USER"));
}

@Test
@DisplayName("DELETE /api/users/{id} - deve eliminare l'utente")
void deleteUserById_success() throws Exception {
    UUID userId = UUID.randomUUID();

    // Non ci serve un thenReturn, perché delete è void
    Mockito.doNothing().when(userService).deleteUser(userId);

    mockMvc.perform(delete("/api/users/{id}", userId))
            .andExpect(status().isNoContent());
}

@Test
@DisplayName("GET /api/users/{id} - deve restituire 404 se l'utente non esiste")
void getUserById_notFound() throws Exception {
    UUID fakeId = UUID.randomUUID();

    Mockito.when(userService.getUserById(fakeId))
            .thenThrow(new RuntimeException("User not found"));

    mockMvc.perform(get("/api/users/{id}", fakeId))
            .andExpect(status().isNotFound());
}



}
