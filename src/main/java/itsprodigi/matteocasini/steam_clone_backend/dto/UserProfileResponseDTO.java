package itsprodigi.matteocasini.steam_clone_backend.dto;

import java.util.UUID;

/**
 * Data Transfer Object (DTO) per le risposte in uscita (output) relative a UserProfile.
 * Questo DTO è utilizzato quando il server invia i dati di un profilo utente al client
 * (es. in risposta a un metodo GET o dopo una PUT/POST di successo).
 *
 * Contiene tutti i campi che il server intende esporre pubblicamente in una risposta.
 * In questo approccio, l'ID dell'utente viene sempre incluso nella risposta del profilo.
 */
public class UserProfileResponseDTO {

    private UUID userId; // L'ID dell'utente associato, incluso nella risposta.
    private String nickname;
    private String avatarUrl;
    private String bio;

    // Costruttore senza argomenti (NoArgsConstructor)
    public UserProfileResponseDTO() {
    }

    // Costruttore con tutti gli argomenti (AllArgsConstructor)
    public UserProfileResponseDTO(UUID userId, String nickname, String avatarUrl, String bio) {
        this.userId = userId;
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
        this.bio = bio;
    }

    // Getter
    public UUID getUserId() {
        return userId;
    }

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
    public void setUserId(UUID userId) {
        this.userId = userId;
    }

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
        return "UserProfileResponseDTO{" +
               "userId=" + userId +
               ", nickname='" + nickname + '\'' +
               ", avatarUrl='" + avatarUrl + '\'' +
               ", bio='" + bio + '\'' +
               '}';
    }
}