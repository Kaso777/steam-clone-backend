package itsprodigi.matteocasini.steam_clone_backend.service;


import itsprodigi.matteocasini.steam_clone_backend.model.User;
import itsprodigi.matteocasini.steam_clone_backend.model.UserProfile;
import itsprodigi.matteocasini.steam_clone_backend.repository.UserProfileRepository;
import itsprodigi.matteocasini.steam_clone_backend.repository.UserRepository;
import itsprodigi.matteocasini.steam_clone_backend.service.UserProfileService;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserProfileRequestDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserProfileResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementazione concreta dell'interfaccia UserProfileService.
 * Contiene la logica di business effettiva per la gestione dei profili utente.
 * Mappa le entità del database ai DTO per le interazioni con il controller e il client.
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
     * Implementazione del metodo per recuperare un profilo utente per ID.
     * Converte l'entità UserProfile trovata in un UserProfileResponseDTO.
     * @param userId L'ID dell'utente/profilo.
     * @return Un Optional contenente il UserProfileResponseDTO se il profilo esiste, altrimenti un Optional vuoto.
     */
    @Override
    public Optional<UserProfileResponseDTO> getUserProfileById(UUID userId) {
        return userProfileRepository.findById(userId)
                .map(this::convertToResponseDto);
    }

    /**
     * Implementazione del metodo per creare o aggiornare un profilo utente.
     * Questa logica gestisce sia la creazione di un nuovo profilo (se non esiste per l'utente specificato)
     * sia l'aggiornamento di un profilo esistente.
     * @param userId L'ID dell'utente a cui associare/aggiornare il profilo.
     * @param profileDetailsRequestDTO Il DTO contenente i dati del profilo inviati dal client.
     * @return Il UserProfileResponseDTO del profilo che è stato salvato o aggiornato.
     * @throws RuntimeException Se l'utente con l'ID specificato non viene trovato nel database.
     */
    @Override
    @Transactional
    public UserProfileResponseDTO createOrUpdateUserProfile(UUID userId, UserProfileRequestDTO profileDetailsRequestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + userId));

        UserProfile userProfile;
        Optional<UserProfile> existingProfile = userProfileRepository.findById(userId);

        if (existingProfile.isEmpty()) {
            // SCENARIO: Il profilo NON esiste per questo utente. Creiamo un nuovo UserProfile.
            userProfile = new UserProfile();
            userProfile.setId(userId);
            userProfile.setUser(user);

            user.setUserProfile(userProfile);
            userRepository.save(user);
        } else {
            // SCENARIO: Il profilo ESISTE già per questo utente. Recuperiamo l'entità esistente per aggiornarla.
            userProfile = existingProfile.get();
        }

        // Aggiorna i campi dell'entità UserProfile con i dati provenienti dal UserProfileRequestDTO.
        userProfile.setNickname(profileDetailsRequestDTO.getNickname());
        userProfile.setAvatarUrl(profileDetailsRequestDTO.getAvatarUrl());
        userProfile.setBio(profileDetailsRequestDTO.getBio());

        UserProfile savedProfile = userProfileRepository.save(userProfile);

        // Converte l'entità UserProfile salvata in un UserProfileResponseDTO e lo restituisce.
        return convertToResponseDto(savedProfile);
    }

    /**
     * Implementazione del metodo per eliminare un profilo utente.
     * L'eliminazione viene gestita disassociando il profilo dall'utente.
     * Grazie a `orphanRemoval=true` sulla relazione `OneToOne` in `User.java`,
     * l'entità `UserProfile` orfana verrà automaticamente eliminata dal database.
     * @param userId L'ID dell'utente/profilo da eliminare.
     * @throws RuntimeException Se l'utente o il profilo non vengono trovati.
     */
    @Override
    @Transactional
    public void deleteUserProfile(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + userId));

        userProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Profilo utente non trovato per l'utente con ID: " + userId));

        user.setUserProfile(null);
        userRepository.save(user);
    }

    // --- Metodi di Mappatura (Conversione tra Entità e DTO) ---

    /**
     * Metodo privato helper per convertire un'entità UserProfile (dal database)
     * in un UserProfileResponseDTO (per l'invio al client).
     * @param userProfile L'entità UserProfile da convertire.
     * @return Il UserProfileResponseDTO corrispondente.
     */
    private UserProfileResponseDTO convertToResponseDto(UserProfile userProfile) {
        // Creiamo una nuova istanza di UserProfileResponseDTO e popoliamo i suoi campi
        // utilizzando i getter dell'entità UserProfile.
        UserProfileResponseDTO dto = new UserProfileResponseDTO();
        dto.setUserId(userProfile.getId());
        dto.setNickname(userProfile.getNickname());
        dto.setAvatarUrl(userProfile.getAvatarUrl());
        dto.setBio(userProfile.getBio());
        return dto;
    }
}