package itsprodigi.matteocasini.steam_clone_backend.service;

import itsprodigi.matteocasini.steam_clone_backend.model.User; // Importa l'entità User
import itsprodigi.matteocasini.steam_clone_backend.repository.UserRepository; // Importa il Repository per User
import itsprodigi.matteocasini.steam_clone_backend.dto.UserRequestDTO; // Importa il DTO di richiesta per User
import itsprodigi.matteocasini.steam_clone_backend.dto.UserResponseDTO; // Importa il DTO di risposta per User

import org.springframework.beans.factory.annotation.Autowired; // Per l'iniezione delle dipendenze
import org.springframework.security.crypto.password.PasswordEncoder; // Per hashare le password in modo sicuro
import org.springframework.stereotype.Service; // Indica che questa è una classe di servizio Spring
import org.springframework.transaction.annotation.Transactional; // Per la gestione delle transazioni di database

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;


import java.util.List; // Per liste di oggetti
import java.util.Optional; // Per gestire la possibile assenza di un valore
import java.util.UUID; // Per gli ID univoci degli utenti
import java.util.stream.Collectors; // Per collezionare elementi da uno stream

import itsprodigi.matteocasini.steam_clone_backend.enums.Role;
import itsprodigi.matteocasini.steam_clone_backend.exception.ResourceNotFoundException; // Importa la ResourceNotFoundException

/**
 * Implementazione concreta dell'interfaccia UserService.
 * Questa classe contiene la logica di business effettiva per la gestione degli Utenti.
 * È responsabile di interagire con il UserRepository per accedere ai dati nel database
 * e di mappare le entità del database (User) ai Data Transfer Objects (DTO)
 * di richiesta (UserRequestDTO) e risposta (UserResponseDTO) per l'interazione con il livello Controller.
 */
@Service // Indica a Spring che questa classe è un componente di servizio e deve essere gestita dal suo IoC container.
public class UserServiceImpl implements UserService { // Implementa l'interfaccia UserService, rispettando il contratto definito.

    private final UserRepository userRepository; // Iniezione del Repository per le operazioni sul database relative agli utenti.
    private final PasswordEncoder passwordEncoder; // Iniezione del PasswordEncoder per hashare le password in modo sicuro.

    /**
     * Costruttore per l'iniezione delle dipendenze.
     * Spring inietta automaticamente le istanze di UserRepository e PasswordEncoder.
     * @param userRepository Il Repository per l'entità User.
     * @param passwordEncoder L'encoder per le password.
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Implementazione del metodo per registrare un nuovo utente nel sistema.
     * Questo metodo riceve un DTO di richiesta, converte i suoi dati in un'entità User,
     * hasha la password per sicurezza e salva la nuova entità nel database.
     * @param userRequestDTO Il Data Transfer Object contenente i dati di registrazione dell'utente
     * (username, email, password, role).
     * @return Il UserResponseDTO dell'utente appena registrato, contenente i dati esposti all'API.
     * @throws RuntimeException se lo username o l'email forniti sono già in uso da un altro utente.
     */
    @Override // Indica che questo metodo implementa un metodo definito nell'interfaccia UserService.
    @Transactional // Assicura che l'intera operazione (lettura e scrittura nel DB) sia atomica.
                   // Se un'eccezione viene lanciata, tutte le modifiche al database vengono annullate (rollback).
    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO) {
        // 1. Verifica l'unicità dello username e dell'email prima di procedere con la registrazione.
        //    Utilizziamo i metodi 'existsByUsername' e 'existsByEmail' del UserRepository.
        //    Questi metodi sono più efficienti di 'findBy...().isPresent()' quando si vuole solo
        //    verificare l'esistenza di un record, poiché il database può ottimizzare la query.
        if (userRepository.existsByUsername(userRequestDTO.getUsername())) {
            throw new RuntimeException("Nome utente '" + userRequestDTO.getUsername() + "' già in uso.");
        }
        if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
            throw new RuntimeException("Email '" + userRequestDTO.getEmail() + "' già in uso.");
        }
        if (userRequestDTO.getPassword() == null || userRequestDTO.getPassword().length() < 6) {
            throw new RuntimeException("La password deve avere almeno 6 caratteri");
        }


        // 2. Crea una nuova entità User e popola i suoi campi con i dati dal DTO di richiesta.
        User user = new User();
        user.setUsername(userRequestDTO.getUsername());
        user.setEmail(userRequestDTO.getEmail());
        // 3. Hashes la password fornita nel DTO prima di impostarla nell'entità User.
        //    È una best practice di sicurezza fondamentale: MAI salvare password in chiaro nel database.
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        // 4. Imposta il ruolo dell'utente, come specificato nel DTO di richiesta.
        try {
            // Converte la stringa del ruolo in un valore dell'enum Role
            user.setRole(Role.valueOf(userRequestDTO.getRole()));
        } catch (IllegalArgumentException e) {
            // Gestisce il caso in cui il ruolo fornito non è valido
            throw new RuntimeException("Ruolo non valido: " + userRequestDTO.getRole() + ". Ruoli consentiti: " +
                    java.util.Arrays.toString(Role.values()));
        }

        // 5. Salva la nuova entità User nel database tramite il UserRepository.
        User savedUser = userRepository.save(user);

        // 6. Converte l'entità User appena salvata in un UserResponseDTO.
        //    Questo DTO sarà la risposta inviata al client, contenente solo i dati esposti.
        return convertToResponseDto(savedUser);
    }

    /**
     * Implementazione del metodo per recuperare un utente tramite il suo ID univoco.
     * Il metodo cerca l'entità User nel database e, se trovata, la converte in un DTO di risposta.
     * @param id L'ID univoco (UUID) dell'utente da recuperare.
     * @return Un Optional contenente il UserResponseDTO se l'utente esiste, altrimenti un Optional vuoto.
     */
    @Override // Indica che questo metodo implementa un metodo definito nell'interfaccia UserService.
    public Optional<UserResponseDTO> getUserById(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Utente con ID " + id + " non trovato"));
        return Optional.of(convertToResponseDto(user));
    }

    /**
     * Implementazione del metodo per recuperare tutti gli utenti registrati nel sistema.
     * Recupera tutte le entità User dal database e le converte in una lista di DTO di risposta.
     * @return Una lista di UserResponseDTO, rappresentante tutti gli utenti.
     */
    @Override // Indica che questo metodo implementa un metodo definito nell'interfaccia UserService.
    public List<UserResponseDTO> getAllUsers() {
        // Recupera tutte le entità User dal database.
        return userRepository.findAll().stream()
                // Per ogni entità User nello stream, la mappa a un UserResponseDTO.
                .map(this::convertToResponseDto)
                // Colleziona tutti i DTO mappati in una nuova lista.
                .collect(Collectors.toList());
    }

    /**
     * Implementazione del metodo per aggiornare i dati di un utente esistente.
     */
    @Override
    @Transactional
    public UserResponseDTO updateUser(UUID id, UserRequestDTO userRequestDTO) {
        // 1. Recupera l'utente autenticato
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();

        boolean isAdmin = currentUser.getRole() == Role.ROLE_ADMIN;
        boolean isSelf = currentUser.getId().equals(id);

        // 2. Controlli di autorizzazione
        if (!isAdmin && !isSelf) {
            throw new AccessDeniedException("Non sei autorizzato a modificare questo utente.");
        }

        // 3. Recupera l'utente da aggiornare
        User userToUpdate = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + id));

        // 4. Se è un utente normale, può aggiornare solo se stesso e solo email/password
        if (!isAdmin) {
            // Aggiorna solo email (se diversa)
            if (!userToUpdate.getEmail().equals(userRequestDTO.getEmail())) {
                userRepository.findByEmail(userRequestDTO.getEmail())
                        .ifPresent(existingUser -> {
                            if (!existingUser.getId().equals(id)) {
                                throw new RuntimeException("Email '" + userRequestDTO.getEmail() + "' già in uso.");
                            }
                        });
                userToUpdate.setEmail(userRequestDTO.getEmail());
            }

            // Aggiorna password se fornita
            if (userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().isBlank()) {
                userToUpdate.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
            }

            // Ignora aggiornamenti a username e ruolo
            return convertToResponseDto(userRepository.save(userToUpdate));
        }

        // 5. Se è ADMIN, può aggiornare tutto

        // Controllo unicità username
        userRepository.findByUsername(userRequestDTO.getUsername())
                    .ifPresent(existingUser -> {
                        if (!existingUser.getId().equals(id)) {
                            throw new RuntimeException("Username '" + userRequestDTO.getUsername() + "' già in uso.");
                        }
                    });

        // Controllo unicità email
        userRepository.findByEmail(userRequestDTO.getEmail())
                    .ifPresent(existingUser -> {
                        if (!existingUser.getId().equals(id)) {
                            throw new RuntimeException("Email '" + userRequestDTO.getEmail() + "' già in uso.");
                        }
                    });

        // Aggiorna tutti i campi
        userToUpdate.setUsername(userRequestDTO.getUsername());
        userToUpdate.setEmail(userRequestDTO.getEmail());

        if (userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().isBlank()) {
            userToUpdate.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        }

        try {
            userToUpdate.setRole(Role.valueOf(userRequestDTO.getRole()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Ruolo non valido: " + userRequestDTO.getRole());
        }

        return convertToResponseDto(userRepository.save(userToUpdate));
    }


    /**
     * Implementazione del metodo per eliminare un utente tramite il suo ID.
     * Il metodo verifica prima se l'utente esiste e poi procede con l'eliminazione.
     * @param id L'ID dell'utente da eliminare.
     * @throws ResourceNotFoundException se l'utente con l'ID specificato non viene trovato.
     */
    @Override // Indica che questo metodo implementa un metodo definito nell'interfaccia UserService.
    @Transactional // Assicura che l'operazione di eliminazione sia atomica.
    public void deleteUser(UUID id) {
        // 1. Recupera l'utente. Se non trovato, ResourceNotFoundException viene lanciata.
        User userToDelete = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con ID: " + id));

        // 2. Elimina l'utente dal database tramite l'entità trovata.
        //    NOTA: Grazie alle configurazioni di cascata nelle relazioni di User (es. con UserProfile e UserGame),
        //    le entità correlate potrebbero essere eliminate automaticamente (se configurato con CascadeType.ALL e orphanRemoval).
        //    Assicurati che il comportamento di cascata sia quello desiderato.
        userRepository.delete(userToDelete);
    }

    // --- Metodi di Mappatura (Conversione tra Entità e DTO) ---

    /**
     * Metodo privato helper per convertire un'entità User (dal database)
     * in un UserResponseDTO (per l'invio al client).
     * Questo metodo è utilizzato internamente dal servizio per preparare le risposte API.
     * @param user L'entità User da convertire.
     * @return Il UserResponseDTO corrispondente, contenente i dati esposti all'API.
     */
    private UserResponseDTO convertToResponseDto(User user) {
        // Crea una nuova istanza di UserResponseDTO.
        UserResponseDTO dto = new UserResponseDTO();
        // Popola i campi del DTO utilizzando i getter dell'entità User.
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name()); // Mappa il ruolo come stringa (es. "ROLE_USER")

        // NOTA: Come concordato, non includiamo qui UserProfileResponseDTO nidificato
        // per mantenere UserResponseDTO più semplice e focalizzato sui dati base dell'utente.
        // Se in futuro si volesse includere il profilo, questa è la posizione dove gestirlo.

        return dto;
    }
}
