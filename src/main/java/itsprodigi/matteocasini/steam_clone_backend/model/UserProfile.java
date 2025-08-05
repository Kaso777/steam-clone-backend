package itsprodigi.matteocasini.steam_clone_backend.model;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

/**
 * Profilo associato a un utente. Contiene dati personalizzabili come nickname,
 * avatar e bio.
 */
@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "nickname", length = 50)
    private String nickname;

    @Column(name = "avatar_url", length = 255)
    private String avatarUrl;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    // Costruttori
    public UserProfile() {
    }

    public UserProfile(User user, String nickname, String avatarUrl, String bio) {
        this.user = user;
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
        this.bio = bio;
    }

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    // equals/hashCode basati sull'ID
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserProfile that))
            return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // toString leggero per evitare cicli infiniti o dati sensibili
    @Override
    public String toString() {
        return "UserProfile{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }
}