package itsprodigi.matteocasini.steam_clone_backend.dto;

import itsprodigi.matteocasini.steam_clone_backend.model.User; // Importa entità User
import java.util.UUID;

/**
 * Data Transfer Object (DTO) per le risposte in uscita (output) relative a User.
 * Questo DTO è utilizzato quando il server invia i dati di un utente al client
 * (es. in risposta a un metodo GET, o dopo una POST/PUT di successo).
 *
 * Contiene i campi che il server intende esporre al client. La password non è inclusa
 * per motivi di sicurezza. Include il ruolo dell'utente e può includere un riferimento
 * al UserProfileResponseDTO per restituire il profilo insieme ai dati base dell'utente.
 */
public class UserResponseDTO {

    // Campo ID: allineato con il nome 'id' nell'entità User per chiarezza di mappatura.
    private UUID id;
    private String username;
    private String email;
    private String role; // Nuovo campo per il ruolo dell'utente

    // Se si vuole includere il profilo utente direttamente nella risposta dell'utente.
    // Questo crea una relazione nidificata nel JSON.
    private UserProfileResponseDTO userProfile;

    // Costruttore senza argomenti (necessario per la deserializzazione JSON/Spring)
    public UserResponseDTO() {
    }

    // Costruttore che prende un'entità User e la mappa al DTO.
    // Questo è un costruttore di convenienza per la mappatura rapida.
    // NOTA: Per le relazioni nidificate (come userProfile), la mappatura completa
    // potrebbe richiedere logica aggiuntiva nel Service per evitare LazyInitializationException
    // e per decidere se includere o meno il profilo.
    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole(); // Mappa il ruolo

        // Mappatura del profilo nidificato (se presente nell'entità e caricato)
        // Questa logica di mappatura può essere più complessa e spesso è meglio farla nel Service.
        // Per ora, lo lasciamo qui per mostrare l'intenzione, ma nel Service avremo un controllo migliore.
        if (user.getUserProfile() != null) {
            // Assumiamo che user.getUserProfile() sia già caricato o che il contesto transazionale lo permetta.
            // Altrimenti, questa riga potrebbe causare una LazyInitializationException.
            this.userProfile = new UserProfileResponseDTO(
                user.getUserProfile().getId(),
                user.getUserProfile().getNickname(),
                user.getUserProfile().getAvatarUrl(),
                user.getUserProfile().getBio()
            );
        }
        // NOTA: NON includiamo la password nel ResponseDTO per sicurezza!
    }

    // Costruttore con tutti i campi (utile per la creazione diretta di DTO nel Service)
    public UserResponseDTO(UUID id, String username, String email, String role, UserProfileResponseDTO userProfile) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.userProfile = userProfile;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() { // Getter per il ruolo
        return role;
    }

    public UserProfileResponseDTO getUserProfile() { // Getter per il profilo nidificato
        return userProfile;
    }

    // Setters
    public void setId(UUID id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) { // Setter per il ruolo
        this.role = role;
    }

    public void setUserProfile(UserProfileResponseDTO userProfile) { // Setter per il profilo nidificato
        this.userProfile = userProfile;
    }

    @Override
    public String toString() {
        return "UserResponseDTO{" +
               "id=" + id +
               ", username='" + username + '\'' +
               ", email='" + email + '\'' +
               ", role='" + role + '\'' +
               ", userProfile=" + userProfile +
               '}';
    }
}