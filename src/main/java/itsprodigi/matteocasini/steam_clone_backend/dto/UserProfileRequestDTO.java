package itsprodigi.matteocasini.steam_clone_backend.dto;

import java.util.Optional;

public class UserProfileRequestDTO {

    private Optional<String> nickname = Optional.empty();
    private Optional<String> avatarUrl = Optional.empty();
    private Optional<String> bio = Optional.empty();

    public UserProfileRequestDTO() {
    }

    public UserProfileRequestDTO(Optional<String> nickname, Optional<String> avatarUrl, Optional<String> bio) {
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
        this.bio = bio;
    }

    public Optional<String> getNickname() {
        return nickname;
    }

    public void setNickname(Optional<String> nickname) {
        this.nickname = nickname != null ? nickname : Optional.empty();
    }

    public Optional<String> getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(Optional<String> avatarUrl) {
        this.avatarUrl = avatarUrl != null ? avatarUrl : Optional.empty();
    }

    public Optional<String> getBio() {
        return bio;
    }

    public void setBio(Optional<String> bio) {
        this.bio = bio != null ? bio : Optional.empty();
    }

    @Override
    public String toString() {
        return "UserProfileRequestDTO{" +
                "nickname=" + nickname +
                ", avatarUrl=" + avatarUrl +
                ", bio=" + bio +
                '}';
    }
}