package itsprodigi.matteocasini.steam_clone_backend.service;


import itsprodigi.matteocasini.steam_clone_backend.model.Tag;
import itsprodigi.matteocasini.steam_clone_backend.repository.TagRepository;
import itsprodigi.matteocasini.steam_clone_backend.service.TagService; // Importa l'interfaccia!
import itsprodigi.matteocasini.steam_clone_backend.dto.TagDTO; // Importa il DTO

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // Utile per mappare liste di entità a liste di DTO

/**
 * Implementazione concreta dell'interfaccia TagService.
 * Contiene la logica di business effettiva per la gestione dei Tag.
 * Mappa le entità del database ai DTO per le interazioni con il controller e il client.
 */
@Service // Indica a Spring che questa è un componente di servizio gestito dal suo IoC container.
public class TagServiceImpl implements TagService { // Implementa l'interfaccia TagService

    private final TagRepository tagRepository; // Repository per l'entità Tag

    @Autowired // Inietta la dipendenza TagRepository.
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    /**
     * Implementazione del metodo per recuperare un tag per ID.
     * Converte l'entità Tag trovata in un TagDTO.
     * @param id L'ID del tag.
     * @return Un Optional contenente il TagDTO se il tag esiste, altrimenti un Optional vuoto.
     */
    @Override // Indica che questo metodo implementa un metodo dell'interfaccia TagService.
    public Optional<TagDTO> getTagById(Long id) {
        // Cerca l'entità Tag nel database usando il Repository.
        return tagRepository.findById(id)
                // Se l'entità è presente, la mappa in un TagDTO usando il metodo helper.
                .map(this::convertToDto);
    }

    /**
     * Implementazione del metodo per recuperare un tag per nome.
     * Converte l'entità Tag trovata in un TagDTO.
     * @param name Il nome del tag.
     * @return Un Optional contenente il TagDTO se il tag esiste, altrimenti un Optional vuoto.
     */
    @Override // Indica che questo metodo implementa un metodo dell'interfaccia TagService.
    public Optional<TagDTO> getTagByName(String name) {
        // Cerca l'entità Tag per nome nel database.
        return tagRepository.findByName(name)
                // Se l'entità è presente, la mappa in un TagDTO.
                .map(this::convertToDto);
    }

    /**
     * Implementazione del metodo per recuperare tutti i tag.
     * Mappa ogni entità Tag in un TagDTO e restituisce una lista di DTO.
     * @return Una lista di TagDTO.
     */
    @Override // Indica che questo metodo implementa un metodo dell'interfaccia TagService.
    public List<TagDTO> getAllTags() {
        // Recupera tutte le entità Tag dal database.
        return tagRepository.findAll().stream()
                // Mappa ogni entità Tag a un TagDTO.
                .map(this::convertToDto)
                // Colleziona i DTO in una lista.
                .collect(Collectors.toList());
    }

    /**
     * Implementazione del metodo per creare un nuovo tag o recuperarne uno esistente.
     * Se un tag con lo stesso nome esiste già, restituisce quello esistente. Altrimenti, crea un nuovo tag.
     * @param tagDTO Il DTO contenente il nome del tag da creare/recuperare.
     * @return Il TagDTO del tag creato o esistente.
     */
    @Override // Indica che questo metodo implementa un metodo dell'interfaccia TagService.
    @Transactional // Assicura che l'operazione sia atomica.
    public TagDTO createOrGetTag(TagDTO tagDTO) {
        // Cerca se esiste già un tag con lo stesso nome (case-insensitive per robustezza).
        Optional<Tag> existingTag = tagRepository.findByName(tagDTO.getName());

        if (existingTag.isPresent()) {
            // Se il tag esiste già, restituisce il suo DTO.
            return convertToDto(existingTag.get());
        } else {
            // Se il tag non esiste, crea una nuova entità Tag dal nome fornito nel DTO.
            Tag newTag = new Tag(tagDTO.getName());
            // Salva la nuova entità Tag nel database.
            Tag savedTag = tagRepository.save(newTag);
            // Converte l'entità salvata in un TagDTO e lo restituisce.
            return convertToDto(savedTag);
        }
    }

    /**
     * Implementazione del metodo per aggiornare i dati di un tag esistente.
     * @param id L'ID del tag da aggiornare.
     * @param tagDTO Il DTO contenente i nuovi dati del tag (principalmente il nome).
     * @return Il TagDTO del tag aggiornato.
     * @throws RuntimeException se il tag con l'ID specificato non viene trovato.
     */
    @Override // Indica che questo metodo implementa un metodo dell'interfaccia TagService.
    @Transactional
    public TagDTO updateTag(Long id, TagDTO tagDTO) {
        // Cerca il tag da aggiornare per ID. Se non trovato, lancia un'eccezione.
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag non trovato con ID: " + id));

        // Aggiorna il nome del tag con il valore fornito nel DTO.
        tag.setName(tagDTO.getName());
        // Salva l'entità Tag aggiornata nel database.
        Tag updatedTag = tagRepository.save(tag);
        // Converte l'entità aggiornata in un TagDTO e lo restituisce.
        return convertToDto(updatedTag);
    }

    /**
     * Implementazione del metodo per eliminare un tag tramite il suo ID.
     * @param id L'ID del tag da eliminare.
     */
    @Override // Indica che questo metodo implementa un metodo dell'interfaccia TagService.
    @Transactional
    public void deleteTag(Long id) {
        // Elimina il tag dal database tramite il suo ID.
        // Se il tag non esiste, JpaRepository.deleteById() non lancerà un'eccezione,
        // ma non farà nulla. Puoi aggiungere un controllo Optional.isPresent() prima se vuoi un errore specifico.
        tagRepository.deleteById(id);
    }

    // --- Metodi di Mappatura (Conversione tra Entità e DTO) ---

    /**
     * Metodo privato helper per convertire un'entità Tag (dal database)
     * in un TagDTO (per l'invio al client o per l'uso interno del servizio).
     * @param tag L'entità Tag da convertire.
     * @return Il TagDTO corrispondente.
     */
    private TagDTO convertToDto(Tag tag) {
        // Creiamo una nuova istanza di TagDTO e popoliamo i suoi campi
        // utilizzando i getter dell'entità Tag.
        TagDTO dto = new TagDTO();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        return dto;
    }
}