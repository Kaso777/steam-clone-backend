package itsprodigi.matteocasini.steam_clone_backend.service;

import itsprodigi.matteocasini.steam_clone_backend.dto.TagDTO;

import java.util.List;
import java.util.Optional;

public interface TagService {

    /**
     * Trova un tag per ID.
     * 
     * @param id ID del tag
     * @return tag trovato, se presente
     */
    Optional<TagDTO> getTagById(Long id);

    /**
     * Trova un tag per nome.
     * 
     * @param name nome del tag
     * @return tag trovato, se presente
     */
    Optional<TagDTO> getTagByName(String name);

    /**
     * Restituisce tutti i tag.
     * 
     * @return lista di tag
     */
    List<TagDTO> getAllTags();

    /**
     * Crea un nuovo tag o restituisce uno esistente con lo stesso nome.
     * 
     * @param tagDTO dati del tag
     * @return tag creato o esistente
     */
    TagDTO createOrGetTag(TagDTO tagDTO);

    /**
     * Aggiorna un tag esistente.
     * 
     * @param id ID del tag
     * @param tagDTO nuovi dati
     * @return tag aggiornato
     */
    TagDTO updateTag(Long id, TagDTO tagDTO);

    /**
     * Elimina un tag.
     * 
     * @param id ID del tag
     */
    void deleteTag(Long id);
}