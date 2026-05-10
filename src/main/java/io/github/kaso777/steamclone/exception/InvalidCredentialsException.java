package io.github.kaso777.steamclone.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Credenziali non valide. Username o password errati.");
    }
}