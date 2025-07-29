package itsprodigi.matteocasini.steam_clone_backend.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Credenziali non valide. Username o password errati.");
    }
}