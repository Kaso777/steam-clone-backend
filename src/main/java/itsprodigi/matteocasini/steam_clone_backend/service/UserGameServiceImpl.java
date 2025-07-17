package itsprodigi.matteocasini.steam_clone_backend.service;

import itsprodigi.matteocasini.steam_clone_backend.model.Game; // Importa l'entità Game
import itsprodigi.matteocasini.steam_clone_backend.model.User; // Importa l'entità User
import itsprodigi.matteocasini.steam_clone_backend.model.UserGame; // Importa l'entità UserGame
import itsprodigi.matteocasini.steam_clone_backend.model.UserGameId; // Importa la chiave composita UserGameId
import itsprodigi.matteocasini.steam_clone_backend.repository.GameRepository; // Importa il GameRepository
import itsprodigi.matteocasini.steam_clone_backend.repository.UserGameRepository; // Importa il UserGameRepository
import itsprodigi.matteocasini.steam_clone_backend.repository.UserRepository; // Importa il UserRepository
import itsprodigi.matteocasini.steam_clone_backend.service.UserGameService; // Importa l'interfaccia UserGameService
import itsprodigi.matteocasini.steam_clone_backend.dto.GameResponseDTO; // Importa GameResponseDTO per la nidificazione
import itsprodigi.matteocasini.steam_clone_backend.dto.LibraryGameItemDTO; // Importa LibraryGameItemDTO (per la lista nella libreria)
import itsprodigi.matteocasini.steam_clone_backend.dto.UserGameRequestDTO; // Importa UserGameRequestDTO (per l'input)
import itsprodigi.matteocasini.steam_clone_backend.dto.UserGameResponseDTO; // Importa UserGameResponseDTO (per la singola voce completa)
import itsprodigi.matteocasini.steam_clone_backend.dto.UserLibraryResponseDTO; // Importa UserLibraryResponseDTO (per l'intera libreria)
import itsprodigi.matteocasini.steam_clone_backend.dto.UserResponseDTO; // Importa UserResponseDTO (per la nidificazione)

import org.springframework.beans.factory.annotation.Autowired; // Per l'iniezione delle dipendenze
import org.springframework.stereotype.Service; // Indica che questa è una classe di servizio Spring
import org.springframework.transaction.annotation.Transactional; // Per la gestione delle transazioni di database

import java.util.List; // Per liste di oggetti
import java.util.Optional; // Per gestire la possibile assenza di un valore
import java.util.UUID; // Per gli ID univoci
import java.util.stream.Collectors; // Per collezionare elementi da uno stream

/**
 * Implementazione concreta dell'interfaccia UserGameService.
 * Questa classe contiene la logica di business per la gestione delle associazioni
 * tra Utenti e Giochi (ovvero, la libreria di giochi di un utente).
 * Interagisce con i Repository per accedere ai dati nel database
 * e con i Data Transfer Objects (DTO) per le interazioni con il livello Controller.
 */
@Service // Indica a Spring che questa classe è un componente di servizio e deve essere gestita dal suo IoC container.
public class UserGameServiceImpl implements UserGameService { // Implementa l'interfaccia UserGameService, rispettando il contratto.

    private final UserGameRepository userGameRepository; // Repository per le operazioni su UserGame.
    private final UserRepository userRepository;         // Repository per recuperare le entità User.
    private final GameRepository gameRepository;         // Repository per recuperare le entità Game.

    /**
     * Costruttore per l'iniezione delle dipendenze.
     * Spring inietta automaticamente le istanze dei Repository necessari.
     * @param userGameRepository Il Repository per UserGame.
     * @param userRepository Il Repository per User.
     * @param gameRepository Il Repository per Game.
     */
    @Autowired
    public UserGameServiceImpl(UserGameRepository userGameRepository,
                               UserRepository userRepository,
                               GameRepository gameRepository) {
        this.userGameRepository = userGameRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    /**
     * Implementazione del metodo per aggiungere un gioco alla libreria di un utente.
     * Recupera le entità User e Game dagli UUID forniti nel DTO di richiesta,
     * crea una nuova entità UserGame e la salva nel database.
     * @param userGameRequestDTO DTO contenente gli UUID dell'utente e del gioco, la data di acquisto e le ore giocate.
     * @return Il UserGameResponseDTO della voce della libreria appena creata.
     * @throws RuntimeException se l'utente o il gioco non vengono trovati, o se l'associazione esiste già.
     */
    @Override // Indica che questo metodo implementa un metodo definito nell'interfaccia UserGameService.
    @Transactional // Assicura che l'intera operazione (lettura e scrittura nel DB) sia atomica.
                   // Se un'eccezione viene lanciata, tutte le modifiche al database vengono annullate (rollback).
    public UserGameResponseDTO addGameToUserLibrary(UserGameRequestDTO userGameRequestDTO) {
        // 1. Recupera l'entità User dall'UUID fornito nel DTO di richiesta.
        User user = userRepository.findById(userGameRequestDTO.getUserUuid())
                .orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + userGameRequestDTO.getUserUuid()));

        // 2. Recupera l'entità Game dall'UUID fornito nel DTO di richiesta.
        Game game = gameRepository.findById(userGameRequestDTO.getGameUuid())
                .orElseThrow(() -> new RuntimeException("Gioco non trovato con ID: " + userGameRequestDTO.getGameUuid()));

        // 3. Verifica se l'associazione UserGame esiste già per evitare duplicati.
        //    Crea un oggetto UserGameId con gli UUID dell'utente e del gioco per la ricerca.
        UserGameId id = new UserGameId(user.getId(), game.getId());
        if (userGameRepository.existsById(id)) {
            throw new RuntimeException("Il gioco è già nella libreria dell'utente.");
        }

        // 4. Crea una nuova entità UserGame e popola i suoi campi con i dati dal DTO di richiesta.
        UserGame userGame = new UserGame(user, game, userGameRequestDTO.getPurchaseDate(), userGameRequestDTO.getPlaytimeHours());

        // 5. Salva la nuova entità UserGame nel database tramite il UserRepository.
        UserGame savedUserGame = userGameRepository.save(userGame);

        // 6. Converte l'entità salvata in un UserGameResponseDTO e lo restituisce.
        return convertToUserGameResponseDto(savedUserGame);
    }

    /**
     * Implementazione del metodo per recuperare una specifica voce UserGame
     * tramite l'ID dell'utente e l'ID del gioco.
     * @param userUuid L'UUID dell'utente.
     * @param gameUuid L'UUID del gioco.
     * @return Un Optional contenente il UserGameResponseDTO se la voce esiste, altrimenti Optional.empty().
     */
    @Override // Indica che questo metodo implementa un metodo definito nell'interfaccia UserGameService.
    public Optional<UserGameResponseDTO> getUserGameByIds(UUID userUuid, UUID gameUuid) {
        // 1. Crea l'ID composito per la ricerca.
        UserGameId id = new UserGameId(userUuid, gameUuid);
        // 2. Cerca l'entità UserGame nel database tramite il suo ID composito.
        return userGameRepository.findById(id)
                // 3. Se trovata, la mappa in un UserGameResponseDTO usando il metodo helper.
                .map(this::convertToUserGameResponseDto);
    }

    /**
     * Implementazione del metodo per recuperare l'intera libreria di giochi per un utente specifico.
     * Recupera l'entità User e tutte le sue associazioni UserGame, poi le mappa
     * nel DTO aggregato UserLibraryResponseDTO. Questo DTO è progettato per mostrare
     * i dettagli dell'utente una sola volta e una lista di giochi senza ridondanze.
     * @param userUuid L'UUID dell'utente di cui recuperare la libreria.
     * @return Un UserLibraryResponseDTO contenente i dettagli dell'utente e la lista dei suoi giochi.
     * Restituisce un DTO con utente null e lista giochi vuota se l'utente non esiste.
     */
    @Override // Indica che questo metodo implementa un metodo definito nell'interfaccia UserGameService.
    @Transactional(readOnly = true) // Indica che questa transazione è di sola lettura, potenzialmente ottimizzata dal driver JDBC.
    public UserLibraryResponseDTO getUserLibrary(UUID userUuid) {
        // 1. Recupera l'entità User. Questo è necessario per popolare il campo 'user' nel UserLibraryResponseDTO.
        Optional<User> userOptional = userRepository.findById(userUuid);
        if (userOptional.isEmpty()) {
            // Se l'utente non esiste nel database, restituisce un DTO di libreria vuota.
            // Il costruttore senza argomenti di UserLibraryResponseDTO inizializzerà i campi a null/lista vuota.
            return new UserLibraryResponseDTO();
        }
        User user = userOptional.get(); // Ottiene l'entità User se presente.

        // 2. Recupera tutte le voci UserGame associate a questo utente usando il suo ID.
        //    Il metodo findByUserId è stato aggiunto al UserGameRepository per questo scopo.
        List<UserGame> userGames = userGameRepository.findByUserId(userUuid);

        // 3. Converte l'entità User e la lista di UserGame nel DTO aggregato UserLibraryResponseDTO.
        //    Il costruttore di UserLibraryResponseDTO si occuperà di mappare l'utente e la lista di giochi
        //    utilizzando internamente LibraryGameItemDTO per ogni voce della lista.
        return new UserLibraryResponseDTO(user, userGames);
    }

    /**
     * Implementazione del metodo per aggiornare gli attributi di una voce UserGame esistente (es. ore giocate).
     * @param userUuid L'UUID dell'utente.
     * @param gameUuid L'UUID del gioco.
     * @param userGameRequestDTO DTO contenente i dati aggiornati (es. nuove ore giocate).
     * @return Il UserGameResponseDTO della voce della libreria aggiornata.
     * @throws RuntimeException se l'associazione UserGame non viene trovata.
     */
    @Override // Indica che questo metodo implementa un metodo definito nell'interfaccia UserGameService.
    @Transactional // Assicura che l'operazione sia atomica.
    public UserGameResponseDTO updateUserGame(UUID userUuid, UUID gameUuid, UserGameRequestDTO userGameRequestDTO) {
        // 1. Crea l'ID composito per la ricerca.
        UserGameId id = new UserGameId(userUuid, gameUuid);
        // 2. Trova l'entità UserGame da aggiornare. Se non trovata, lancia un'eccezione.
        UserGame userGame = userGameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Associazione utente-gioco non trovata per utente " + userUuid + " e gioco " + gameUuid));

        // 3. Aggiorna i campi dell'entità UserGame con i dati dal DTO di richiesta.
        //    In questo contesto, assumiamo che 'playtimeHours' e 'purchaseDate' possano essere aggiornati.
        userGame.setPlaytimeHours(userGameRequestDTO.getPlaytimeHours());
        userGame.setPurchaseDate(userGameRequestDTO.getPurchaseDate()); // Aggiorna anche la data di acquisto se presente nel DTO

        // 4. Salva l'entità UserGame aggiornata nel database.
        UserGame updatedUserGame = userGameRepository.save(userGame);

        // 5. Converte l'entità aggiornata in un UserGameResponseDTO e lo restituisce.
        return convertToUserGameResponseDto(updatedUserGame);
    }

    /**
     * Implementazione del metodo per rimuovere un gioco dalla libreria di un utente.
     * @param userUuid L'UUID dell'utente.
     * @param gameUuid L'UUID del gioco da rimuovere.
     * @throws RuntimeException se l'associazione UserGame non viene trovata.
     */
    @Override // Indica che questo metodo implementa un metodo definito nell'interfaccia UserGameService.
    @Transactional // Assicura che l'operazione sia atomica.
    public void removeGameFromUserLibrary(UUID userUuid, UUID gameUuid) {
        // 1. Crea l'ID composito per la ricerca.
        UserGameId id = new UserGameId(userUuid, gameUuid);
        // 2. Verifica se l'associazione UserGame esiste prima di tentare l'eliminazione.
        if (!userGameRepository.existsById(id)) {
            throw new RuntimeException("Associazione utente-gioco non trovata per utente " + userUuid + " e gioco " + gameUuid);
        }
        // 3. Elimina l'associazione UserGame dal database.
        userGameRepository.deleteById(id);
    }

    /**
     * Implementazione del metodo per recuperare tutti i giochi presenti in tutte le librerie.
     * Mappa ogni entità UserGame in un UserGameResponseDTO e restituisce una lista di DTO.
     * @return Una lista di UserGameResponseDTO.
     */
    @Override // Indica che questo metodo implementa un metodo definito nell'interfaccia UserGameService.
    public List<UserGameResponseDTO> getAllUserGames() {
        // Recupera tutte le entità UserGame dal database.
        return userGameRepository.findAll().stream()
                // Mappa ogni entità UserGame a un UserGameResponseDTO.
                .map(this::convertToUserGameResponseDto)
                // Colleziona i DTO in una lista.
                .collect(Collectors.toList());
    }

    // --- Metodi di Mappatura (Conversione tra Entità e DTO) ---

    /**
     * Metodo privato helper per convertire un'entità UserGame (dal database)
     * in un UserGameResponseDTO (per l'invio al client o per l'uso interno del servizio).
     * Questo DTO include i dettagli completi dell'utente e del gioco.
     * @param userGame L'entità UserGame da convertire.
     * @return Il UserGameResponseDTO corrispondente.
     */
    private UserGameResponseDTO convertToUserGameResponseDto(UserGame userGame) {
        // NOTA IMPORTANTE: Assicurati che userGame.getUser() e userGame.getGame() siano caricati
        // (non lazy-loaded) quando questo metodo viene chiamato, altrimenti potresti
        // incorrere in LazyInitializationException.
        // Questo è gestito implicitamente se il metodo chiamante è @Transactional.

        // Crea e popola il DTO dell'utente nidificato.
        // Utilizza il costruttore di UserResponseDTO che accetta un'entità User.
        UserResponseDTO userDto = new UserResponseDTO(userGame.getUser());
        
        // Crea e popola il DTO del gioco nidificato.
        // Utilizza il costruttore di GameResponseDTO che accetta un'entità Game.
        GameResponseDTO gameDto = new GameResponseDTO(userGame.getGame());

        // Crea e restituisce il UserGameResponseDTO completo.
        return new UserGameResponseDTO(
                userDto,
                gameDto,
                userGame.getPurchaseDate(),
                userGame.getPlaytimeHours()
        );
    }

    /**
     * Metodo privato helper per convertire un'entità UserGame (dal database)
     * in un LibraryGameItemDTO (per l'uso all'interno di UserLibraryResponseDTO).
     * Questo DTO include i dettagli del gioco e gli attributi della relazione, ma NON i dettagli dell'utente.
     * @param userGame L'entità UserGame da convertire.
     * @return Il LibraryGameItemDTO corrispondente.
     */
    private LibraryGameItemDTO convertToLibraryItemDto(UserGame userGame) {
        // NOTA IMPORTANTE: Assicurati che userGame.getGame() sia caricato
        // (non lazy-loaded) quando questo metodo viene chiamato.

        // Crea e popola il DTO del gioco nidificato.
        // Utilizza il costruttore di GameResponseDTO che accetta un'entità Game.
        GameResponseDTO gameDto = new GameResponseDTO(userGame.getGame());

        // Crea e restituisce il LibraryGameItemDTO.
        return new LibraryGameItemDTO(
                gameDto,
                userGame.getPurchaseDate(),
                userGame.getPlaytimeHours()
        );
    }
}