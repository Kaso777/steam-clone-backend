package io.github.kaso777.steamclone.exception;

import java.util.UUID;

public class UserProfileNotFoundException extends RuntimeException {
    public UserProfileNotFoundException(UUID userId) {
        super("Profilo utente non trovato per l'utente con ID: " + userId);
    }
}