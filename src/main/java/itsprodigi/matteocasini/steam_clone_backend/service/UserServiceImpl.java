package itsprodigi.matteocasini.steam_clone_backend.service;

// Importa le classi necessarie dai pacchetti del tuo progetto
import itsprodigi.matteocasini.steam_clone_backend.model.User; // L'entità User per interagire con il DB
import itsprodigi.matteocasini.steam_clone_backend.repository.UserRepository; // L'interfaccia per le operazioni DB su User
import itsprodigi.matteocasini.steam_clone_backend.dto.UserRequestDTO; // DTO per i dati in ingresso
import itsprodigi.matteocasini.steam_clone_backend.dto.UserResponseDTO; // DTO per i dati in uscita
import itsprodigi.matteocasini.steam_clone_backend.exception.ResourceNotFoundException; // La tua eccezione personalizzata per risorse non trovate

// Importa le annotazioni e classi di Spring Framework
import org.springframework.stereotype.Service; // Indica a Spring che questa è una classe di servizio (componente gestito)
import org.springframework.transaction.annotation.Transactional; // Gestione delle transazioni di database

// Importa classi standard di Java
import java.util.List; // Per lavorare con liste di oggetti
import java.util.UUID; // Per lavorare con gli UUID
import java.util.stream.Collectors; // Per facilitare la trasformazione di stream in liste

@Service // Marca la classe come un Service di Spring, gestito dal contenitore IoC
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository; // Iniezione della dipendenza del UserRepository

    // Costruttore per l'iniezione delle dipendenze (Spring inietta UserRepository qui)
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Crea un nuovo utente nel sistema.
     * Applica controlli di unicità per username ed email prima di salvare.
     * @param userRequestDTO DTO contenente i dati del nuovo utente.
     * @return UserResponseDTO con i dati dell'utente creato e il suo UUID.
     * @throws IllegalStateException Se username o email sono già in uso.
     */
    @Override // Indica che questo metodo implementa un metodo dall'interfaccia UserService
    @Transactional // Ogni operazione all'interno di questo metodo è gestita come una singola transazione database
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        // Controllo per evitare username duplicati
        if (userRepository.existsByUsername(userRequestDTO.getUsername())) {
            throw new IllegalStateException("Username is already taken.");
        }
        // Controllo per evitare email duplicate
        if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
            throw new IllegalStateException("Email is already registered.");
        }

        User user = new User(); // Crea una nuova istanza dell'entità User
        user.setUsername(userRequestDTO.getUsername());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(userRequestDTO.getPassword()); // NOTA: In un'applicazione reale, qui andrebbe l'hashing della password!

        // Salva l'utente nel database. Il metodo @PrePersist in User.java genererà l'UUID per 'id'.
        User savedUser = userRepository.save(user);
        // Converte l'entità salvata in un DTO di risposta e lo restituisce.
        // Il DTO prenderà l'UUID dal campo 'id' dell'utente, che ora è il suo identificatore pubblico.
        return new UserResponseDTO(savedUser);
    }

    /**
     * Recupera un utente tramite il suo UUID.
     * @param uuid L'UUID dell'utente da recuperare.
     * @return UserResponseDTO con i dati dell'utente.
     * @throws ResourceNotFoundException Se l'utente con l'UUID specificato non viene trovato.
     */
    @Override
    @Transactional(readOnly = true) // La transazione è in sola lettura, ottimizzata per le query
    public UserResponseDTO getUserByUuid(UUID uuid) {
        // CAMBIAMENTO CHIAVE: Utilizza findById del UserRepository.
        // Poiché ora UUID è l'ID primario dell'entità User, findById funziona direttamente.
        User user = userRepository.findById(uuid)
                // Se l'utente non viene trovato, lancia un'eccezione ResourceNotFoundException.
                .orElseThrow(() -> new ResourceNotFoundException("User not found with UUID: " + uuid));
        // Converte l'entità trovata in un DTO di risposta.
        return new UserResponseDTO(user);
    }

    /**
     * Recupera tutti gli utenti presenti nel sistema.
     * @return Lista di UserResponseDTO di tutti gli utenti.
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        // Recupera tutti gli utenti dal database
        return userRepository.findAll().stream()
                // Mappa ogni entità User in un UserResponseDTO
                .map(UserResponseDTO::new)
                // Raccoglie i DTO in una lista
                .collect(Collectors.toList());
    }

    /**
     * Aggiorna i dati di un utente esistente.
     * Applica controlli di unicità per username ed email, escludendo l'utente corrente.
     * @param uuid L'UUID dell'utente da aggiornare.
     * @param userRequestDTO DTO contenente i nuovi dati dell'utente.
     * @return UserResponseDTO con i dati dell'utente aggiornato.
     * @throws ResourceNotFoundException Se l'utente con l'UUID specificato non viene trovato.
     * @throws IllegalStateException Se il nuovo username o email sono già in uso da un altro utente.
     */
    @Override
    @Transactional
    public UserResponseDTO updateUser(UUID uuid, UserRequestDTO userRequestDTO) {
        // CAMBIAMENTO CHIAVE: Recupera l'utente esistente tramite findById
        User existingUser = userRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with UUID: " + uuid));

        // Controlla se il nuovo username è già preso da un *altro* utente.
        // Se il username è lo stesso dell'utente corrente, non c'è bisogno di controllare.
        if (!existingUser.getUsername().equals(userRequestDTO.getUsername()) && userRepository.existsByUsername(userRequestDTO.getUsername())) {
            throw new IllegalStateException("Username is already taken.");
        }
        // Controlla se la nuova email è già registrata da un *altro* utente.
        if (!existingUser.getEmail().equals(userRequestDTO.getEmail()) && userRepository.existsByEmail(userRequestDTO.getEmail())) {
            throw new IllegalStateException("Email is already registered.");
        }

        // Aggiorna i campi dell'utente esistente con i nuovi dati dal DTO
        existingUser.setUsername(userRequestDTO.getUsername());
        existingUser.setEmail(userRequestDTO.getEmail());
        existingUser.setPassword(userRequestDTO.getPassword()); // NOTA: Qui andrebbe gestito l'aggiornamento sicuro della password

        User updatedUser = userRepository.save(existingUser); // Salva le modifiche nel database
        return new UserResponseDTO(updatedUser); // Restituisce l'utente aggiornato come DTO
    }

    /**
     * Elimina un utente tramite il suo UUID.
     * @param uuid L'UUID dell'utente da eliminare.
     * @throws ResourceNotFoundException Se l'utente con l'UUID specificato non viene trovato.
     */
    @Override
    @Transactional
    public void deleteUser(UUID uuid) {
        // CAMBIAMENTO CHIAVE: Prima di eliminare, controlla se l'utente esiste con existsById.
        if (!userRepository.existsById(uuid)) {
            throw new ResourceNotFoundException("User not found with UUID: " + uuid);
        }
        // Elimina l'utente dal database tramite il suo UUID.
        userRepository.deleteById(uuid);
    }
}