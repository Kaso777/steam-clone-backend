package itsprodigi.matteocasini.steam_clone_backend.exception;

import java.util.UUID;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(UUID userId) {
        super("Utente non trovato con ID: " + userId);
    }
}