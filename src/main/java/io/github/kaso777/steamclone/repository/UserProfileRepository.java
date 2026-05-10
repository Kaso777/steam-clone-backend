package io.github.kaso777.steamclone.repository;

import io.github.kaso777.steamclone.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    // Metodi standard come findById, save, deleteById sono giÃ  ereditati da JpaRepository
}
