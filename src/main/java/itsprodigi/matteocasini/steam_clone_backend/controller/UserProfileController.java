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
 * Controller REST dedicato alla gestione dei profili utente.
 * Separato dal controller utente principale per garantire chiarezza e separazione delle responsabilità.
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
     * Recupera il profilo dell'utente specificato.
     * GET /api/users/{userId}/profile
     */
    @GetMapping
    public ResponseEntity<UserProfileResponseDTO> getUserProfile(@PathVariable UUID userId) {
        return userProfileService.getUserProfileById(userId)
                .map(profileDto -> new ResponseEntity<>(profileDto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Crea o aggiorna il profilo dell'utente.
     * PUT /api/users/{userId}/profile
     */
    @PutMapping
    public ResponseEntity<UserProfileResponseDTO> createOrUpdateUserProfile(
            @PathVariable UUID userId,
            @Valid @RequestBody UserProfileRequestDTO profileDetailsRequestDTO) {
        UserProfileResponseDTO updatedProfile = userProfileService.createOrUpdateUserProfile(userId, profileDetailsRequestDTO);
        return new ResponseEntity<>(updatedProfile, HttpStatus.OK);
    }

    /**
     * Elimina il profilo dell'utente.
     * DELETE /api/users/{userId}/profile
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteUserProfile(@PathVariable UUID userId) {
        userProfileService.deleteUserProfile(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
