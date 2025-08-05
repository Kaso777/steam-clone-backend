package itsprodigi.matteocasini.steam_clone_backend.controller;

import itsprodigi.matteocasini.steam_clone_backend.service.UserProfileService;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserProfileRequestDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserProfileResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

import itsprodigi.matteocasini.steam_clone_backend.model.User;
import itsprodigi.matteocasini.steam_clone_backend.enums.Role;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller REST dedicato alla gestione dei profili utente.
 * Separato dal controller utente principale per garantire chiarezza e
 * separazione delle responsabilità.
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
        UserProfileResponseDTO profile = userProfileService.getUserProfileById(userId);
        return ResponseEntity.ok(profile);
    }

    /**
     * Crea o aggiorna il profilo dell'utente.
     * PUT /api/users/{userId}/profile
     */
    @PutMapping
    public ResponseEntity<UserProfileResponseDTO> createOrUpdateUserProfile(
            @PathVariable UUID userId,
            @Valid @RequestBody UserProfileRequestDTO profileDetailsRequestDTO) {
                 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    User currentUser = (User) auth.getPrincipal();

    // Controllo: o admin o stesso user
    if (!currentUser.getId().equals(userId) && !currentUser.getRole().equals(Role.ROLE_ADMIN)) {
        throw new AccessDeniedException("Non autorizzato a modificare questo profilo");
    }



        UserProfileResponseDTO updatedProfile = userProfileService.createOrUpdateUserProfile(userId,
                profileDetailsRequestDTO);
        return ResponseEntity.ok(updatedProfile);
    }

    /*
    Messo per completezza ma la delete viene gestita nel controller principale degli utenti
    @DeleteMapping
    public ResponseEntity<Void> deleteUserProfile(@PathVariable UUID userId) {
        userProfileService.deleteUserProfile(userId);
        return ResponseEntity.noContent().build();
    }
         */
}