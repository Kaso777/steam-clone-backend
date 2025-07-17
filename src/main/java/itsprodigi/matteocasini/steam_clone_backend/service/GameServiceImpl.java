package itsprodigi.matteocasini.steam_clone_backend.service;

import itsprodigi.matteocasini.steam_clone_backend.model.Game; // Importa l'entità Game
import itsprodigi.matteocasini.steam_clone_backend.model.Tag;   // Importa l'entità Tag
import itsprodigi.matteocasini.steam_clone_backend.repository.GameRepository; // Importa il GameRepository
import itsprodigi.matteocasini.steam_clone_backend.repository.TagRepository;   // Importa il TagRepository (necessario per i tag)
import itsprodigi.matteocasini.steam_clone_backend.service.GameService; // Importa l'interfaccia GameService
import itsprodigi.matteocasini.steam_clone_backend.dto.GameRequestDTO;  // Importa GameRequestDTO
import itsprodigi.matteocasini.steam_clone_backend.dto.GameResponseDTO; // Importa GameResponseDTO
import itsprodigi.matteocasini.steam_clone_backend.dto.TagDTO;          // Importa TagDTO (per la nidificazione in GameResponseDTO)
import itsprodigi.matteocasini.steam_clone_backend.exception.ResourceNotFoundException; // Importa l'eccezione personalizzata

import org.springframework.beans.factory.annotation.Autowired; // Per l'iniezione delle dipendenze
import org.springframework.stereotype.Service; // Indica che questa è una classe di servizio Spring
import org.springframework.transaction.annotation.Transactional; // Per la gestione delle transazioni di database

import java.util.HashSet; // Per collezioni Set (per i tag)
import java.util.List;    // Per liste di oggetti
import java.util.Optional; // Per gestire la possibile assenza di un valore
import java.util.Set;     // Interfaccia Set
import java.util.UUID;    // Per gli ID univoci
import java.util.stream.Collectors; // Per collezionare elementi da uno stream

/**
 * Implementazione concreta dell'interfaccia GameService.
 * Questa classe contiene la logica di business per la gestione dei Giochi.
 * È responsabile di interagire con il GameRepository e il TagRepository per accedere ai dati
 * e di mappare le entità del database (Game, Tag) ai Data Transfer Objects (DTO)
 * di richiesta (GameRequestDTO) e risposta (GameResponseDTO) per l'interazione con il livello Controller.
 */
@Service // Indica a Spring che questa classe è un componente di servizio e deve essere gestita dal suo IoC container.
public class GameServiceImpl implements GameService { // Implementa l'interfaccia GameService, rispettando il contratto.

    private final GameRepository gameRepository; // Repository per le operazioni su Game.
    private final TagRepository tagRepository;   // Repository per le operazioni su Tag (necessario per associare i tag ai giochi).

    /**
     * Costruttore per l'iniezione delle dipendenze.
     * Spring inietta automaticamente le istanze dei Repository necessari.
     * @param gameRepository Il Repository per Game.
     * @param tagRepository Il Repository per Tag.
     */
    @Autowired
    public GameServiceImpl(GameRepository gameRepository, TagRepository tagRepository) {
        this.gameRepository = gameRepository;
        this.tagRepository = tagRepository;
    }

    /**
     * Implementazione del metodo per creare un nuovo gioco nel sistema.
     * Prende i dati dal GameRequestDTO, crea un'entità Game, gestisce l'associazione con i Tag
     * (trovandoli o creandoli se non esistono) e salva il gioco nel database.
     * @param gameRequestDTO DTO contenente i dati del gioco da creare, inclusi i nomi dei tag.
     * @return DTO del gioco creato (GameResponseDTO).
     * @throws RuntimeException se un gioco con lo stesso titolo esiste già.
     */
    @Override // Indica che questo metodo implementa un metodo definito nell'interfaccia GameService.
    @Transactional // Assicura che l'intera operazione sia atomica.
    public GameResponseDTO createGame(GameRequestDTO gameRequestDTO) {
        // 1. Verifica se un gioco con lo stesso titolo esiste già per evitare duplicati.
        if (gameRepository.findByTitle(gameRequestDTO.getTitle()).isPresent()) {
            throw new RuntimeException("Un gioco con il titolo '" + gameRequestDTO.getTitle() + "' esiste già.");
        }

        // 2. Crea una nuova entità Game e popola i suoi campi dal DTO di richiesta.
        Game game = new Game();
        game.setTitle(gameRequestDTO.getTitle());
        game.setPrice(gameRequestDTO.getPrice());
        game.setReleaseDate(gameRequestDTO.getReleaseDate());
        game.setDeveloper(gameRequestDTO.getDeveloper());
        game.setPublisher(gameRequestDTO.getPublisher());

        // 3. Gestisce l'associazione dei Tag:
        //    Se il DTO di richiesta include nomi di tag, li processa.
        if (gameRequestDTO.getTagNames() != null && !gameRequestDTO.getTagNames().isEmpty()) {
            Set<Tag> tags = new HashSet<>();
            for (String tagName : gameRequestDTO.getTagNames()) {
                // Cerca un tag esistente per nome (case-insensitive per robustezza).
                Optional<Tag> existingTag = tagRepository.findByNameIgnoreCase(tagName);
                Tag tag;
                if (existingTag.isPresent()) {
                    tag = existingTag.get(); // Usa il tag esistente.
                } else {
                    // Se il tag non esiste, lo crea e lo salva.
                    tag = new Tag(tagName);
                    tagRepository.save(tag); // Salva il nuovo tag.
                }
                tags.add(tag); // Aggiunge il tag al set di tag del gioco.
            }
            game.setTags(tags); // Imposta il set di tag sul gioco.
        }

        // 4. Salva la nuova entità Game nel database.
        Game savedGame = gameRepository.save(game);

        // 5. Converte l'entità salvata in un GameResponseDTO e lo restituisce.
        return convertToResponseDto(savedGame);
    }

    /**
     * Implementazione del metodo per recuperare un gioco tramite il suo ID univoco.
     * @param id L'ID (UUID) del gioco da recuperare.
     * @return Un Optional contenente il DTO del gioco trovato (GameResponseDTO).
     * @throws ResourceNotFoundException se il gioco non viene trovato.
     */
    @Override // Indica che questo metodo implementa un metodo definito nell'interfaccia GameService.
    @Transactional(readOnly = true) // Transazione di sola lettura per ottimizzazione.
    public Optional<GameResponseDTO> getGameById(UUID id) {
        // Cerca l'entità Game nel database tramite il suo ID.
        // Utilizza un JOIN FETCH per caricare i tag insieme al gioco in una singola query,
        // evitando LazyInitializationException quando si converte in DTO.
        return gameRepository.findByIdWithTags(id)
                // Se l'Optional contiene un'entità Game, la mappa in un GameResponseDTO.
                .map(this::convertToResponseDto);
    }

    /**
     * Implementazione del metodo per recuperare tutti i giochi presenti nel sistema.
     * Recupera tutte le entità Game dal database e le converte in una lista di DTO di risposta.
     * @return Una lista di DTO di tutti i giochi (List<GameResponseDTO>).
     */
    @Override // Indica che questo metodo implementa un metodo definito nell'interfaccia GameService.
    @Transactional(readOnly = true) // Transazione di sola lettura per ottimizzazione.
    public List<GameResponseDTO> getAllGames() {
        // Recupera tutte le entità Game dal database.
        // Utilizza una query personalizzata per caricare i tag insieme ai giochi.
        return gameRepository.findAllWithTags().stream()
                // Per ogni entità Game nello stream, la mappa a un GameResponseDTO.
                .map(this::convertToResponseDto)
                // Colleziona tutti i DTO mappati in una nuova lista.
                .collect(Collectors.toList());
    }

    /**
     * Implementazione del metodo per aggiornare i dati di un gioco esistente.
     * Aggiorna i campi del gioco e le sue associazioni con i tag.
     * @param id L'ID (UUID) del gioco da aggiornare.
     * @param gameRequestDTO DTO contenente i nuovi dati del gioco e i tag aggiornati.
     * @return DTO del gioco aggiornato (GameResponseDTO).
     * @throws ResourceNotFoundException se il gioco non viene trovato.
     * @throws RuntimeException se un altro gioco con lo stesso titolo esiste già.
     */
    @Override // Indica che questo metodo implementa un metodo definito nell'interfaccia GameService.
    @Transactional // Assicura che l'operazione sia atomica.
    public GameResponseDTO updateGame(UUID id, GameRequestDTO gameRequestDTO) {
        // 1. Trova il gioco da aggiornare per ID. Se non trovato, lancia un'eccezione.
        Game game = gameRepository.findByIdWithTags(id) // Usa findByIdWithTags per caricare anche i tag esistenti
                .orElseThrow(() -> new ResourceNotFoundException("Gioco non trovato con ID: " + id));

        // 2. Verifica unicità del titolo, escludendo il gioco corrente.
        gameRepository.findByTitle(gameRequestDTO.getTitle())
                .ifPresent(existingGame -> {
                    if (!existingGame.getId().equals(id)) {
                        throw new RuntimeException("Un altro gioco con il titolo '" + gameRequestDTO.getTitle() + "' esiste già.");
                    }
                });

        // 3. Aggiorna i campi dell'entità Game con i dati dal DTO di richiesta.
        game.setTitle(gameRequestDTO.getTitle());
        game.setPrice(gameRequestDTO.getPrice());
        game.setReleaseDate(gameRequestDTO.getReleaseDate());
        game.setDeveloper(gameRequestDTO.getDeveloper());
        game.setPublisher(gameRequestDTO.getPublisher());

        // 4. Aggiorna l'associazione dei Tag:
        //    Pulisce i tag esistenti e aggiunge i nuovi basandosi sui nomi forniti.
        game.getTags().clear(); // Rimuove tutti i tag associati attualmente.
        if (gameRequestDTO.getTagNames() != null && !gameRequestDTO.getTagNames().isEmpty()) {
            for (String tagName : gameRequestDTO.getTagNames()) {
                Optional<Tag> existingTag = tagRepository.findByNameIgnoreCase(tagName);
                Tag tag;
                if (existingTag.isPresent()) {
                    tag = existingTag.get();
                } else {
                    tag = new Tag(tagName);
                    tagRepository.save(tag); // Salva il nuovo tag se non esiste.
                }
                game.addTag(tag); // Utilizza il metodo helper addTag per mantenere la bidirezionalità.
            }
        }

        // 5. Salva l'entità Game aggiornata nel database.
        Game updatedGame = gameRepository.save(game);

        // 6. Converte l'entità aggiornata in un GameResponseDTO e lo restituisce.
        return convertToResponseDto(updatedGame);
    }

    /**
     * Implementazione del metodo per eliminare un gioco dal sistema tramite il suo ID.
     * @param id L'ID (UUID) del gioco da eliminare.
     * @throws ResourceNotFoundException se il gioco non viene trovato.
     */
    @Override // Indica che questo metodo implementa un metodo definito nell'interfaccia GameService.
    @Transactional // Assicura che l'operazione sia atomica.
    public void deleteGame(UUID id) {
        // 1. Verifica se il gioco esiste prima di tentare l'eliminazione.
        if (!gameRepository.existsById(id)) {
            throw new ResourceNotFoundException("Gioco non trovato con ID: " + id);
        }
        // 2. Elimina il gioco dal database.
        //    Grazie alle configurazioni di cascata (CascadeType.ALL) e orphanRemoval=true
        //    nella relazione OneToMany con UserGame, le voci UserGame associate a questo gioco
        //    verranno eliminate automaticamente.
        //    Per la relazione ManyToMany con Tag, i tag non verranno eliminati, ma l'associazione nella tabella di join sì.
        gameRepository.deleteById(id);
    }

    /**
     * Implementazione del metodo per trovare giochi il cui titolo contiene la stringa specificata.
     * La ricerca è case-insensitive.
     * @param title Il titolo o parte del titolo da cercare.
     * @return Una lista di DTO dei giochi corrispondenti.
     */
    @Override // Indica che questo metodo implementa un metodo definito nell'interfaccia GameService.
    @Transactional(readOnly = true) // Transazione di sola lettura.
    public List<GameResponseDTO> findGamesByTitle(String title) {
        // Cerca i giochi per titolo (case-insensitive) e carica i tag.
        return gameRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Implementazione del metodo per trovare giochi associati a un tag specifico (tramite il nome del tag).
     * @param tagName Il nome del tag da cercare.
     * @return Una lista di DTO dei giochi corrispondenti al tag.
     */
    @Override // Indica che questo metodo implementa un metodo definito nell'interfaccia GameService.
    @Transactional(readOnly = true) // Transazione di sola lettura.
    public List<GameResponseDTO> findGamesByTagName(String tagName) {
        // Cerca i giochi associati a un tag specifico per nome (case-insensitive).
        return gameRepository.findByTags_NameIgnoreCase(tagName).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Implementazione del metodo per trovare giochi sviluppati da uno specifico sviluppatore.
     * La ricerca è case-insensitive.
     * @param developer Il nome dello sviluppatore.
     * @return Una lista di DTO dei giochi corrispondenti.
     */
    @Override // Indica che questo metodo implementa un metodo definito nell'interfaccia GameService.
    @Transactional(readOnly = true) // Transazione di sola lettura.
    public List<GameResponseDTO> findGamesByDeveloper(String developer) {
        // Cerca i giochi per sviluppatore (case-insensitive) e carica i tag.
        return gameRepository.findByDeveloperIgnoreCase(developer).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Implementazione del metodo per trovare giochi pubblicati da uno specifico editore.
     * La ricerca è case-insensitive.
     * @param publisher Il nome dell'editore.
     * @return Una lista di DTO dei giochi corrispondenti.
     */
    @Override // Indica che questo metodo implementa un metodo definito nell'interfaccia GameService.
    @Transactional(readOnly = true) // Transazione di sola lettura.
    public List<GameResponseDTO> findGamesByPublisher(String publisher) {
        // Cerca i giochi per editore (case-insensitive) e carica i tag.
        return gameRepository.findByPublisherIgnoreCase(publisher).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    // --- Metodi di Mappatura (Conversione tra Entità e DTO) ---

    /**
     * Metodo privato helper per convertire un'entità Game (dal database)
     * in un GameResponseDTO (per l'invio al client o per l'uso interno del servizio).
     * Questo metodo si occupa anche di mappare la collezione di Tag associati al gioco.
     * @param game L'entità Game da convertire.
     * @return Il GameResponseDTO corrispondente.
     */
    private GameResponseDTO convertToResponseDto(Game game) {
        // NOTA IMPORTANTE: Assicurati che la collezione di tag (game.getTags()) sia caricata
        // (non lazy-loaded) quando questo metodo viene chiamato, altrimenti potresti
        // incorrere in LazyInitializationException.
        // Le query personalizzate nel repository (findByIdWithTags, findAllWithTags, findBy...)
        // sono state aggiunte per garantire che i tag vengano caricati insieme al gioco.

        // Mappa la lista di Tag dell'entità Game a una lista di TagDTO.
        List<TagDTO> tagDtos = game.getTags().stream()
                                            .map(tag -> new TagDTO(tag.getId(), tag.getName()))
                                            .collect(Collectors.toList());

        // Crea e restituisce il GameResponseDTO completo.
        return new GameResponseDTO(
                game.getId(),
                game.getTitle(),
                game.getPrice(),
                game.getReleaseDate(),
                game.getDeveloper(),
                game.getPublisher(),
                tagDtos
        );
    }
}