package itsprodigi.matteocasini.steam_clone_backend.service;

import itsprodigi.matteocasini.steam_clone_backend.dto.UserGameRequestDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserGameResponseDTO;
import itsprodigi.matteocasini.steam_clone_backend.exception.ResourceNotFoundException;
import itsprodigi.matteocasini.steam_clone_backend.model.Game;
import itsprodigi.matteocasini.steam_clone_backend.model.User;
import itsprodigi.matteocasini.steam_clone_backend.model.UserGame;
import itsprodigi.matteocasini.steam_clone_backend.model.UserGameId;
import itsprodigi.matteocasini.steam_clone_backend.repository.GameRepository;
import itsprodigi.matteocasini.steam_clone_backend.repository.UserGameRepository;
import itsprodigi.matteocasini.steam_clone_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.time.LocalDate;

@Service
public class UserGameServiceImpl implements UserGameService {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final UserGameRepository userGameRepository;

    // Iniezione delle dipendenze tramite costruttore
    public UserGameServiceImpl(UserRepository userRepository, GameRepository gameRepository, UserGameRepository userGameRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.userGameRepository = userGameRepository;
    }

    @Override
    @Transactional
    public UserGameResponseDTO addGameToUserLibrary(UserGameRequestDTO userGameRequestDTO) {
        // 1. Trova l'utente per UUID, altrimenti lancia eccezione
        User user = userRepository.findById(userGameRequestDTO.getUserUuid())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with UUID: " + userGameRequestDTO.getUserUuid()));

        // 2. Trova il gioco per UUID, altrimenti lancia eccezione
        Game game = gameRepository.findById(userGameRequestDTO.getGameUuid())
                .orElseThrow(() -> new ResourceNotFoundException("Game not found with UUID: " + userGameRequestDTO.getGameUuid()));

        // 3. Controlla se l'utente possiede già il gioco per evitare duplicati
        if (userGameRepository.existsByIdUserUuidAndIdGameUuid(user.getId(), game.getId())) {
            throw new IllegalStateException("User already owns this game.");
        }

        // 4. Crea la nuova entità UserGame
        UserGame userGame = new UserGame(user, game, userGameRequestDTO.getPurchaseDate());

        // 5. Salva la relazione nel database
        UserGame savedUserGame = userGameRepository.save(userGame);

        // 6. Restituisci il DTO di risposta
        return new UserGameResponseDTO(savedUserGame);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserGame> getUserLibrary(UUID userUuid) {
        User user = userRepository.findById(userUuid)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userUuid));
        // Assicurati che il metodo findByUserId esista nel tuo UserGameRepository
        return userGameRepository.findByUserId(user.getId()); // Usa user.getId() che è l'UUID
    }

    @Override
    @Transactional(readOnly = true)
    public UserGameResponseDTO getUserGameEntry(UUID userUuid, UUID gameUuid) {
        // Trova la specifica entry UserGame per l'UUID dell'utente e l'UUID del gioco
        return userGameRepository.findByIdUserUuidAndIdGameUuid(userUuid, gameUuid)
                .map(UserGameResponseDTO::new) // Se trovata, mappa a DTO
                .orElseThrow(() -> new ResourceNotFoundException("Game not found in user's library. User UUID: " + userUuid + ", Game UUID: " + gameUuid));
    }

    @Override
    @Transactional
    public void removeGameFromUserLibrary(UUID userUuid, UUID gameUuid) {
        // Controlla prima se l'entry esiste per lanciare un'eccezione significativa se non esiste
        if (!userGameRepository.existsByIdUserUuidAndIdGameUuid(userUuid, gameUuid)) {
            throw new ResourceNotFoundException("Game not found in user's library to remove. User UUID: " + userUuid + ", Game UUID: " + gameUuid);
        }
        // Elimina l'entry dalla libreria
        userGameRepository.deleteByIdUserUuidAndIdGameUuid(userUuid, gameUuid);
    }
}