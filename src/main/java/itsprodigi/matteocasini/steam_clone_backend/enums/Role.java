package itsprodigi.matteocasini.steam_clone_backend.enums;

/**
 * Enum per definire i ruoli degli utenti nell'applicazione.
 * Utilizza un prefisso "ROLE_" per conformarsi alle convenzioni di Spring Security.
 */
public enum Role {
    ROLE_USER,  // Ruolo standard per gli utenti normali
    ROLE_ADMIN  // Ruolo per gli amministratori, con privilegi maggiori
}