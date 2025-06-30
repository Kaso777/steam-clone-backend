package itsprodigi.matteocasini.steam_clone_backend.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id // Indica che questo campo è la chiave primaria
    // `columnDefinition = "UUID"` è importante per H2 e PostgreSQL, dice al DB che tipo di colonna è.
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @PrePersist // Questo metodo viene eseguito automaticamente prima di salvare una nuova entità nel DB
    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID(); // Genera un UUID per l'ID primario
        }
    }

    // Getter e Setter per il nuovo ID (UUID)
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    // Se avevi i getter/setter per il vecchio campo `uuid`, rimuovili:
    // public UUID getUuid() { return uuid; }
    // public void setUuid(UUID uuid) { this.uuid = uuid; }

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

    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", username='" + username + '\'' +
               ", email='" + email + '\'' +
               '}';
    }
}