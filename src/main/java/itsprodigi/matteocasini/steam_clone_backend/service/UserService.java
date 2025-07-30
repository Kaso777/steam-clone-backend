package itsprodigi.matteocasini.steam_clone_backend.service;

import itsprodigi.matteocasini.steam_clone_backend.dto.UserRequestDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserResponseDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Servizio per la gestione degli utenti.
 * Definisce le operazioni disponibili per interagire con gli utenti.
 */
public interface UserService {

    /**
     * Registra un nuovo utente.
     *
     * @param userRequestDTO Dati dell'utente da registrare.
     * @return L'utente registrato.
     */
    UserResponseDTO registerUser(UserRequestDTO userRequestDTO);

    /**
     * Recupera un utente tramite ID.
     *
     * @param id ID dell'utente.
     * @return L'utente, se trovato.
     */
    Optional<UserResponseDTO> getUserById(UUID id);

    /**
     * Recupera tutti gli utenti registrati.
     *
     * @return Lista di utenti.
     */
    List<UserResponseDTO> getAllUsers();

    /**
     * Aggiorna i dati di un utente esistente.
     *
     * @param id              ID dell'utente da aggiornare.
     * @param userRequestDTO  Nuovi dati dell'utente.
     * @return L'utente aggiornato.
     */
    UserResponseDTO updateUser(UUID id, UserRequestDTO userRequestDTO);

    /**
     * Elimina un utente tramite ID.
     *
     * @param id ID dell'utente da eliminare.
     */
    void deleteUser(UUID id);
}