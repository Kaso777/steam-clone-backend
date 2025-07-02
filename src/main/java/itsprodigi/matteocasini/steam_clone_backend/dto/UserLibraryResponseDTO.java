package itsprodigi.matteocasini.steam_clone_backend.dto;

import itsprodigi.matteocasini.steam_clone_backend.model.UserGame;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserLibraryResponseDTO {
    private UserResponseDTO user;
    private List<LibraryGameDTO> gamesInLibrary;

    // Costruttore che accetta una lista di entità UserGame e le mappa in questo DTO
    public UserLibraryResponseDTO(List<UserGame> userGames) {
        if (userGames != null && !userGames.isEmpty()) {
            // L'utente è lo stesso per tutti gli UserGame, quindi lo prendiamo dal primo
            this.user = new UserResponseDTO(userGames.get(0).getUser());
            // Mappa ogni UserGame a un LibraryGameDTO
            this.gamesInLibrary = userGames.stream()
                                        .map(LibraryGameDTO::new)
                                        .collect(Collectors.toList());
        } else {
            // Se la lista è vuota, l'utente non ha giochi.
            // Inizializza l'utente a null e la lista dei giochi vuota.
            this.user = null;
            this.gamesInLibrary = List.of(); // Restituisce una lista immutabile vuota
        }
    }

    // Costruttore vuoto (necessario per la deserializzazione JSON)
    public UserLibraryResponseDTO() {
    }

    // --- Getters ---
    public UserResponseDTO getUser() {
        return user;
    }

    public List<LibraryGameDTO> getGamesInLibrary() {
        return gamesInLibrary;
    }

    // --- Setters ---
    public void setUser(UserResponseDTO user) {
        this.user = user;
    }

    public void setGamesInLibrary(List<LibraryGameDTO> gamesInLibrary) {
        this.gamesInLibrary = gamesInLibrary;
    }

    // --- Metodi equals, hashCode, toString ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserLibraryResponseDTO that = (UserLibraryResponseDTO) o;
        return Objects.equals(user, that.user) && Objects.equals(gamesInLibrary, that.gamesInLibrary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, gamesInLibrary);
    }

    @Override
    public String toString() {
        return "UserLibraryResponseDTO{" +
               "user=" + user +
               ", gamesInLibrary=" + gamesInLibrary +
               '}';
    }
}