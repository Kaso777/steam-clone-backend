package io.github.kaso777.steamclone.service;

import io.github.kaso777.steamclone.dto.UserResponseDTO;
import io.github.kaso777.steamclone.dto.UserUpdateDTO;
import io.github.kaso777.steamclone.dto.auth.RegisterRequest;
import io.github.kaso777.steamclone.model.User;

import java.util.List;
import java.util.UUID;

/**
 * Servizio per la gestione degli utenti.
 * Definisce le operazioni disponibili per interagire con gli utenti.
 */
public interface UserService {

    /**
     * Recupera l'utente attualmente autenticato.
     */
    User getAuthenticatedUser();

    /**
     * Registra un nuovo utente.
     */
    UserResponseDTO registerUser(RegisterRequest registerRequest);

    /**
     * Recupera un utente tramite ID.
     */
    UserResponseDTO getUserById(UUID id);

    /**
     * Recupera tutti gli utenti registrati.
     */
    List<UserResponseDTO> getAllUsers();

    /**
     * Aggiorna i dati di un utente esistente.
     */
    UserResponseDTO updateUser(UUID id, UserUpdateDTO userUpdateDTO);

    /**
     * Elimina un utente tramite ID.
     */
    void deleteUser(UUID id);
}
