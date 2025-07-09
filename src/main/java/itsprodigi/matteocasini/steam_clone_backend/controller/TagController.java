package itsprodigi.matteocasini.steam_clone_backend.controller;


import itsprodigi.matteocasini.steam_clone_backend.service.TagService; // Importa l'interfaccia del servizio
import itsprodigi.matteocasini.steam_clone_backend.dto.TagDTO;       // Importa il nostro unico TagDTO
import jakarta.validation.Valid; // Per attivare la validazione Bean Validation definita nel DTO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST per la gestione delle operazioni sui Tag.
 * Espone gli endpoint API per creare, leggere, aggiornare ed eliminare i tag.
 */
@RestController // Indica che questa classe è un controller REST e gestisce le richieste HTTP.
@RequestMapping("/api/tags") // Definisce il percorso base per tutti gli endpoint di questo controller.
public class TagController {

    // Inietta l'interfaccia del servizio. Spring si occuperà di iniettare l'implementazione concreta (TagServiceImpl).
    private final TagService tagService;

    @Autowired // Costruttore per l'iniezione delle dipendenze.
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Endpoint GET per recuperare tutti i tag esistenti.
     * Esempio di URL: GET /api/tags
     * @return ResponseEntity contenente una lista di TagDTO e lo stato HTTP 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<TagDTO>> getAllTags() {
        // Chiama il servizio per ottenere tutti i tag, che verranno restituiti come lista di TagDTO.
        List<TagDTO> tags = tagService.getAllTags();
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    /**
     * Endpoint GET per recuperare un tag specifico tramite il suo ID.
     * Esempio di URL: GET /api/tags/1
     * @param id L'ID numerico del tag, estratto dal percorso URL.
     * @return ResponseEntity contenente il TagDTO e lo stato HTTP (200 OK se trovato, 404 NOT FOUND altrimenti).
     */
    @GetMapping("/{id}")
    public ResponseEntity<TagDTO> getTagById(@PathVariable Long id) {
        // Chiama il servizio per ottenere il tag per ID.
        return tagService.getTagById(id)
                // Se il tag è presente, crea una risposta HTTP 200 OK con il DTO del tag.
                .map(tagDto -> new ResponseEntity<>(tagDto, HttpStatus.OK))
                // Se il tag non è presente (Optional vuoto), restituisce una risposta HTTP 404 NOT FOUND.
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint POST per creare un nuovo tag.
     * Se un tag con lo stesso nome esiste già, restituisce quello esistente (evitando duplicati).
     * Esempio di URL: POST /api/tags
     * Corpo della richiesta: {"name": "NuovoTag"}
     *
     * @param tagDTO Il TagDTO inviato nel corpo della richiesta con i dati del nuovo tag (principalmente il nome).
     * @Valid: Attiva la validazione dei campi del `TagDTO` in base alle annotazioni (es. @NotBlank, @Size).
     * Se la validazione fallisce, Spring MVC genererà automaticamente un errore 400 Bad Request.
     * @return ResponseEntity contenente il TagDTO del tag creato o trovato e lo stato HTTP 201 CREATED.
     */
    @PostMapping
    public ResponseEntity<TagDTO> createTag(@Valid @RequestBody TagDTO tagDTO) {
        // Chiama il servizio per creare o ottenere un tag.
        // Il servizio restituisce un TagDTO (sia che sia stato creato ex novo o recuperato).
        TagDTO createdTag = tagService.createOrGetTag(tagDTO);
        return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
    }

    /**
     * Endpoint PUT per aggiornare un tag esistente.
     * Esempio di URL: PUT /api/tags/1
     * Corpo della richiesta: {"name": "NomeAggiornato"}
     *
     * @param id L'ID del tag da aggiornare, estratto dal percorso URL.
     * @param tagDTO Il TagDTO inviato nel corpo della richiesta con i nuovi dati del tag.
     * @Valid: Attiva la validazione sui campi del DTO di input.
     * @return ResponseEntity contenente il TagDTO del tag aggiornato e lo stato HTTP (200 OK).
     * In caso di errore (es. tag non trovato), restituisce 404 NOT FOUND.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TagDTO> updateTag(@PathVariable Long id, @Valid @RequestBody TagDTO tagDTO) {
        try {
            // Chiama il servizio per aggiornare il tag.
            TagDTO updatedTag = tagService.updateTag(id, tagDTO);
            return new ResponseEntity<>(updatedTag, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Gestione generica degli errori.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint DELETE per eliminare un tag specifico.
     * Esempio di URL: DELETE /api/tags/1
     * @param id L'ID numerico del tag da eliminare, estratto dal percorso URL.
     * @return ResponseEntity con stato HTTP (204 NO CONTENT se l'eliminazione ha successo).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}