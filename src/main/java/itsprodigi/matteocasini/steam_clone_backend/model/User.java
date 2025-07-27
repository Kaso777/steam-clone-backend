package itsprodigi.matteocasini.steam_clone_backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import java.util.*;

// Import di Spring Security
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import itsprodigi.matteocasini.steam_clone_backend.enums.Role;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    // --- MODIFICATO QUI ---
    @Enumerated(EnumType.STRING) // Indica a JPA di salvare l'enum come stringa nel DB (es. "ROLE_USER")
    @Column(name = "role", nullable = false, length = 20)
    private Role role; // <-- CAMBIATO DA String A Role
    // ----------------------

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserProfile userProfile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserGame> userGames = new HashSet<>();

    // Costruttori
    public User() {
    }

    // --- MODIFICATO QUI ---
    public User(String username, String email, String password, Role role) { // <-- TIPO DI 'role' CAMBIATO
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }
    // ----------------------

    // Getter
    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // --- MODIFICATO QUI ---
    public Role getRole() { // <-- TIPO DI RITORNO CAMBIATO
        return role;
    }
    // ----------------------

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

    // --- MODIFICATO QUI ---
    public void setRole(Role role) { // <-- TIPO DI PARAMETRO CAMBIATO
        this.role = role;
    }
    // ----------------------

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
        if (userProfile != null) {
            userProfile.setUser(this);
        }
    }

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
                ", role='" + role + '\'' + // Qui verrà stampato il nome dell'enum (es. ROLE_USER)
                '}';
    }

    // **********************************************
    // METODI IMPLEMENTATI DALL'INTERFACCIA UserDetails
    // **********************************************

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    

}