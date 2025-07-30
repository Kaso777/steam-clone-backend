package itsprodigi.matteocasini.steam_clone_backend.dto;

import itsprodigi.matteocasini.steam_clone_backend.model.User;
import itsprodigi.matteocasini.steam_clone_backend.model.UserGame;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserLibraryResponseDTO {

    private UserResponseDTO user;
    private List<LibraryGameItemDTO> gamesInLibrary;

    public UserLibraryResponseDTO() {
    }

    public UserLibraryResponseDTO(User user, List<UserGame> userGames) {
        this.user = new UserResponseDTO(user);
        this.gamesInLibrary = userGames.stream()
                .map(LibraryGameItemDTO::new)
                .collect(Collectors.toList());
    }

    public UserLibraryResponseDTO(UserResponseDTO user, List<LibraryGameItemDTO> gamesInLibrary) {
        this.user = user;
        this.gamesInLibrary = gamesInLibrary;
    }

    public UserResponseDTO getUser() {
        return user;
    }

    public List<LibraryGameItemDTO> getGamesInLibrary() {
        return gamesInLibrary;
    }

    public void setUser(UserResponseDTO user) {
        this.user = user;
    }

    public void setGamesInLibrary(List<LibraryGameItemDTO> gamesInLibrary) {
        this.gamesInLibrary = gamesInLibrary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserLibraryResponseDTO that))
            return false;
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