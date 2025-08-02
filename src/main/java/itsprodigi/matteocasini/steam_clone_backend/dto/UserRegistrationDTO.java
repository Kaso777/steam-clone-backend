package itsprodigi.matteocasini.steam_clone_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRegistrationDTO {

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

    @NotBlank(message = "Il ruolo non può essere vuoto o composto solo da spazi.")
    private String role;

    public UserRegistrationDTO() {
    }

    public UserRegistrationDTO(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserRegistrationDTO{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}