package itsprodigi.matteocasini.steam_clone_backend.service;


import itsprodigi.matteocasini.steam_clone_backend.dto.TagDTO; // Importiamo il nostro unico TagDTO
import java.util.List;
import java.util.Optional;

/**
 * Interfaccia per il servizio di gestione dei Tag.
 * Definisce il contratto dei metodi disponibili per interagire con i tag,
 * garantendo il disaccoppiamento tra l'implementazione concreta e i componenti che la utilizzano.
 */
public interface TagService {

    /**
     * Recupera un tag tramite il suo ID.
     * @param id L'ID del tag da recuperare.
     * @return Un Optional contenente il TagDTO se trovato, altrimenti Optional.empty().
     */
    Optional<TagDTO> getTagById(Long id);

    /**
     * Recupera un tag tramite il suo nome.
     * @param name Il nome del tag da recuperare.
     * @return Un Optional contenente il TagDTO se trovato, altrimenti Optional.empty().
     */
    Optional<TagDTO> getTagByName(String name);

    /**
     * Recupera tutti i tag disponibili.
     * @return Una lista di TagDTO.
     */
    List<TagDTO> getAllTags();

    /**
     * Crea un nuovo tag o restituisce un tag esistente se un tag con lo stesso nome è già presente.
     * @param tagDTO Il DTO contenente i dati del tag da creare (principalmente il nome).
     * @return Il TagDTO del tag creato o trovato.
     */
    TagDTO createOrGetTag(TagDTO tagDTO);

    /**
     * Aggiorna i dati di un tag esistente.
     * @param id L'ID del tag da aggiornare.
     * @param tagDTO Il DTO contenente i nuovi dati del tag.
     * @return Il TagDTO del tag aggiornato.
     * @throws RuntimeException se il tag con l'ID specificato non viene trovato.
     */
    TagDTO updateTag(Long id, TagDTO tagDTO);

    /**
     * Elimina un tag tramite il suo ID.
     * @param id L'ID del tag da eliminare.
     */
    void deleteTag(Long id);
}