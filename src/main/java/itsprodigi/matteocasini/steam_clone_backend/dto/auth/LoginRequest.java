package itsprodigi.matteocasini.steam_clone_backend.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO per la richiesta di login.
 */
public class LoginRequest {

    @NotBlank(message = "Lo username non può essere vuoto")
    @Size(min = 3, max = 50, message = "Lo username deve contenere tra 3 e 50 caratteri")
    private String username;

    @NotBlank(message = "La password non può essere vuota")
    @Size(min = 6, message = "La password deve contenere almeno 6 caratteri")
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter e Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}