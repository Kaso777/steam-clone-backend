// Si potrebbe evitare di utilizzare un controller seprato per UserProfile ed integrare gli endpoint nel UserController, ma per
// chiarezza e separazione delle responsabilità preferisco mantenere un controller dedicato per il profilo utente.

package itsprodigi.matteocasini.steam_clone_backend.controller;


import itsprodigi.matteocasini.steam_clone_backend.service.UserProfileService;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserProfileRequestDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserProfileResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller REST per la gestione delle operazioni sui profili utente.
 * Espone gli endpoint API per creare, leggere, aggiornare ed eliminare i profili utente.
 * L'accesso a un profilo è sempre legato all'ID dell'utente, che è parte del percorso URL.
 */
@RestController
@RequestMapping("/api/users/{userId}/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    /**
     * Endpoint GET per recuperare il profilo di un utente specifico.
     * Esempio di URL: GET /api/users/a1b2c3d4-e5f6-7890-1234-567890abcdef/profile
     *
     * @param userId L'ID univoco (UUID) dell'utente, estratto dal percorso URL.
     * @return ResponseEntity contenente il UserProfileResponseDTO e lo stato HTTP (200 OK se trovato, 404 NOT FOUND altrimenti).
     */
    @GetMapping
    public ResponseEntity<UserProfileResponseDTO> getUserProfile(@PathVariable UUID userId) {
        return userProfileService.getUserProfileById(userId)
                .map(profileDto -> new ResponseEntity<>(profileDto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint PUT per creare un nuovo profilo o aggiornare un profilo esistente per un utente.
     * Questo endpoint è idempotente: ripetute richieste con gli stessi dati avranno lo stesso effetto.
     * Esempio di URL: PUT /api/users/a1b2c3d4-e5f6-7890-1234-567890abcdef/profile
     *
     * @param userId L'ID univoco (UUID) dell'utente per cui si sta creando/aggiornando il profilo.
     * @param profileDetailsRequestDTO Il UserProfileRequestDTO inviato nel corpo della richiesta
     * con i dati del profilo (nickname, avatarUrl, bio).
     * @Valid: Questa annotazione di Bean Validation attiva la validazione dei campi
     * del `UserProfileRequestDTO` in base alle annotazioni definite al suo interno (@NotBlank, @Size).
     * Se la validazione fallisce, Spring MVC genererà automaticamente un errore 400 Bad Request.
     * @return ResponseEntity contenente il UserProfileResponseDTO del profilo salvato/aggiornato e lo stato HTTP (200 OK).
     * In caso di errore (es. utente non trovato), restituisce 404 NOT FOUND.
     */
    @PutMapping
    public ResponseEntity<UserProfileResponseDTO> createOrUpdateUserProfile(@PathVariable UUID userId, @Valid @RequestBody UserProfileRequestDTO profileDetailsRequestDTO) {
        try {
            UserProfileResponseDTO updatedProfile = userProfileService.createOrUpdateUserProfile(userId, profileDetailsRequestDTO);
            return new ResponseEntity<>(updatedProfile, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint DELETE per eliminare il profilo di un utente specifico.
     * Esempio di URL: DELETE /api/users/a1b2c3d4-e5f6-7890-1234-567890abcdef/profile
     *
     * @param userId L'ID univoco (UUID) dell'utente il cui profilo deve essere eliminato.
     * @return ResponseEntity con stato HTTP (204 NO CONTENT se l'eliminazione ha successo,
     * 404 NOT FOUND se l'utente/profilo non esiste).
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteUserProfile(@PathVariable UUID userId) {
        try {
            userProfileService.deleteUserProfile(userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}