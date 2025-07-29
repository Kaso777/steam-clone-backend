package itsprodigi.matteocasini.steam_clone_backend.service;

import itsprodigi.matteocasini.steam_clone_backend.dto.GameRequestDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.GameResponseDTO;
import itsprodigi.matteocasini.steam_clone_backend.exception.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

/**
 * Interfaccia per il servizio di gestione dei Giochi.
 * Definisce il contratto dei metodi disponibili per interagire con i giochi,
 * garantendo il disaccoppiamento tra l'implementazione concreta e i componenti che la utilizzano.
 */
public interface GameService {

    GameResponseDTO createGame(GameRequestDTO gameRequestDTO);

    GameResponseDTO getGameById(UUID id) throws ResourceNotFoundException;

    List<GameResponseDTO> getAllGames();

    GameResponseDTO updateGame(UUID id, GameRequestDTO gameRequestDTO) throws ResourceNotFoundException;

    void deleteGame(UUID id) throws ResourceNotFoundException;

    List<GameResponseDTO> findGamesByTitle(String title);

    List<GameResponseDTO> findGamesByTagName(String tagName);

    List<GameResponseDTO> findGamesByDeveloper(String developer);

    List<GameResponseDTO> findGamesByPublisher(String publisher);

}
