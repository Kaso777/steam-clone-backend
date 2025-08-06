package itsprodigi.matteocasini.steam_clone_backend.service;

import itsprodigi.matteocasini.steam_clone_backend.dto.UserProfileRequestDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserProfileResponseDTO;
import itsprodigi.matteocasini.steam_clone_backend.exception.InvalidUserProfileDataException;
import itsprodigi.matteocasini.steam_clone_backend.exception.UserNotFoundException;
import itsprodigi.matteocasini.steam_clone_backend.exception.UserProfileNotFoundException;
import itsprodigi.matteocasini.steam_clone_backend.model.User;
import itsprodigi.matteocasini.steam_clone_backend.model.UserProfile;
import itsprodigi.matteocasini.steam_clone_backend.repository.UserProfileRepository;
import itsprodigi.matteocasini.steam_clone_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementazione concreta dell'interfaccia UserProfileService.
 * Contiene la logica di business effettiva per la gestione dei profili utente.
 */
@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserProfileServiceImpl(UserProfileRepository userProfileRepository, UserRepository userRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
    }

    /**
     * Recupera un profilo utente tramite l'ID utente/profilo.
     */
    @Override
    public UserProfileResponseDTO getUserProfileById(UUID userId) {
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new UserProfileNotFoundException(userId));

        return new UserProfileResponseDTO(
                profile.getId(),
                profile.getNickname(),
                profile.getAvatarUrl(),
                profile.getBio());
    }

    /**
     * Crea un nuovo profilo o aggiorna un profilo esistente associato a un utente.
     * Validazioni di base sui dati (es. nickname non vuoto).
     */
    @Override
    @Transactional
    public UserProfileResponseDTO createOrUpdateUserProfile(UUID userId,
            UserProfileRequestDTO profileDetailsRequestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        UserProfile userProfile = userProfileRepository.findById(userId).orElseGet(() -> {
            UserProfile newProfile = new UserProfile();
            newProfile.setId(userId);
            newProfile.setUser(user);
            user.setUserProfile(newProfile);
            userRepository.save(user);
            return newProfile;
        });

        if (profileDetailsRequestDTO.getNickname().isPresent()) {
            String nickname = profileDetailsRequestDTO.getNickname().get();
            if (nickname.isBlank()) {
                throw new InvalidUserProfileDataException("Il nickname non può essere vuoto o solo spazi.");
            }
            userProfile.setNickname(nickname);
        }

        if (profileDetailsRequestDTO.getAvatarUrl().isPresent()) {
            userProfile.setAvatarUrl(profileDetailsRequestDTO.getAvatarUrl().get());
        }

        if (profileDetailsRequestDTO.getBio().isPresent()) {
            userProfile.setBio(profileDetailsRequestDTO.getBio().get());
        }

        UserProfile savedProfile = userProfileRepository.save(userProfile);
        return convertToResponseDto(savedProfile);
    }

    /**
     * Elimina un profilo utente dato l'ID.
     * La cancellazione avviene rimuovendo il riferimento dall'entità User.
     */
    @Override
    @Transactional
    public void deleteUserProfile(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        userProfileRepository.findById(userId)
                .orElseThrow(() -> new UserProfileNotFoundException(userId));

        user.setUserProfile(null); // orphanRemoval=true si occupa di eliminare il profilo
        userRepository.save(user);
    }

    /**
     * Mappa un'entità UserProfile in un DTO di risposta.
     */
    private UserProfileResponseDTO convertToResponseDto(UserProfile userProfile) {
        UserProfileResponseDTO dto = new UserProfileResponseDTO();
        dto.setUserId(userProfile.getId());
        dto.setNickname(userProfile.getNickname());
        dto.setAvatarUrl(userProfile.getAvatarUrl());
        dto.setBio(userProfile.getBio());
        return dto;
    }
}
