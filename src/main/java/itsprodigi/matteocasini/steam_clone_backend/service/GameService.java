package itsprodigi.matteocasini.steam_clone_backend.service;

import itsprodigi.matteocasini.steam_clone_backend.dto.GameRequestDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.GameResponseDTO;

import java.util.List;
import java.util.UUID;

public interface GameService {

    /**
     * Crea un nuovo gioco nel sistema.
     * @param gameRequestDTO DTO contenente i dati del gioco da creare.
     * @return DTO del gioco creato.
     */
    GameResponseDTO createGame(GameRequestDTO gameRequestDTO);

    /**
     * Recupera un gioco tramite il suo UUID.
     * @param uuid L'UUID del gioco da recuperare.
     * @return DTO del gioco trovato.
     * @throws itsprodigi.matteocasini.steam_clone_backend.exception.ResourceNotFoundException se il gioco non viene trovato.
     */
    GameResponseDTO getGameByUuid(UUID uuid);

    /**
     * Recupera tutti i giochi presenti nel sistema.
     * @return Una lista di DTO di tutti i giochi.
     */
    List<GameResponseDTO> getAllGames();

    /**
     * Aggiorna i dati di un gioco esistente.
     * @param uuid L'UUID del gioco da aggiornare.
     * @param gameRequestDTO DTO contenente i nuovi dati del gioco.
     * @return DTO del gioco aggiornato.
     * @throws itsprodigi.matteocasini.steam_clone_backend.exception.ResourceNotFoundException se il gioco non viene trovato.
     */
    GameResponseDTO updateGame(UUID uuid, GameRequestDTO gameRequestDTO);

    /**
     * Elimina un gioco dal sistema tramite il suo UUID.
     * @param uuid L'UUID del gioco da eliminare.
     * @throws itsprodigi.matteocasini.steam_clone_backend.exception.ResourceNotFoundException se il gioco non viene trovato.
     */
    void deleteGame(UUID uuid);
}