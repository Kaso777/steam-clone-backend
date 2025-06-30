package itsprodigi.matteocasini.steam_clone_backend.service;

import itsprodigi.matteocasini.steam_clone_backend.dto.GameRequestDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.GameResponseDTO;
import itsprodigi.matteocasini.steam_clone_backend.exception.ResourceNotFoundException;
import itsprodigi.matteocasini.steam_clone_backend.model.Game;
import itsprodigi.matteocasini.steam_clone_backend.repository.GameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import per @Transactional

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service // Indica che questa classe è un componente di servizio di Spring
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository; // Inietta il GameRepository

    // Costruttore per l'iniezione delle dipendenze (Dependency Injection)
    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    @Transactional // Assicura che l'operazione sia atomica (o tutto o niente)
    public GameResponseDTO createGame(GameRequestDTO gameRequestDTO) {
        // Mappa il DTO di richiesta all'entità Game
        Game game = new Game();
        game.setTitle(gameRequestDTO.getTitle());
        game.setGenre(gameRequestDTO.getGenre());
        game.setPrice(gameRequestDTO.getPrice());
        game.setReleaseDate(gameRequestDTO.getReleaseDate());
        game.setDeveloper(gameRequestDTO.getDeveloper());
        game.setPublisher(gameRequestDTO.getPublisher());

        // generateUuid() viene chiamato automaticamente da @PrePersist in Game.java prima del salvataggio
        Game savedGame = gameRepository.save(game); // Salva il gioco nel database
        return new GameResponseDTO(savedGame); // Restituisce il DTO di risposta
    }

    @Override
    @Transactional(readOnly = true) // Ottimizzato per operazioni di sola lettura
    public GameResponseDTO getGameByUuid(UUID uuid) {
        Game game = gameRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found with UUID: " + uuid));
        return new GameResponseDTO(game);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GameResponseDTO> getAllGames() {
        return gameRepository.findAll().stream()
                .map(GameResponseDTO::new) // Mappa ogni entità Game a un GameResponseDTO
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GameResponseDTO updateGame(UUID uuid, GameRequestDTO gameRequestDTO) {
        Game existingGame = gameRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found with UUID: " + uuid));

        // Aggiorna i campi dell'entità esistente con i dati dal DTO di richiesta
        existingGame.setTitle(gameRequestDTO.getTitle());
        existingGame.setGenre(gameRequestDTO.getGenre());
        existingGame.setPrice(gameRequestDTO.getPrice());
        existingGame.setReleaseDate(gameRequestDTO.getReleaseDate());
        existingGame.setDeveloper(gameRequestDTO.getDeveloper());
        existingGame.setPublisher(gameRequestDTO.getPublisher());

        Game updatedGame = gameRepository.save(existingGame); // Salva le modifiche
        return new GameResponseDTO(updatedGame);
    }

    @Override
    @Transactional
    public void deleteGame(UUID uuid) {
        if (!gameRepository.existsByUuid(uuid)) { // Verifica prima se il gioco esiste
            throw new ResourceNotFoundException("Game not found with UUID: " + uuid);
        }
        gameRepository.deleteByUuid(uuid); // Elimina il gioco
    }
}