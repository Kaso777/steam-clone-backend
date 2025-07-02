package itsprodigi.matteocasini.steam_clone_backend.service;

import itsprodigi.matteocasini.steam_clone_backend.dto.UserGameRequestDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserGameResponseDTO;
import itsprodigi.matteocasini.steam_clone_backend.model.UserGame;

import java.util.List;
import java.util.UUID;

public interface UserGameService {

    /**
     * Aggiunge un gioco alla libreria di un utente.
     * @param userGameRequestDTO DTO contenente gli UUID dell'utente e del gioco, più la data di acquisto.
     * @return DTO della relazione UserGame creata.
     * @throws itsprodigi.matteocasini.steam_clone_backend.exception.ResourceNotFoundException se utente o gioco non trovati.
     * @throws IllegalStateException se l'utente possiede già il gioco.
     */
    UserGameResponseDTO addGameToUserLibrary(UserGameRequestDTO userGameRequestDTO);

    /**
     * Recupera tutti i giochi nella libreria di un utente specifico.
     * @param userUuid L'UUID dell'utente di cui recuperare la libreria.
     * @return Una lista di DTO di UserGameResponseDTO che rappresentano i giochi posseduti dall'utente.
     * @throws itsprodigi.matteocasini.steam_clone_backend.exception.ResourceNotFoundException se l'utente non viene trovato.
     */
    List<UserGame> getUserLibrary(UUID userUuid);

    /**
     * Recupera una specifica entry della libreria (un utente che possiede un gioco specifico).
     * @param userUuid L'UUID dell'utente.
     * @param gameUuid L'UUID del gioco.
     * @return DTO della relazione UserGame.
     * @throws itsprodigi.matteocasini.steam_clone_backend.exception.ResourceNotFoundException se la relazione non viene trovata.
     */
    UserGameResponseDTO getUserGameEntry(UUID userUuid, UUID gameUuid);

    /**
     * Rimuove un gioco dalla libreria di un utente.
     * @param userUuid L'UUID dell'utente.
     * @param gameUuid L'UUID del gioco da rimuovere.
     * @throws itsprodigi.matteocasini.steam_clone_backend.exception.ResourceNotFoundException se la relazione non viene trovata.
     */
    void removeGameFromUserLibrary(UUID userUuid, UUID gameUuid);
} 