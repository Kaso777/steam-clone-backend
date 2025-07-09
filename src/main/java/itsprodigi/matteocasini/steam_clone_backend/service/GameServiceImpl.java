package itsprodigi.matteocasini.steam_clone_backend.service;

import itsprodigi.matteocasini.steam_clone_backend.dto.GameRequestDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.GameResponseDTO;
import itsprodigi.matteocasini.steam_clone_backend.exception.ResourceNotFoundException;
import itsprodigi.matteocasini.steam_clone_backend.model.Game;
import itsprodigi.matteocasini.steam_clone_backend.repository.GameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Gestisce le transazioni del database

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors; // Per facilitare le operazioni su collezioni (es. liste)

@Service // Marca la classe come un Service di Spring
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository; // Iniezione della dipendenza del GameRepository

    // Costruttore per l'iniezione delle dipendenze
    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    /**
     * Crea un nuovo gioco nel sistema.
     * @param gameRequestDTO DTO contenente i dati del nuovo gioco.
     * @return GameResponseDTO con i dati del gioco creato e il suo UUID.
     */
    @Override
    @Transactional
    public GameResponseDTO createGame(GameRequestDTO gameRequestDTO) {
        Game game = new Game(); // Crea una nuova istanza dell'entità Game
        game.setTitle(gameRequestDTO.getTitle());
        game.setPrice(gameRequestDTO.getPrice());
        game.setReleaseDate(gameRequestDTO.getReleaseDate());
        game.setDeveloper(gameRequestDTO.getDeveloper());
        game.setPublisher(gameRequestDTO.getPublisher());

        // Salva il gioco nel database. Il metodo @PrePersist in Game.java genererà l'UUID per 'id'.
        Game savedGame = gameRepository.save(game);
        // Converte l'entità salvata in un DTO di risposta.
        return new GameResponseDTO(savedGame);
    }

    /**
     * Recupera un gioco tramite il suo UUID.
     * @param uuid L'UUID del gioco da recuperare.
     * @return GameResponseDTO con i dati del gioco.
     * @throws ResourceNotFoundException Se il gioco con l'UUID specificato non viene trovato.
     */
    @Override
    @Transactional(readOnly = true)
    public GameResponseDTO getGameByUuid(UUID uuid) {
        // CAMBIAMENTO CHIAVE: Utilizza findById del GameRepository.
        // Poiché ora UUID è l'ID primario dell'entità Game, findById funziona direttamente.
        Game game = gameRepository.findById(uuid)
                // Se il gioco non viene trovato, lancia un'eccezione ResourceNotFoundException.
                .orElseThrow(() -> new ResourceNotFoundException("Game not found with UUID: " + uuid));
        // Converte l'entità trovata in un DTO di risposta.
        return new GameResponseDTO(game);
    }

    /**
     * Recupera tutti i giochi presenti nel sistema.
     * @return Lista di GameResponseDTO di tutti i giochi.
     */
    @Override
    @Transactional(readOnly = true)
    public List<GameResponseDTO> getAllGames() {
        // Recupera tutti i giochi dal database
        return gameRepository.findAll().stream()
                // Mappa ogni entità Game in un GameResponseDTO
                .map(GameResponseDTO::new)
                // Raccoglie i DTO in una lista
                .collect(Collectors.toList());
    }

    /**
     * Aggiorna i dati di un gioco esistente.
     * @param uuid L'UUID del gioco da aggiornare.
     * @param gameRequestDTO DTO contenente i nuovi dati del gioco.
     * @return GameResponseDTO con i dati del gioco aggiornato.
     * @throws ResourceNotFoundException Se il gioco con l'UUID specificato non viene trovato.
     */
    @Override
    @Transactional
    public GameResponseDTO updateGame(UUID uuid, GameRequestDTO gameRequestDTO) {
        // CAMBIAMENTO CHIAVE: Recupera il gioco esistente tramite findById
        Game existingGame = gameRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found with UUID: " + uuid));

        // Aggiorna i campi del gioco esistente con i nuovi dati dal DTO
        existingGame.setTitle(gameRequestDTO.getTitle());
        existingGame.setPrice(gameRequestDTO.getPrice());
        existingGame.setReleaseDate(gameRequestDTO.getReleaseDate());
        existingGame.setDeveloper(gameRequestDTO.getDeveloper());
        existingGame.setPublisher(gameRequestDTO.getPublisher());

        Game updatedGame = gameRepository.save(existingGame); // Salva le modifiche nel database
        return new GameResponseDTO(updatedGame); // Restituisce il gioco aggiornato come DTO
    }

    /**
     * Elimina un gioco tramite il suo UUID.
     * @param uuid L'UUID del gioco da eliminare.
     * @throws ResourceNotFoundException Se il gioco con l'UUID specificato non viene trovato.
     */
    @Override
    @Transactional
    public void deleteGame(UUID uuid) {
        // CAMBIAMENTO CHIAVE: Prima di eliminare, controlla se il gioco esiste con existsById.
        if (!gameRepository.existsById(uuid)) {
            throw new ResourceNotFoundException("Game not found with UUID: " + uuid);
        }
        // Elimina il gioco dal database tramite il suo UUID.
        gameRepository.deleteById(uuid);
    }

    /**
     * Trova giochi per titolo (ricerca case-insensitive contenente).
     * @param title Il titolo o parte del titolo da cercare.
     * @return Lista di GameResponseDTO dei giochi corrispondenti.
     */
    @Override
    @Transactional(readOnly = true)
    public List<GameResponseDTO> findGamesByTitle(String title) {
        return gameRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(GameResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Trova giochi per genere (ricerca case-insensitive esatta).
     * @param genre Il genere da cercare.
     * @return Lista di GameResponseDTO dei giochi corrispondenti.
     */
    @Override
    @Transactional(readOnly = true)
    public List<GameResponseDTO> findGamesByGenre(String genre) {
        return gameRepository.findByGenreIgnoreCase(genre).stream()
                .map(GameResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Trova giochi per sviluppatore (ricerca case-insensitive contenente).
     * @param developer Lo sviluppatore o parte del nome dello sviluppatore da cercare.
     * @return Lista di GameResponseDTO dei giochi corrispondenti.
     */
    @Override
    @Transactional(readOnly = true)
    public List<GameResponseDTO> findGamesByDeveloper(String developer) {
        return gameRepository.findByDeveloperContainingIgnoreCase(developer).stream()
                .map(GameResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Trova giochi per publisher (ricerca case-insensitive contenente).
     * @param publisher Il publisher o parte del nome del publisher da cercare.
     * @return Lista di GameResponseDTO dei giochi corrispondenti.
     */
    @Override
    @Transactional(readOnly = true)
    public List<GameResponseDTO> findGamesByPublisher(String publisher) {
        return gameRepository.findByPublisherContainingIgnoreCase(publisher).stream()
                .map(GameResponseDTO::new)
                .collect(Collectors.toList());
    }
}