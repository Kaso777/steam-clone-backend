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

/**
 * Implementazione concreta dell'interfaccia UserService.
 * Contiene la logica di business effettiva per la gestione degli utenti.
 */
@Service // Questa annotazione rende UserServiceImpl un "componente" gestito da Spring.
         // Spring creerà un'istanza di questa classe e la inietterà dove è richiesta.
public class UserServiceImpl implements UserService { // Implementa il contratto definito nell'interfaccia UserService

    private final UserRepository userRepository; // Dichiarazione di una dipendenza dal UserRepository

    /**
     * Costruttore per l'iniezione delle dipendenze (Dependency Injection).
     * Spring rileverà automaticamente questo costruttore e fornirà un'istanza
     * di UserRepository quando creerà un'istanza di UserServiceImpl.
     * @param userRepository L'istanza del repository per accedere al database.
     */
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Implementazione del metodo per creare un nuovo utente.
     * @param userRequestDTO DTO contenente i dati dell'utente da creare.
     * @return UserResponseDTO contenente i dati dell'utente creato.
     */
    @Override // Indica che questo metodo implementa un metodo dell'interfaccia UserService
    @Transactional // Questa annotazione rende il metodo transazionale. Se qualsiasi operazione database
                   // all'interno di questo metodo fallisce, l'intera transazione viene "rollbatta" (annullata),
                   // garantendo l'integrità dei dati. E' cruciale per le operazioni di scrittura.
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        // 1. Mappa il DTO di richiesta (UserRequestDTO) all'entità (User).
        //    Questo separa la rappresentazione dei dati dell'API dalla rappresentazione del database.
        User user = new User(); // Crea una nuova istanza dell'entità User
        user.setUsername(userRequestDTO.getUsername()); // Copia lo username dal DTO all'entità
        user.setEmail(userRequestDTO.getEmail());       // Copia l'email
        user.setPassword(userRequestDTO.getPassword()); // Copia la password (NOTA: Attualmente salvata in chiaro.
                                                        // Verrà crittografata con l'hashing in futuro per sicurezza).

        // 2. Salva l'entità User nel database tramite il repository.
        //    Il metodo save() di JpaRepository gestirà l'inserimento nel DB.
        //    L'UUID dell'utente verrà generato automaticamente dall'entità grazie a @PrePersist.
        user = userRepository.save(user);

        // 3. Mappa l'entità User salvata (che ora ha l'ID e l'UUID) a un DTO di risposta (UserResponseDTO).
        //    Questo DTO è ciò che verrà inviato indietro al client.
        return new UserResponseDTO(user);
    }

    /**
     * Implementazione del metodo per recuperare tutti gli utenti.
     * @return Una lista di UserResponseDTO.
     */
    @Override
    public List<UserResponseDTO> getAllUsers() {
        // 1. Recupera tutte le entità User dal database.
        //    findAll() è un metodo fornito da JpaRepository.
        return userRepository.findAll()
                // 2. Converte la Collection di entità User in uno Stream.
                .stream()
                // 3. Mappa ogni entità User recuperata a una nuova istanza di UserResponseDTO.
                //    'UserResponseDTO::new' è un method reference che indica di chiamare il costruttore
                //    di UserResponseDTO che accetta un oggetto User come parametro.
                .map(UserResponseDTO::new)
                // 4. Raccoglie tutti i DTO trasformati in una nuova lista.
                .collect(Collectors.toList());
    }

    /**
     * Implementazione del metodo per recuperare un utente tramite UUID.
     * @param uuid L'UUID dell'utente da cercare.
     * @return UserResponseDTO dell'utente trovato.
     */
    @Override
    public UserResponseDTO getUserByUuid(UUID uuid) {
        // 1. Cerca l'utente nel database tramite il suo UUID.
        //    findByUuid() è il metodo personalizzato che abbiamo aggiunto al UserRepository.
        //    Restituisce un Optional<User> perché l'utente potrebbe non esistere.
        User user = userRepository.findByUuid(uuid)
                // 2. Se l'Optional è vuoto (utente non trovato), lancia la tua eccezione personalizzata.
                //    Il GlobalExceptionHandler intercetterà questa eccezione e genererà una risposta 404.
                .orElseThrow(() -> new ResourceNotFoundException("User not found with UUID: " + uuid));
        
        // 3. Se l'utente è stato trovato, lo mappa a un UserResponseDTO e lo restituisce.
        return new UserResponseDTO(user);
    }

    /**
     * Implementazione del metodo per aggiornare un utente.
     * @param uuid L'UUID dell'utente da aggiornare.
     * @param userRequestDTO DTO con i dati aggiornati.
     * @return UserResponseDTO dell'utente aggiornato.
     */
    @Override
    @Transactional // Anche l'aggiornamento è un'operazione di scrittura che necessita di transazione.
    public UserResponseDTO updateUser(UUID uuid, UserRequestDTO userRequestDTO) {
        // 1. Cerca l'utente esistente da aggiornare.
        //    Se non trovato, lancia ResourceNotFoundException.
        User existingUser = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with UUID: " + uuid));

        // 2. Aggiorna i campi dell'entità esistente con i dati dal DTO di richiesta.
        existingUser.setUsername(userRequestDTO.getUsername());
        existingUser.setEmail(userRequestDTO.getEmail());
        existingUser.setPassword(userRequestDTO.getPassword()); // NOTA: La password non è ancora crittografata qui.

        // 3. Salva l'entità aggiornata nel database.
        //    Il metodo save() di JpaRepository, se l'entità ha già un ID, effettua un UPDATE.
        existingUser = userRepository.save(existingUser);

        // 4. Mappa l'entità aggiornata a un DTO di risposta e la restituisce.
        return new UserResponseDTO(existingUser);
    }

    /**
     * Implementazione del metodo per eliminare un utente.
     * @param uuid L'UUID dell'utente da eliminare.
     */
    @Override
    @Transactional // Le operazioni di eliminazione devono essere transazionali.
    public void deleteUser(UUID uuid) {
        // 1. Prima di tentare di eliminare, verifica se l'utente esiste.
        //    Usiamo existsByUuid() per efficienza (potrebbe essere più veloce di findByUuid se non serve l'oggetto completo).
        if (!userRepository.existsByUuid(uuid)) {
            // 2. Se l'utente non esiste, lancia ResourceNotFoundException.
            throw new ResourceNotFoundException("User not found with UUID: " + uuid);
        }
        // 3. Se l'utente esiste, procedi con l'eliminazione tramite il suo UUID.
        //    deleteByUuid() è il metodo personalizzato che abbiamo aggiunto al UserRepository.
        userRepository.deleteByUuid(uuid);
    }
}