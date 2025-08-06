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
    private final UserService userService;

    @Autowired
    public UserGameServiceImpl(UserGameRepository userGameRepository,
            UserRepository userRepository,
            GameRepository gameRepository,
            UserService userService) {
        this.userGameRepository = userGameRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.userService = userService;
    }

    /**
     * Verifica se l'utente autenticato ha accesso alla libreria specificata.
     * Consente l'accesso solo se l'utente è se stesso o un amministratore.
     */
    private void checkAccess(UUID targetUserId) {
        User currentUser = userService.getAuthenticatedUser();
        boolean isSelf = currentUser.getId().equals(targetUserId);
        boolean isAdmin = currentUser.getRole().name().equals("ROLE_ADMIN");

        if (!isSelf && !isAdmin) {
            throw new org.springframework.security.access.AccessDeniedException("Non autorizzato.");
        }
    }

    /**
     * Aggiunge un gioco alla libreria di un utente.
     * Lancia un'eccezione se il gioco è già presente.
     */
    @Override
    @Transactional
    public UserGameResponseDTO addGameToUserLibrary(UserGameRequestDTO dto) {
        checkAccess(dto.getUserUuid());

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

    /**
     * Recupera un'associazione utente-gioco specifica.
     * Non attualmente utilizzato, ma potenzialmente utile.
     */
    @Override
    public Optional<UserGameResponseDTO> getUserGameByIds(UUID userUuid, UUID gameUuid) {
        checkAccess(userUuid);
        return userGameRepository.findById(new UserGameId(userUuid, gameUuid))
                .map(this::convertToUserGameResponseDto);
    }

    /**
     * Recupera la libreria di giochi di un determinato utente.
     */
    @Override
    @Transactional(readOnly = true)
    public UserLibraryResponseDTO getUserLibrary(UUID userUuid) {
        checkAccess(userUuid);

        Optional<User> userOpt = userRepository.findById(userUuid);
        if (userOpt.isEmpty()) {
            return new UserLibraryResponseDTO();
        }

        List<UserGame> userGames = userGameRepository.findByUserId(userUuid);
        return new UserLibraryResponseDTO(userOpt.get(), userGames);
    }

    /**
     * Aggiorna le informazioni di un gioco nella libreria di un utente.
     */
    @Override
    @Transactional
    public UserGameResponseDTO updateUserGame(UUID userUuid, UUID gameUuid, UserGameRequestDTO dto) {
        checkAccess(userUuid);

        UserGame userGame = userGameRepository.findById(new UserGameId(userUuid, gameUuid))
                .orElseThrow(() -> new RuntimeException(
                        "Associazione utente-gioco non trovata per utente " + userUuid + " e gioco " + gameUuid));

        userGame.setPlaytimeHours(dto.getPlaytimeHours());
        userGame.setPurchaseDate(dto.getPurchaseDate());

        return convertToUserGameResponseDto(userGameRepository.save(userGame));
    }

    /**
     * Rimuove un gioco dalla libreria di un utente.
     */
    @Override
    @Transactional
    public void removeGameFromUserLibrary(UUID userUuid, UUID gameUuid) {
        checkAccess(userUuid);

        UserGameId id = new UserGameId(userUuid, gameUuid);
        if (!userGameRepository.existsById(id)) {
            throw new RuntimeException("Associazione non trovata per utente " + userUuid + " e gioco " + gameUuid);
        }
        userGameRepository.deleteById(id);
    }

    /**
     * Recupera tutte le associazioni utente-gioco presenti nel sistema.
     * Non attualmente esposto come endpoint pubblico; utile per uso interno o per
     * amministratori.
     */
    @Override
    public List<UserGameResponseDTO> getAllUserGames() {
        return userGameRepository.findAll().stream()
                .map(this::convertToUserGameResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Converte un'associazione UserGame in un DTO di risposta.
     */
    private UserGameResponseDTO convertToUserGameResponseDto(UserGame userGame) {
        return new UserGameResponseDTO(
                new UserResponseDTO(userGame.getUser()),
                new GameResponseDTO(userGame.getGame()),
                userGame.getPurchaseDate(),
                userGame.getPlaytimeHours());
    }

    /*
     * Metodo per la conversione alternativa in un oggetto semplificato della libreria.
     * Non attualmente utilizzato, ma potenzialmente utile.
     *
     * private LibraryGameItemDTO convertToLibraryItemDto(UserGame userGame) {
     *     return new LibraryGameItemDTO(
     *         new GameResponseDTO(userGame.getGame()),
     *         userGame.getPurchaseDate(),
     *         userGame.getPlaytimeHours());
     * }
     */
}