package itsprodigi.matteocasini.steam_clone_backend.exception;

public class InvalidUserProfileDataException extends RuntimeException {
    public InvalidUserProfileDataException(String message) {
        super(message);
    }
}
