package itsprodigi.matteocasini.steam_clone_backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO per la registrazione di un nuovo utente.
 */
public class RegisterRequest {

    @NotBlank(message = "Lo username non può essere vuoto")
    @Size(min = 3, max = 50, message = "Lo username deve contenere tra 3 e 50 caratteri")
    private String username;

    @NotBlank(message = "L'email non può essere vuota")
    @Email(message = "L'email deve essere valida")
    private String email;

    @NotBlank(message = "La password non può essere vuota")
    @Size(min = 6, message = "La password deve contenere almeno 6 caratteri")
    private String password;

    public RegisterRequest() {
        // Costruttore richiesto da Spring per la deserializzazione JSON
    }

    public RegisterRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getter e Setter

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
