package itsprodigi.matteocasini.steam_clone_backend.repository;

import itsprodigi.matteocasini.steam_clone_backend.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID; // Per l'ID di UserProfile

@Repository // Indica a Spring che questa è un componente Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    // JpaRepository<TipoEntita, TipoIDEntita>
    // L'ID di UserProfile è UUID, lo stesso di User.

    // Non ci sono molti metodi specifici da aggiungere qui, dato che l'ID è la chiave principale.
    // Metodi come findById(UUID id) sono già forniti da JpaRepository.
}