package itsprodigi.matteocasini.steam_clone_backend.service;

import itsprodigi.matteocasini.steam_clone_backend.dto.GameRequestDTO;  // DTO per le richieste (input) relative a Game
import itsprodigi.matteocasini.steam_clone_backend.dto.GameResponseDTO; // DTO per la risposta (output) di un Game
import itsprodigi.matteocasini.steam_clone_backend.exception.ResourceNotFoundException; // Importa l'eccezione personalizzata

import java.util.List;
import java.util.UUID;
import java.util.Optional; // Aggiunto per getUserById per coerenza con altri servizi

/**
 * Interfaccia per il servizio di gestione dei Giochi.
 * Definisce il contratto dei metodi disponibili per interagire con i giochi,
 * garantendo il disaccoppiamento tra l'implementazione concreta e i componenti che la utilizzano.
 * Gestisce le operazioni CRUD e le ricerche specifiche per i giochi.
 */
public interface GameService {

    /**
     * Crea un nuovo gioco nel sistema.
     * Accetta un GameRequestDTO come input, che include i dati del gioco e i nomi dei tag associati.
     * @param gameRequestDTO DTO contenente i dati del gioco da creare.
     * @return DTO del gioco creato (GameResponseDTO).
     */
    GameResponseDTO createGame(GameRequestDTO gameRequestDTO);

    /**
     * Recupera un gioco tramite il suo ID univoco.
     * Rinominato da getGameByUuid a getGameById per coerenza con il nome del campo 'id'.
     * @param id L'ID (UUID) del gioco da recuperare.
     * @return Un Optional contenente il DTO del gioco trovato (GameResponseDTO).
     * @throws ResourceNotFoundException se il gioco non viene trovato.
     */
    Optional<GameResponseDTO> getGameById(UUID id);

    /**
     * Recupera tutti i giochi presenti nel sistema.
     * @return Una lista di DTO di tutti i giochi (List<GameResponseDTO>).
     */
    List<GameResponseDTO> getAllGames();

    /**
     * Aggiorna i dati di un gioco esistente.
     * Accetta un GameRequestDTO come input, che include i nuovi dati del gioco e i tag aggiornati.
     * @param id L'ID (UUID) del gioco da aggiornare.
     * @param gameRequestDTO DTO contenente i nuovi dati del gioco.
     * @return DTO del gioco aggiornato (GameResponseDTO).
     * @throws ResourceNotFoundException se il gioco non viene trovato.
     */
    GameResponseDTO updateGame(UUID id, GameRequestDTO gameRequestDTO);

    /**
     * Elimina un gioco dal sistema tramite il suo ID univoco.
     * @param id L'ID (UUID) del gioco da eliminare.
     * @throws ResourceNotFoundException se il gioco non viene trovato.
     */
    void deleteGame(UUID id);

    /**
     * Trova giochi il cui titolo contiene la stringa specificata (case-insensitive).
     * @param title Il titolo o parte del titolo da cercare.
     * @return Una lista di DTO dei giochi corrispondenti.
     */
    List<GameResponseDTO> findGamesByTitle(String title);

    /**
     * Trova giochi associati a un tag specifico (tramite il nome del tag).
     * Sostituisce findGamesByGenre, poiché i giochi sono ora associati a Tag.
     * @param tagName Il nome del tag da cercare.
     * @return Una lista di DTO dei giochi corrispondenti al tag.
     */
    List<GameResponseDTO> findGamesByTagName(String tagName);

    /**
     * Trova giochi sviluppati da uno specifico sviluppatore.
     * @param developer Il nome dello sviluppatore.
     * @return Una lista di DTO dei giochi corrispondenti.
     */
    List<GameResponseDTO> findGamesByDeveloper(String developer);

    /**
     * Trova giochi pubblicati da uno specifico editore.
     * @param publisher Il nome dell'editore.
     * @return Una lista di DTO dei giochi corrispondenti.
     */
    List<GameResponseDTO> findGamesByPublisher(String publisher);
}