package itsprodigi.matteocasini.steam_clone_backend.service;

import itsprodigi.matteocasini.steam_clone_backend.dto.GameRequestDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.GameResponseDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.GameUpdateDTO;
import itsprodigi.matteocasini.steam_clone_backend.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Servizio per la gestione dei giochi.
 * Definisce operazioni CRUD e ricerche specifiche per titolo, tag, sviluppatore
 * ed editore.
 */
public interface GameService {

    /**
     * Crea un nuovo gioco.
     *
     * @param gameRequestDTO dati per la creazione del gioco
     * @return gioco creato
     */
    GameResponseDTO createGame(GameRequestDTO gameRequestDTO);

    /**
     * Recupera un gioco tramite il suo ID.
     *
     * @param id ID del gioco
     * @return gioco se esistente
     * @throws ResourceNotFoundException se non trovato
     */
    Optional<GameResponseDTO> getGameById(UUID id);

    /**
     * Recupera tutti i giochi presenti.
     *
     * @return lista di giochi
     */
    List<GameResponseDTO> getAllGames();

    /**
     * Aggiorna un gioco esistente.
     *
     * @param id            ID del gioco
     * @param gameUpdateDTO nuovi dati
     * @return gioco aggiornato
     * @throws ResourceNotFoundException se non trovato
     */
    GameResponseDTO updateGame(UUID id, GameUpdateDTO gameUpdateDTO);

    /**
     * Elimina un gioco esistente.
     *
     * @param id ID del gioco
     * @throws ResourceNotFoundException se non trovato
     */
    void deleteGame(UUID id);

    /**
     * Cerca giochi contenenti una parte del titolo (case-insensitive).
     *
     * @param title stringa da cercare
     * @return lista di giochi trovati
     */
    List<GameResponseDTO> findGamesByTitle(String title);

    /**
     * Trova giochi associati a un tag.
     *
     * @param tagName nome del tag
     * @return lista di giochi
     */
    List<GameResponseDTO> findGamesByTagName(String tagName);

    /**
     * Trova giochi sviluppati da uno specifico sviluppatore.
     *
     * @param developer nome dello sviluppatore
     * @return lista di giochi
     */
    List<GameResponseDTO> findGamesByDeveloper(String developer);

    /**
     * Trova giochi pubblicati da un editore specifico.
     *
     * @param publisher nome dell'editore
     * @return lista di giochi
     */
    List<GameResponseDTO> findGamesByPublisher(String publisher);
}