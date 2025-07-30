package itsprodigi.matteocasini.steam_clone_backend.service;

import itsprodigi.matteocasini.steam_clone_backend.dto.UserGameRequestDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserGameResponseDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserLibraryResponseDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserGameService {

    UserGameResponseDTO addGameToUserLibrary(UserGameRequestDTO userGameRequestDTO);

    Optional<UserGameResponseDTO> getUserGameByIds(UUID userUuid, UUID gameUuid);

    UserLibraryResponseDTO getUserLibrary(UUID userUuid);

    UserGameResponseDTO updateUserGame(UUID userUuid, UUID gameUuid, UserGameRequestDTO userGameRequestDTO);

    void removeGameFromUserLibrary(UUID userUuid, UUID gameUuid);

    List<UserGameResponseDTO> getAllUserGames();
}