package itsprodigi.matteocasini.steam_clone_backend.exception;

import java.util.UUID;

public class UserProfileNotFoundException extends RuntimeException {
    public UserProfileNotFoundException(UUID userId) {
        super("Profilo utente non trovato per l'utente con ID: " + userId);
    }
}