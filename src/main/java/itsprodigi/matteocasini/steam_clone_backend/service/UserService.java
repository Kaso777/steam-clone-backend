package itsprodigi.matteocasini.steam_clone_backend.service;

import itsprodigi.matteocasini.steam_clone_backend.dto.UserRequestDTO;  // Importa il DTO di richiesta
import itsprodigi.matteocasini.steam_clone_backend.dto.UserResponseDTO; // Importa il DTO di risposta
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interfaccia per il servizio di gestione degli Utenti.
 * Definisce il contratto dei metodi disponibili per interagire con gli utenti,
 * garantendo il disaccoppiamento tra l'implementazione concreta e i componenti che la utilizzano.
 */
public interface UserService {

    /**
     * Registra un nuovo utente nel sistema.
     * Accetta un UserRequestDTO come input, che include username, email, password e role.
     * @param userRequestDTO Il DTO contenente i dati di registrazione dell'utente.
     * @return Il UserResponseDTO dell'utente appena registrato.
     */
    UserResponseDTO registerUser(UserRequestDTO userRequestDTO);

    /**
     * Recupera un utente tramite il suo ID.
     * Restituisce un Optional di UserResponseDTO.
     * @param id L'ID univoco (UUID) dell'utente da recuperare.
     * @return Un Optional contenente il UserResponseDTO se trovato, altrimenti Optional.empty().
     */
    Optional<UserResponseDTO> getUserById(UUID id);

    /**
     * Recupera tutti gli utenti registrati nel sistema.
     * Restituisce una lista di UserResponseDTO.
     * @return Una lista di UserResponseDTO.
     */
    List<UserResponseDTO> getAllUsers();

    /**
     * Aggiorna i dati di un utente esistente.
     * Accetta un UserRequestDTO come input, che include i dati aggiornati dell'utente.
     * @param id L'ID dell'utente da aggiornare.
     * @param userRequestDTO Il DTO contenente i nuovi dati dell'utente.
     * @return Il UserResponseDTO dell'utente aggiornato.
     */
    UserResponseDTO updateUser(UUID id, UserRequestDTO userRequestDTO);

    /**
     * Elimina un utente tramite il suo ID.
     * @param id L'ID dell'utente da eliminare.
     */
    void deleteUser(UUID id);
}