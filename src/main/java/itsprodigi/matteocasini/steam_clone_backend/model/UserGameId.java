package itsprodigi.matteocasini.steam_clone_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Classe per la chiave primaria composta di UserGame.
 */
@Embeddable
public class UserGameId implements Serializable {

    @Column(name = "user_uuid", columnDefinition = "UUID", nullable = false)
    private UUID userUuid;

    @Column(name = "game_uuid", columnDefinition = "UUID", nullable = false)
    private UUID gameUuid;

    public UserGameId() {
    }

    public UserGameId(UUID userUuid, UUID gameUuid) {
        this.userUuid = userUuid;
        this.gameUuid = gameUuid;
    }

    public UUID getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(UUID userUuid) {
        this.userUuid = userUuid;
    }

    public UUID getGameUuid() {
        return gameUuid;
    }

    public void setGameUuid(UUID gameUuid) {
        this.gameUuid = gameUuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserGameId that))
            return false;
        return Objects.equals(userUuid, that.userUuid) &&
                Objects.equals(gameUuid, that.gameUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userUuid, gameUuid);
    }

    @Override
    public String toString() {
        return "UserGameId{" +
                "userUuid=" + userUuid +
                ", gameUuid=" + gameUuid +
                '}';
    }
}