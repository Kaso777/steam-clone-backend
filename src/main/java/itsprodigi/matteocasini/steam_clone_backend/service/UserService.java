package itsprodigi.matteocasini.steam_clone_backend.service;

import itsprodigi.matteocasini.steam_clone_backend.dto.UserRegistrationDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserResponseDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserUpdateDTO;
import itsprodigi.matteocasini.steam_clone_backend.model.User;


import java.util.List;
//import java.util.Optional;
import java.util.UUID;




/**
 * Servizio per la gestione degli utenti.
 * Definisce le operazioni disponibili per interagire con gli utenti.
 */
public interface UserService {

    User getAuthenticatedUser();


    /**
     * Registra un nuovo utente.
     */
    UserResponseDTO registerUser(UserRegistrationDTO userRegistrationDTO);

    /**
     * Recupera un utente tramite ID.
     *
     * @param id ID dell'utente.
     * @return L'utente, se trovato.
     */
    UserResponseDTO getUserById(UUID id);


    /**
     * Recupera tutti gli utenti registrati.
     *
     * @return Lista di utenti.
     */
    List<UserResponseDTO> getAllUsers();

    /**
     * Aggiorna i dati di un utente esistente.
     *
     * @param id ID dell'utente da aggiornare.
     * @param userUpdateDTO DTO contenente i nuovi dati dell'utente.
     */
    UserResponseDTO updateUser(UUID id, UserUpdateDTO userUpdateDTO);

    /**
     * Elimina un utente tramite ID.
     *
     * @param id ID dell'utente da eliminare.
     */
    void deleteUser(UUID id);
}