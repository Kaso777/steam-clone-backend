package itsprodigi.matteocasini.steam_clone_backend.service;

import itsprodigi.matteocasini.steam_clone_backend.dto.GameRequestDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.GameResponseDTO;
import itsprodigi.matteocasini.steam_clone_backend.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interfaccia per il servizio di gestione dei giochi.
 * Definisce le operazioni disponibili per creare, leggere, aggiornare e cancellare giochi,
 * oltre a metodi di ricerca specifici basati su titolo, tag, sviluppatore o editore.
 */
public interface GameService {

    /**
     * Crea un nuovo gioco.
     * @param gameRequestDTO DTO contenente i dati del gioco e i nomi dei tag associati.
     * @return DTO del gioco creato.
     */
    GameResponseDTO createGame(GameRequestDTO gameRequestDTO);

    /**
     * Restituisce un gioco tramite il suo ID.
     * @param id UUID del gioco da cercare.
     * @return Optional contenente il gioco, se trovato.
     * @throws ResourceNotFoundException se il gioco non esiste.
     */
    Optional<GameResponseDTO> getGameById(UUID id);

    /**
     * Restituisce tutti i giochi presenti nel sistema.
     * @return Lista di DTO dei giochi.
     */
    List<GameResponseDTO> getAllGames();

    /**
     * Aggiorna un gioco esistente.
     * @param id UUID del gioco da aggiornare.
     * @param gameRequestDTO DTO contenente i nuovi dati del gioco.
     * @return DTO del gioco aggiornato.
     * @throws ResourceNotFoundException se il gioco non esiste.
     */
    GameResponseDTO updateGame(UUID id, GameRequestDTO gameRequestDTO);

    /**
     * Elimina un gioco tramite il suo ID.
     * @param id UUID del gioco da eliminare.
     * @throws ResourceNotFoundException se il gioco non esiste.
     */
    void deleteGame(UUID id);

    /**
     * Cerca giochi il cui titolo contiene una determinata stringa (case-insensitive).
     * @param title Titolo o parte del titolo da cercare.
     * @return Lista di giochi corrispondenti.
     */
    List<GameResponseDTO> findGamesByTitle(String title);

    /**
     * Restituisce i giochi associati a un tag specifico.
     * @param tagName Nome del tag.
     * @return Lista di giochi corrispondenti.
     */
    List<GameResponseDTO> findGamesByTagName(String tagName);

    /**
     * Restituisce i giochi sviluppati da uno specifico sviluppatore.
     * @param developer Nome dello sviluppatore.
     * @return Lista di giochi corrispondenti.
     */
    List<GameResponseDTO> findGamesByDeveloper(String developer);

    /**
     * Restituisce i giochi pubblicati da uno specifico editore.
     * @param publisher Nome dell'editore.
     * @return Lista di giochi corrispondenti.
     */
    List<GameResponseDTO> findGamesByPublisher(String publisher);
}
