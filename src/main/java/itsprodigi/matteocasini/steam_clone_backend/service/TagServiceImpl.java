package itsprodigi.matteocasini.steam_clone_backend.service;

import itsprodigi.matteocasini.steam_clone_backend.dto.TagDTO;
import itsprodigi.matteocasini.steam_clone_backend.model.Tag;
import itsprodigi.matteocasini.steam_clone_backend.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementazione del servizio per la gestione dei Tag.
 * Gestisce operazioni CRUD e mappatura tra entità Tag e DTO.
 */
@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    /**
     * Recupera un tag tramite il suo ID.
     *
     * @param id ID del tag
     * @return Optional contenente il TagDTO se presente
     */
    @Override
    public Optional<TagDTO> getTagById(Long id) {
        return tagRepository.findById(id).map(this::convertToDto);
    }

    /**
     * Recupera un tag tramite il suo nome.
     *
     * @param name nome del tag
     * @return Optional contenente il TagDTO se presente
     */
    @Override
    public Optional<TagDTO> getTagByName(String name) {
        return tagRepository.findByName(name).map(this::convertToDto);
    }

    /**
     * Restituisce la lista di tutti i tag.
     *
     * @return lista di TagDTO
     */
    @Override
    public List<TagDTO> getAllTags() {
        return tagRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Crea un nuovo tag oppure restituisce quello esistente con lo stesso nome.
     * Operazione transactional per garantire consistenza.
     *
     * @param tagDTO dati del tag da creare o cercare
     * @return TagDTO creato o esistente
     */
    @Override
    @Transactional
    public TagDTO createOrGetTag(TagDTO tagDTO) {
        return tagRepository.findByName(tagDTO.getName())
                .map(this::convertToDto)
                .orElseGet(() -> convertToDto(tagRepository.save(new Tag(tagDTO.getName()))));
    }

    /**
     * Aggiorna un tag esistente identificato dall'ID.
     *
     * @param id     ID del tag da aggiornare
     * @param tagDTO nuovi dati del tag
     * @return TagDTO aggiornato
     * @throws RuntimeException se il tag non viene trovato
     */
    @Override
    @Transactional
    public TagDTO updateTag(Long id, TagDTO tagDTO) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag non trovato con ID: " + id));
        tag.setName(tagDTO.getName());
        return convertToDto(tagRepository.save(tag));
    }

    /**
     * Elimina un tag tramite ID.
     * Rimuove anche il riferimento da tutti i giochi associati per evitare
     * problemi di consistenza nelle relazioni bidirezionali.
     *
     * @param id ID del tag da eliminare
     * @throws RuntimeException se il tag non viene trovato
     */
    @Override
    @Transactional
    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag non trovato con ID: " + id));

        // Rimuovi il tag da tutti i giochi associati
        tag.getGames().forEach(game -> {
            game.getTags().remove(tag);
        });

        // si svuota la lista per sicurezza
        tag.getGames().clear();

        tagRepository.delete(tag);
    }

    /**
     * Converte un'entità Tag in un DTO.
     *
     * @param tag entità Tag
     * @return TagDTO corrispondente
     */
    private TagDTO convertToDto(Tag tag) {
        TagDTO dto = new TagDTO();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        return dto;
    }
}