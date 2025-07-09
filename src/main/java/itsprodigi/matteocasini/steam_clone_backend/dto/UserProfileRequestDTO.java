package itsprodigi.matteocasini.steam_clone_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) per le richieste in ingresso (input) relative a UserProfile.
 * Questo DTO è utilizzato quando un client invia dati al server per creare o aggiornare
 * un profilo utente (es. tramite un metodo PUT o POST).
 *
 * Contiene solo i campi che il client è responsabile di fornire. L'ID dell'utente (userId)
 * non è incluso qui, poiché tipicamente viene fornito nel percorso URL della richiesta (es. /users/{userId}/profile)
 * o derivato dal token di autenticazione dell'utente.
 */
public class UserProfileRequestDTO {

    @NotBlank(message = "Il nickname non può essere vuoto o composto solo da spazi.")
    @Size(min = 3, max = 50, message = "Il nickname deve contenere tra 3 e 50 caratteri.")
    private String nickname;

    @Size(max = 255, message = "L'URL dell'avatar non può superare i 255 caratteri.")
    private String avatarUrl;

    @Size(max = 1000, message = "La biografia non può superare i 1000 caratteri.")
    private String bio;

    // Costruttore senza argomenti (NoArgsConstructor)
    public UserProfileRequestDTO() {}

    // Costruttore con tutti gli argomenti (AllArgsConstructor)
    public UserProfileRequestDTO(String nickname, String avatarUrl, String bio) {
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
        this.bio = bio;
    }

    // Getter
    public String getNickname() {
        return nickname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getBio() {
        return bio;
    }

    // Setter
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public String toString() {
        return "UserProfileRequestDTO{" +
               "nickname='" + nickname + '\'' +
               ", avatarUrl='" + avatarUrl + '\'' +
               ", bio='" + bio + '\'' +
               '}';
    }
}