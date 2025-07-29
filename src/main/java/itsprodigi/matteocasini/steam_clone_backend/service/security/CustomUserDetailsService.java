package itsprodigi.matteocasini.steam_clone_backend.service.security;

import itsprodigi.matteocasini.steam_clone_backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servizio che implementa UserDetailsService per Spring Security.
 * Utilizzato per caricare i dettagli dell'utente a partire dallo username.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Costruttore con iniezione di dipendenza.
     * @param userRepository Repository degli utenti.
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Carica un utente dal database dato lo username.
     * Utilizzato da Spring Security durante il login.
     *
     * @param username Lo username dell'utente.
     * @return I dettagli dell'utente (implementazione di UserDetails).
     * @throws UsernameNotFoundException Se l'utente non esiste.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}
