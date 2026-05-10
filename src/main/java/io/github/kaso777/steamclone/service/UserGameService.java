package io.github.kaso777.steamclone.service;

import io.github.kaso777.steamclone.dto.UserGameRequestDTO;
import io.github.kaso777.steamclone.dto.UserGameResponseDTO;
import io.github.kaso777.steamclone.dto.UserLibraryResponseDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interfaccia per la gestione della libreria giochi degli utenti.
 * Definisce le operazioni CRUD sull'associazione utente-gioco.
 */

public interface UserGameService {

    /**
     * Aggiunge un gioco alla libreria di un utente.
     */
    UserGameResponseDTO addGameToUserLibrary(UUID userUuid, UserGameRequestDTO userGameRequestDTO);

    /**
     * Recupera un'associazione utente-gioco specifica.
     * Non attualmente utilizzato, ma potenzialmente utile.
     */
    Optional<UserGameResponseDTO> getUserGameByIds(UUID userUuid, UUID gameUuid);

    /**
     * Recupera la libreria di giochi di un determinato utente.
     */
    UserLibraryResponseDTO getUserLibrary(UUID userUuid);

    /**
     * Aggiorna le informazioni di un gioco nella libreria di un utente.
     */
    UserGameResponseDTO updateUserGame(UUID userUuid, UUID gameUuid, UserGameRequestDTO userGameRequestDTO);

    /**
     * Rimuove un gioco dalla libreria di un utente.
     */
    void removeGameFromUserLibrary(UUID userUuid, UUID gameUuid);

    /**
     * Recupera tutte le associazioni utente-gioco presenti nel sistema.
     * Non attualmente esposto come endpoint pubblico; utile per uso interno o per
     * amministratori.
     */
    List<UserGameResponseDTO> getAllUserGames();
}
