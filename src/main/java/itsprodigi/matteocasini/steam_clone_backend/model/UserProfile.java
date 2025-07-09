package itsprodigi.matteocasini.steam_clone_backend.model;

import jakarta.persistence.*;
import java.util.UUID; // Se l'ID dell'utente è UUID

@Entity
@Table(name = "user_profiles") // Nome della tabella nel database
public class UserProfile {

    @Id
    // L'ID di UserProfile sarà lo stesso dell'ID dell'utente associato.
    // Questo è un modo comune per implementare OneToOne con chiave condivisa.
    // @GeneratedValue(strategy = GenerationType.IDENTITY) // Non usare IDENTITY se l'ID è condiviso con User
    @Column(name = "user_id") // La colonna ID di UserProfile sarà anche la FK verso User
    private UUID id; // L'ID sarà lo stesso dell'utente a cui si riferisce

    // Relazione OneToOne con User
    // Questo lato è il proprietario della relazione (contiene la FK logica/fisica)
    @OneToOne(fetch = FetchType.LAZY) // Si usa lazy perchè i profili utente non sono sempre necessari
    @MapsId // Indica che l'ID di questa entità è mappato dall'ID dell'entità associata
    @JoinColumn(name = "user_id") // Specifica la colonna della chiave esterna
    private User user;

    @Column(name = "nickname", length = 50)
    private String nickname;

    @Column(name = "avatar_url", length = 255)
    private String avatarUrl;

    @Column(name = "bio", columnDefinition = "TEXT") // Per testi più lunghi
    private String bio;

    // Costruttori
    public UserProfile() {}

    public UserProfile(User user, String nickname, String avatarUrl, String bio) {
        this.user = user;
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
        this.bio = bio;
    }

    // Getter e Setter per tutti i campi

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

    // È buona pratica implementare equals() e hashCode()
    // specialmente per le entità con relazioni.
    // Puoi generarle automaticamente con il tuo IDE.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}