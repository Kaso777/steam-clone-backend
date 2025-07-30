package itsprodigi.matteocasini.steam_clone_backend.service;

import itsprodigi.matteocasini.steam_clone_backend.dto.UserProfileRequestDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserProfileResponseDTO;
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

    @Override
    public UserProfileResponseDTO getUserProfileById(UUID userId) {
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new UserProfileNotFoundException(userId));
        return convertToResponseDto(profile);
    }

    @Override
    @Transactional
    public UserProfileResponseDTO createOrUpdateUserProfile(UUID userId, UserProfileRequestDTO profileDetailsRequestDTO) {
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

        userProfile.setNickname(profileDetailsRequestDTO.getNickname());
        userProfile.setAvatarUrl(profileDetailsRequestDTO.getAvatarUrl());
        userProfile.setBio(profileDetailsRequestDTO.getBio());

        UserProfile savedProfile = userProfileRepository.save(userProfile);
        return convertToResponseDto(savedProfile);
    }

    @Override
    @Transactional
    public void deleteUserProfile(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        userProfileRepository.findById(userId)
                .orElseThrow(() -> new UserProfileNotFoundException(userId));

        user.setUserProfile(null); // orphanRemoval = true gestisce la delete
        userRepository.save(user);
    }

    // --- Metodi di mapping (Entity -> DTO) ---

    private UserProfileResponseDTO convertToResponseDto(UserProfile userProfile) {
        UserProfileResponseDTO dto = new UserProfileResponseDTO();
        dto.setUserId(userProfile.getId());
        dto.setNickname(userProfile.getNickname());
        dto.setAvatarUrl(userProfile.getAvatarUrl());
        dto.setBio(userProfile.getBio());
        return dto;
    }
}