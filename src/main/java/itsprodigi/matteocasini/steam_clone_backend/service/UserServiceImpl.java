package itsprodigi.matteocasini.steam_clone_backend.service;

import itsprodigi.matteocasini.steam_clone_backend.model.User; // Importa l'entità User
import itsprodigi.matteocasini.steam_clone_backend.repository.UserRepository; // Importa il Repository per User
import itsprodigi.matteocasini.steam_clone_backend.dto.UserRequestDTO; // Importa il DTO di richiesta per User
import itsprodigi.matteocasini.steam_clone_backend.dto.UserResponseDTO; // Importa il DTO di risposta per User

import org.springframework.beans.factory.annotation.Autowired; // Per l'iniezione delle dipendenze
import org.springframework.security.crypto.password.PasswordEncoder; // Per hashare le password in modo sicuro
import org.springframework.stereotype.Service; // Indica che questa è una classe di servizio Spring
import org.springframework.transaction.annotation.Transactional; // Per la gestione delle transazioni di database

import java.util.List; // Per liste di oggetti
import java.util.Optional; // Per gestire la possibile assenza di un valore
import java.util.UUID; // Per gli ID univoci degli utenti
import java.util.stream.Collectors; // Per collezionare elementi da uno stream

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

        // 2. Crea una nuova entità User e popola i suoi campi con i dati dal DTO di richiesta.
        User user = new User();
        user.setUsername(userRequestDTO.getUsername());
        user.setEmail(userRequestDTO.getEmail());
        // 3. Hashes la password fornita nel DTO prima di impostarla nell'entità User.
        //    È una best practice di sicurezza fondamentale: MAI salvare password in chiaro nel database.
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        // 4. Imposta il ruolo dell'utente, come specificato nel DTO di richiesta.
        user.setRole(userRequestDTO.getRole());

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
        // Cerca l'entità User nel database tramite il suo ID.
        return userRepository.findById(id)
                // Se l'Optional contiene un'entità User (cioè l'utente è stato trovato),
                // applica il metodo 'convertToResponseDto' per trasformarla in un DTO.
                .map(this::convertToResponseDto);
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
     * Il metodo cerca l'utente per ID, verifica l'unicità dello username e dell'email (escludendo l'utente stesso),
     * aggiorna i campi dell'entità con i nuovi dati dal DTO di richiesta e salva le modifiche.
     * @param id L'ID dell'utente da aggiornare.
     * @param userRequestDTO Il DTO contenente i nuovi dati dell'utente (username, email, password, role).
     * @return Il UserResponseDTO dell'utente aggiornato.
     * @throws RuntimeException se l'utente non viene trovato o se username/email sono già in uso da altri utenti.
     */
    @Override // Indica che questo metodo implementa un metodo definito nell'interfaccia UserService.
    @Transactional // Assicura che l'operazione di aggiornamento sia atomica.
    public UserResponseDTO updateUser(UUID id, UserRequestDTO userRequestDTO) {
        // 1. Trova l'utente da aggiornare per ID. Se l'utente non esiste, lancia un'eccezione.
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + id));

        // 2. Verifica l'unicità dello username:
        //    Cerca un utente con lo username fornito. Se esiste, controlla che non sia l'utente che stiamo aggiornando.
        //    Questo impedisce a un utente di cambiare il suo username con uno già preso da un altro utente.
        userRepository.findByUsername(userRequestDTO.getUsername())
                .ifPresent(existingUser -> {
                    if (!existingUser.getId().equals(id)) { // Se l'ID dell'utente esistente NON è l'ID dell'utente che stiamo aggiornando
                        throw new RuntimeException("Nome utente '" + userRequestDTO.getUsername() + "' già in uso da un altro utente.");
                    }
                });
        // 3. Verifica l'unicità dell'email:
        //    Stesso principio dello username, ma per l'email.
        userRepository.findByEmail(userRequestDTO.getEmail())
                .ifPresent(existingUser -> {
                    if (!existingUser.getId().equals(id)) {
                        throw new RuntimeException("Email '" + userRequestDTO.getEmail() + "' già in uso da un altro utente.");
                    }
                });

        // 4. Aggiorna i campi dell'entità User con i dati provenienti dal DTO di richiesta.
        user.setUsername(userRequestDTO.getUsername());
        user.setEmail(userRequestDTO.getEmail());
        // 5. Aggiorna la password solo se una nuova password è stata fornita nel DTO e non è vuota.
        //    Questo permette agli utenti di aggiornare altri dati senza dover reinserire la password.
        if (userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        }
        // 6. Aggiorna il ruolo dell'utente.
        user.setRole(userRequestDTO.getRole());

        // 7. Salva l'entità User aggiornata nel database.
        User updatedUser = userRepository.save(user);
        // 8. Converte l'entità aggiornata in un UserResponseDTO e lo restituisce.
        return convertToResponseDto(updatedUser);
    }

    /**
     * Implementazione del metodo per eliminare un utente tramite il suo ID.
     * Il metodo verifica prima se l'utente esiste e poi procede con l'eliminazione.
     * @param id L'ID dell'utente da eliminare.
     * @throws RuntimeException se l'utente con l'ID specificato non viene trovato.
     */
    @Override // Indica che questo metodo implementa un metodo definito nell'interfaccia UserService.
    @Transactional // Assicura che l'operazione di eliminazione sia atomica.
    public void deleteUser(UUID id) {
        // 1. Verifica se l'utente esiste prima di tentare l'eliminazione.
        //    Questo previene un'eccezione se si tenta di eliminare un utente inesistente.
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Utente non trovato con ID: " + id);
        }
        // 2. Elimina l'utente dal database tramite il suo ID.
        //    NOTA: Grazie alle configurazioni di cascata nelle relazioni di User (es. con UserProfile e UserGame),
        //    le entità correlate potrebbero essere eliminate automaticamente (se configurato con CascadeType.ALL e orphanRemoval).
        //    Assicurati che il comportamento di cascata sia quello desiderato.
        userRepository.deleteById(id);
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
        dto.setRole(user.getRole()); // Popola il campo 'role' nel DTO di risposta.

        // NOTA: Come concordato, non includiamo qui UserProfileResponseDTO nidificato
        // per mantenere UserResponseDTO più semplice e focalizzato sui dati base dell'utente.
        // Se in futuro si volesse includere il profilo, questa è la posizione dove gestirlo.

        return dto;
    }
}