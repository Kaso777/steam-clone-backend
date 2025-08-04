package itsprodigi.matteocasini.steam_clone_backend.exception;

public class GameAlreadyInLibraryException extends RuntimeException {
    public GameAlreadyInLibraryException(String message) {
        super(message);
    }
}
