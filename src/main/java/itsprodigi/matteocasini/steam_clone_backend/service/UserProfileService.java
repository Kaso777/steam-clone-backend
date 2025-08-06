package itsprodigi.matteocasini.steam_clone_backend.service;

import itsprodigi.matteocasini.steam_clone_backend.dto.UserProfileRequestDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserProfileResponseDTO;

import java.util.UUID;

/**
 * Interfaccia per il servizio di gestione dei profili utente.
 */
public interface UserProfileService {

    /**
     * Recupera un profilo utente tramite l'ID dell'utente/profilo.
     */
    UserProfileResponseDTO getUserProfileById(UUID userId);

    /**
     * Crea o aggiorna un profilo utente associato a un dato ID utente.
     * Se il profilo non esiste viene creato, altrimenti aggiornato.
     */
    UserProfileResponseDTO createOrUpdateUserProfile(UUID userId, UserProfileRequestDTO profileDetailsRequestDTO);

    /**
     * Elimina un profilo utente dato l'ID utente.
     */
    void deleteUserProfile(UUID userId);
}