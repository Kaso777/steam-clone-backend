package itsprodigi.matteocasini.steam_clone_backend.service;


import itsprodigi.matteocasini.steam_clone_backend.dto.UserProfileRequestDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserProfileResponseDTO;
import java.util.Optional;
import java.util.UUID;

/**
 * Interfaccia per il servizio di gestione dei profili utente.
 * Definisce il contratto dei metodi disponibili per interagire con i profili utente,
 * garantendo il disaccoppiamento tra l'implementazione concreta e i componenti che la utilizzano.
 */
public interface UserProfileService {

    /**
     * Recupera un profilo utente tramite l'ID dell'utente (che è anche l'ID del profilo).
     * @param userId L'ID dell'utente/profilo da recuperare.
     * @return Un Optional contenente il UserProfileResponseDTO se trovato, altrimenti Optional.empty().
     */
    Optional<UserProfileResponseDTO> getUserProfileById(UUID userId);

    /**
     * Crea un nuovo profilo utente o aggiorna un profilo esistente per un dato utente.
     * @param userId L'ID dell'utente a cui associare/aggiornare il profilo.
     * @param profileDetailsRequestDTO Il DTO contenente i dati del profilo inviati dal client.
     * @return Il UserProfileResponseDTO del profilo salvato o aggiornato.
     */
    UserProfileResponseDTO createOrUpdateUserProfile(UUID userId, UserProfileRequestDTO profileDetailsRequestDTO);

    /**
     * Elimina un profilo utente per un dato ID utente.
     * @param userId L'ID dell'utente/profilo da eliminare.
     */
    void deleteUserProfile(UUID userId);
}