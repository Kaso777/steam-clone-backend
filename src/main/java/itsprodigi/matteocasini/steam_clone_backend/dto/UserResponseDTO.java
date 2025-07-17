package itsprodigi.matteocasini.steam_clone_backend.dto;

import itsprodigi.matteocasini.steam_clone_backend.model.User; // Importa entità User
import java.util.UUID;

public class UserResponseDTO {
    private UUID id; // Questo campo nel DTO manterrà il nome 'uuid' per coerenza API esterna
    private String username;
    private String email;
    private String role;

    // Costruttore vuoto (spesso utile)
    public UserResponseDTO() {
    }

    // Costruttore: prende un'entità User e la mappa al DTO
    public UserResponseDTO(User user) {
        this.id = user.getId(); // Mappa l'ID (UUID) dell'entità all'UUID del DTO
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole();
        // NOTA: NON includiamo la password nel ResponseDTO per sicurezza!
    }

    // Costruttore con tutti i campi (se lo avevi, puoi tenerlo)
    public UserResponseDTO(UUID uuid, String username, String email, String role) {
        this.id = uuid;
        this.username = username;
        this.email = email;
        this.role = role;
    }
// Getters
    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() { // Getter per il ruolo
        return role;
    }

    // Setters
    public void setId(UUID id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) { // Setter per il ruolo
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserResponseDTO{" +
               "id=" + id +
               ", username='" + username + '\'' +
               ", email='" + email + '\'' +
               ", role='" + role + '\'' +
               '}';
    }
}