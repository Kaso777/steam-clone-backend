package itsprodigi.matteocasini.steam_clone_backend.service;

import itsprodigi.matteocasini.steam_clone_backend.dto.*;
import itsprodigi.matteocasini.steam_clone_backend.exception.GameAlreadyInLibraryException;
import itsprodigi.matteocasini.steam_clone_backend.model.*;
import itsprodigi.matteocasini.steam_clone_backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserGameServiceImpl implements UserGameService {

    private final UserGameRepository userGameRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final UserService userService; // ✅ aggiunto

    @Autowired
    public UserGameServiceImpl(UserGameRepository userGameRepository,
                                UserRepository userRepository,
                                GameRepository gameRepository,
                                UserService userService) { // ✅ aggiunto
        this.userGameRepository = userGameRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.userService = userService; // ✅ aggiunto
    }

    // ✅ Metodo di controllo accesso
    private void checkAccess(UUID targetUserId) {
        User currentUser = userService.getAuthenticatedUser();
        boolean isSelf = currentUser.getId().equals(targetUserId);
        boolean isAdmin = currentUser.getRole().name().equals("ROLE_ADMIN");

        if (!isSelf && !isAdmin) {
            throw new org.springframework.security.access.AccessDeniedException("Non autorizzato.");
        }
    }

    @Override
    @Transactional
    public UserGameResponseDTO addGameToUserLibrary(UserGameRequestDTO dto) {
        checkAccess(dto.getUserUuid()); // ✅ controllo

        User user = userRepository.findById(dto.getUserUuid())
                .orElseThrow(() -> new RuntimeException("Utente non trovato: " + dto.getUserUuid()));

        Game game = gameRepository.findById(dto.getGameUuid())
                .orElseThrow(() -> new RuntimeException("Gioco non trovato: " + dto.getGameUuid()));

        UserGameId id = new UserGameId(user.getId(), game.getId());
        if (userGameRepository.existsById(id)) {
    throw new GameAlreadyInLibraryException("Il gioco è già nella libreria dell'utente.");
}


        UserGame userGame = new UserGame(user, game, dto.getPurchaseDate(), dto.getPlaytimeHours());
        return convertToUserGameResponseDto(userGameRepository.save(userGame));
    }

    @Override
    public Optional<UserGameResponseDTO> getUserGameByIds(UUID userUuid, UUID gameUuid) {
        checkAccess(userUuid); // ✅ controllo
        return userGameRepository.findById(new UserGameId(userUuid, gameUuid))
                .map(this::convertToUserGameResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public UserLibraryResponseDTO getUserLibrary(UUID userUuid) {
        checkAccess(userUuid); // ✅ controllo

        Optional<User> userOpt = userRepository.findById(userUuid);
        if (userOpt.isEmpty()) {
            return new UserLibraryResponseDTO();
        }

        List<UserGame> userGames = userGameRepository.findByUserId(userUuid);
        return new UserLibraryResponseDTO(userOpt.get(), userGames);
    }

    @Override
    @Transactional
    public UserGameResponseDTO updateUserGame(UUID userUuid, UUID gameUuid, UserGameRequestDTO dto) {
        checkAccess(userUuid); // ✅ controllo

        UserGame userGame = userGameRepository.findById(new UserGameId(userUuid, gameUuid))
                .orElseThrow(() -> new RuntimeException(
                        "Associazione utente-gioco non trovata per utente " + userUuid + " e gioco " + gameUuid));

        userGame.setPlaytimeHours(dto.getPlaytimeHours());
        userGame.setPurchaseDate(dto.getPurchaseDate());

        return convertToUserGameResponseDto(userGameRepository.save(userGame));
    }

    @Override
    @Transactional
    public void removeGameFromUserLibrary(UUID userUuid, UUID gameUuid) {
        checkAccess(userUuid); // ✅ controllo

        UserGameId id = new UserGameId(userUuid, gameUuid);
        if (!userGameRepository.existsById(id)) {
            throw new RuntimeException("Associazione non trovata per utente " + userUuid + " e gioco " + gameUuid);
        }
        userGameRepository.deleteById(id);
    }

    //forse da qui è tutto commentabile
    @Override
    public List<UserGameResponseDTO> getAllUserGames() {
        return userGameRepository.findAll().stream()
                .map(this::convertToUserGameResponseDto)
                .collect(Collectors.toList());
    }

    private UserGameResponseDTO convertToUserGameResponseDto(UserGame userGame) {
        return new UserGameResponseDTO(
                new UserResponseDTO(userGame.getUser()),
                new GameResponseDTO(userGame.getGame()),
                userGame.getPurchaseDate(),
                userGame.getPlaytimeHours());
    }

    private LibraryGameItemDTO convertToLibraryItemDto(UserGame userGame) {
        return new LibraryGameItemDTO(
                new GameResponseDTO(userGame.getGame()),
                userGame.getPurchaseDate(),
                userGame.getPlaytimeHours());
    }
}
