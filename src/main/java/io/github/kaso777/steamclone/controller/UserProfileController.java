package io.github.kaso777.steamclone.controller;

import io.github.kaso777.steamclone.service.UserProfileService;
import io.github.kaso777.steamclone.dto.UserProfileRequestDTO;
import io.github.kaso777.steamclone.dto.UserProfileResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

import io.github.kaso777.steamclone.model.User;
import io.github.kaso777.steamclone.enums.Role;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller REST per la gestione dei profili utente.
 * Separato dal controller utente principale per chiarezza architetturale.
 */
@RestController
@RequestMapping("/api/users/{userId}/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping
    public ResponseEntity<UserProfileResponseDTO> getUserProfile(@PathVariable UUID userId) {
        UserProfileResponseDTO profile = userProfileService.getUserProfileById(userId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping
    public ResponseEntity<UserProfileResponseDTO> createOrUpdateUserProfile(
            @PathVariable UUID userId,
            @Valid @RequestBody UserProfileRequestDTO profileDetailsRequestDTO) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();

        // Consente la modifica solo all'utente stesso o ad un admin
        if (!currentUser.getId().equals(userId) && !currentUser.getRole().equals(Role.ROLE_ADMIN)) {
            throw new AccessDeniedException("Non autorizzato a modificare questo profilo");
        }

        UserProfileResponseDTO updatedProfile = userProfileService.createOrUpdateUserProfile(userId, profileDetailsRequestDTO);
        return ResponseEntity.ok(updatedProfile);
    }

    // Metodo non utilizzato: la cancellazione del profilo e gestita dal controller utente.
    /*
    @DeleteMapping
    public ResponseEntity<Void> deleteUserProfile(@PathVariable UUID userId) {
        userProfileService.deleteUserProfile(userId);
        return ResponseEntity.noContent().build();
    }
    */
}