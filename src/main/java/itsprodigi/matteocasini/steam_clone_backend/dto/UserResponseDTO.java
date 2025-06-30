package itsprodigi.matteocasini.steam_clone_backend.dto;

import itsprodigi.matteocasini.steam_clone_backend.model.User; // Importa entità User
import java.util.UUID;

public class UserResponseDTO {
    private UUID uuid; // Questo campo nel DTO manterrà il nome 'uuid' per coerenza API esterna
    private String username;
    private String email;

    // Costruttore vuoto (spesso utile)
    public UserResponseDTO() {
    }

    // Costruttore: prende un'entità User e la mappa al DTO
    public UserResponseDTO(User user) {
        this.uuid = user.getId(); // Mappa l'ID (UUID) dell'entità all'UUID del DTO
        this.username = user.getUsername();
        this.email = user.getEmail();
        // NOTA: NON includiamo la password nel ResponseDTO per sicurezza!
    }

    // Costruttore con tutti i campi (se lo avevi, puoi tenerlo)
    public UserResponseDTO(UUID uuid, String username, String email) {
        this.uuid = uuid;
        this.username = username;
        this.email = email;
    }

    // Getters e Setters
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserResponseDTO{" +
                "uuid=" + uuid +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}