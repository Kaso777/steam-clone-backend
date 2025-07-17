package itsprodigi.matteocasini.steam_clone_backend.repository;

import itsprodigi.matteocasini.steam_clone_backend.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Per findByName

@Repository // Indica a Spring che questa è un componente Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    // JpaRepository<TipoEntita, TipoIDEntita>

    // Puoi aggiungere metodi personalizzati se necessario, ad esempio:
    Optional<Tag> findByName(String name); // Per trovare un Tag per nome

    /**
     * Trova un tag tramite il suo nome, ignorando la distinzione tra maiuscole e minuscole.
     * Utile per verificare l'esistenza di un tag prima di crearne uno nuovo.
     * @param name Il nome del tag da cercare.
     * @return Un Optional contenente il Tag se trovato, altrimenti Optional.empty().
     */
    Optional<Tag> findByNameIgnoreCase(String name);
}