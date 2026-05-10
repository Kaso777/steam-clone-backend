package io.github.kaso777.steamclone.exception;

import java.util.UUID;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(UUID userId) {
        super("Utente non trovato con ID: " + userId);
    }
}