package itsprodigi.matteocasini.steam_clone_backend.service;

import itsprodigi.matteocasini.steam_clone_backend.model.Game;
import itsprodigi.matteocasini.steam_clone_backend.model.Tag;
import itsprodigi.matteocasini.steam_clone_backend.repository.GameRepository;
import itsprodigi.matteocasini.steam_clone_backend.repository.TagRepository;
import itsprodigi.matteocasini.steam_clone_backend.dto.GameRequestDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.GameResponseDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.TagDTO;
import itsprodigi.matteocasini.steam_clone_backend.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementazione dell'interfaccia GameService.
 * Gestisce la logica di business relativa ai giochi e alle associazioni con i tag.
 */
@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final TagRepository tagRepository;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository, TagRepository tagRepository) {
        this.gameRepository = gameRepository;
        this.tagRepository = tagRepository;
    }

    /**
     * Crea un nuovo gioco e lo salva nel database.
     * Se il titolo è già presente, solleva un'eccezione.
     */
    @Override
    @Transactional
    public GameResponseDTO createGame(GameRequestDTO gameRequestDTO) {
        if (gameRepository.findByTitle(gameRequestDTO.getTitle()).isPresent()) {
            throw new RuntimeException("Un gioco con il titolo '" + gameRequestDTO.getTitle() + "' esiste già.");
        }

        Game game = new Game();
        game.setTitle(gameRequestDTO.getTitle());
        game.setPrice(gameRequestDTO.getPrice());
        game.setReleaseDate(gameRequestDTO.getReleaseDate());
        game.setDeveloper(gameRequestDTO.getDeveloper());
        game.setPublisher(gameRequestDTO.getPublisher());

        // Associa i tag esistenti o ne crea di nuovi
        if (gameRequestDTO.getTagNames() != null && !gameRequestDTO.getTagNames().isEmpty()) {
            Set<Tag> tags = new HashSet<>();
            for (String tagName : gameRequestDTO.getTagNames()) {
                Tag tag = tagRepository.findByNameIgnoreCase(tagName)
                        .orElseGet(() -> tagRepository.save(new Tag(tagName)));
                tags.add(tag);
            }
            game.setTags(tags);
        }

        Game savedGame = gameRepository.save(game);
        return convertToResponseDto(savedGame);
    }

    /**
     * Recupera un gioco per ID e lo converte in DTO.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<GameResponseDTO> getGameById(UUID id) {
        return gameRepository.findByIdWithTags(id)
                .map(this::convertToResponseDto);
    }

    /**
     * Restituisce la lista completa dei giochi.
     */
    @Override
    @Transactional(readOnly = true)
    public List<GameResponseDTO> getAllGames() {
        return gameRepository.findAllWithTags().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Aggiorna un gioco esistente con i dati forniti.
     * Se un altro gioco ha lo stesso titolo, solleva un'eccezione.
     */
    @Override
    @Transactional
    public GameResponseDTO updateGame(UUID id, GameRequestDTO gameRequestDTO) {
        Game game = gameRepository.findByIdWithTags(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gioco non trovato con ID: " + id));

        gameRepository.findByTitle(gameRequestDTO.getTitle())
                .ifPresent(existingGame -> {
                    if (!existingGame.getId().equals(id)) {
                        throw new RuntimeException("Un altro gioco con il titolo '" + gameRequestDTO.getTitle() + "' esiste già.");
                    }
                });

        game.setTitle(gameRequestDTO.getTitle());
        game.setPrice(gameRequestDTO.getPrice());
        game.setReleaseDate(gameRequestDTO.getReleaseDate());
        game.setDeveloper(gameRequestDTO.getDeveloper());
        game.setPublisher(gameRequestDTO.getPublisher());

        // Aggiorna i tag
        game.getTags().clear();
        if (gameRequestDTO.getTagNames() != null && !gameRequestDTO.getTagNames().isEmpty()) {
            for (String tagName : gameRequestDTO.getTagNames()) {
                Tag tag = tagRepository.findByNameIgnoreCase(tagName)
                        .orElseGet(() -> tagRepository.save(new Tag(tagName)));
                game.addTag(tag);
            }
        }

        Game updatedGame = gameRepository.save(game);
        return convertToResponseDto(updatedGame);
    }

    /**
     * Elimina un gioco tramite ID.
     */
    @Override
    @Transactional
    public void deleteGame(UUID id) {
        if (!gameRepository.existsById(id)) {
            throw new ResourceNotFoundException("Gioco non trovato con ID: " + id);
        }
        gameRepository.deleteById(id);
    }

    /**
     * Cerca giochi il cui titolo contiene una certa stringa (case-insensitive).
     */
    @Override
    @Transactional(readOnly = true)
    public List<GameResponseDTO> findGamesByTitle(String title) {
        return gameRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Cerca giochi associati a un determinato tag.
     */
    @Override
    @Transactional(readOnly = true)
    public List<GameResponseDTO> findGamesByTagName(String tagName) {
        return gameRepository.findByTags_NameIgnoreCase(tagName).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Cerca giochi per sviluppatore (case-insensitive).
     */
    @Override
    @Transactional(readOnly = true)
    public List<GameResponseDTO> findGamesByDeveloper(String developer) {
        return gameRepository.findByDeveloperIgnoreCase(developer).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Cerca giochi per editore (case-insensitive).
     */
    @Override
    @Transactional(readOnly = true)
    public List<GameResponseDTO> findGamesByPublisher(String publisher) {
        return gameRepository.findByPublisherIgnoreCase(publisher).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Converte un'entità Game in un DTO di risposta.
     */
    private GameResponseDTO convertToResponseDto(Game game) {
        List<TagDTO> tagDtos = game.getTags().stream()
                .map(tag -> new TagDTO(tag.getId(), tag.getName()))
                .collect(Collectors.toList());

        return new GameResponseDTO(
                game.getId(),
                game.getTitle(),
                game.getPrice(),
                game.getReleaseDate(),
                game.getDeveloper(),
                game.getPublisher(),
                tagDtos
        );
    }
}
