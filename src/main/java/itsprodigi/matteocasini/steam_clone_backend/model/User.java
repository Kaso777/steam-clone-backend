package itsprodigi.matteocasini.steam_clone_backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import java.util.*;

// Import di Spring Security
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Entity
@Table(name = "users")
// Implementiamo l'interfaccia UserDetails
public class User implements UserDetails { // <-- AGGIUNTO 'implements UserDetails'

    @Id // Indica che questo campo è la chiave primaria
    @GeneratedValue // Permette a Hibernate di generare il valore
    @UuidGenerator // Questa annotazione specifica il generatore UUID
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "role", nullable = false, length = 20) // "USER" o "ADMIN"
    private String role; // Questo campo verrà utilizzato per i ruoli/autorizzazioni

    // Relazione OneToOne con UserProfile
    // Questo lato è il non proprietario della relazione (non contiene la FK)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserProfile userProfile;

    // Relazione OneToMany con UserGame (per la libreria, lato non proprietario)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserGame> userGames = new HashSet<>();

    // Costruttori
    public User() {
    }

    public User(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getter (esistenti)
    public UUID getId() {
        return id;
    }

    // Nota: getUsername() e getPassword() esistono già e sono richiesti da UserDetails
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public Set<UserGame> getUserGames() {
        return userGames;
    }

    // Setter
    public void setId(UUID id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
        if (userProfile != null) {
            userProfile.setUser(this); // Mantiene la bidirezionalità
        }
    }
    // L'if sopra serve a garantire che quando si imposta un UserProfile, il campo user di UserProfile venga aggiornato correttamente per
    // avere una relazione bidirezionale corretta degli oggetti Java utilizzati in memoria. 

    // Helper method to add a UserGame
    public void addUserGame(UserGame userGame) {
        userGames.add(userGame);
        userGame.setUser(this);
    }

    public void removeUserGame(UserGame userGame) {
        userGames.remove(userGame);
        userGame.setUser(null);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }


    // **********************************************
    // METODI IMPLEMENTATI DALL'INTERFACCIA UserDetails
    // **********************************************

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Converte la stringa del ruolo (es. "USER", "ADMIN") in un'istanza di GrantedAuthority
        // Spring Security si aspetta le autorità in formato "ROLE_NOME_RUOLO" per l'annotazione @PreAuthorize
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public boolean isAccountNonExpired() {
        // Indica se l'account dell'utente è scaduto. Per ora, sempre true.
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Indica se l'account dell'utente è bloccato. Per ora, sempre true.
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Indica se le credenziali dell'utente (password) sono scadute. Per ora, sempre true.
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Indica se l'utente è abilitato (attivo). Per ora, sempre true.
        return true;
    }
}