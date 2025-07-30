package itsprodigi.matteocasini.steam_clone_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserProfileRequestDTO {

    @NotBlank(message = "Il nickname non può essere vuoto o composto solo da spazi.")
    @Size(min = 3, max = 50, message = "Il nickname deve contenere tra 3 e 50 caratteri.")
    private String nickname;

    @Size(max = 255, message = "L'URL dell'avatar non può superare i 255 caratteri.")
    private String avatarUrl;

    @Size(max = 1000, message = "La biografia non può superare i 1000 caratteri.")
    private String bio;

    public UserProfileRequestDTO() {
    }

    public UserProfileRequestDTO(String nickname, String avatarUrl, String bio) {
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
        this.bio = bio;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getBio() {
        return bio;
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