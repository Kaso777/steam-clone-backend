package itsprodigi.matteocasini.steam_clone_backend.service;

// Importa i DTO che verranno usati come parametri di input o tipi di ritorno
import itsprodigi.matteocasini.steam_clone_backend.dto.UserRequestDTO;
import itsprodigi.matteocasini.steam_clone_backend.dto.UserResponseDTO;

// Importa le classi standard di Java necessarie
import java.util.List; // Per restituire liste di utenti
import java.util.UUID; // Per identificare gli utenti tramite il loro ID pubblico (UUID)

/**
 * Interfaccia per il servizio utente.
 * Definisce le operazioni di business che possono essere eseguite
 * relative alla gestione degli utenti.
 * Questa astrazione permette di separare la definizione delle operazioni
 * dalla loro implementazione concreta (che sarà in UserServiceImpl).
 */
public interface UserService {

    /**
     * Crea un nuovo utente nel sistema.
     * Riceve i dati dell'utente tramite un UserRequestDTO e restituisce
     * i dettagli dell'utente creato (con l'UUID generato) tramite un UserResponseDTO.
     * @param userRequestDTO DTO contenente i dati dell'utente da creare.
     * @return UserResponseDTO contenente i dati dell'utente appena creato.
     */
    UserResponseDTO createUser(UserRequestDTO userRequestDTO);

    /**
     * Recupera una lista di tutti gli utenti registrati nel sistema.
     * Ogni utente viene restituito come un UserResponseDTO.
     * @return Una lista di UserResponseDTO che rappresentano tutti gli utenti.
     */
    List<UserResponseDTO> getAllUsers();

    /**
     * Recupera un singolo utente tramite il suo UUID (identificatore pubblico).
     * Se l'utente non viene trovato, verrà lanciata un'eccezione (gestita dal GlobalExceptionHandler).
     * @param uuid L'UUID dell'utente da recuperare.
     * @return UserResponseDTO contenente i dati dell'utente trovato.
     */
    UserResponseDTO getUserByUuid(UUID uuid);

    /**
     * Aggiorna i dati di un utente esistente identificato dal suo UUID.
     * Riceve i dati aggiornati tramite un UserRequestDTO.
     * Se l'utente non viene trovato, verrà lanciata un'eccezione.
     * @param uuid L'UUID dell'utente da aggiornare.
     * @param userRequestDTO DTO contenente i nuovi dati per l'utente.
     * @return UserResponseDTO contenente i dati aggiornati dell'utente.
     */
    UserResponseDTO updateUser(UUID uuid, UserRequestDTO userRequestDTO);

    /**
     * Elimina un utente dal sistema tramite il suo UUID.
     * Se l'utente non viene trovato, verrà lanciata un'eccezione.
     * @param uuid L'UUID dell'utente da eliminare.
     */
    void deleteUser(UUID uuid);
} 