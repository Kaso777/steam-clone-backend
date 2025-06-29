package itsprodigi.matteocasini.steam_clone_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRequestDTO {

    @NotBlank(message = "Lo username non può essere vuoto")
    @Size(min = 3, max = 50, message = "Lo username deve avere tra 3 e 50 caratteri")
    private String username;

    @NotBlank(message = "L'email non può essere vuota")
    @Email(message = "Formato email non valido")
    @Size(max = 100, message = "L'email non può superare i 100 caratteri")
    private String email;

    @NotBlank(message = "La password non può essere vuota")
    @Size(min = 6, message = "La password deve avere almeno 6 caratteri")
    private String password;

    // Costruttore senza argomenti (necessario per la deserializzazione JSON)
    public UserRequestDTO() {
    }

    // Costruttore con argomenti
    public UserRequestDTO(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getter
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Setter (anche se per un DTO di richiesta i setter sono meno usati)
    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserRequestDTO{" +
               "username='" + username + '\'' +
               ", email='" + email + '\'' +
               ", password='" + password + '\'' + // Per il momento la password è visibile per debug, ma poi verrà gestita in modo sicuro
               '}';
    }
}