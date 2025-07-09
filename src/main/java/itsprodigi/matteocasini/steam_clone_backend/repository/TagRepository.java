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
}