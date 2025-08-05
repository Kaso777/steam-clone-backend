package itsprodigi.matteocasini.steam_clone_backend.service.security;

import itsprodigi.matteocasini.steam_clone_backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servizio che implementa UserDetailsService per Spring Security.
 * Carica i dati dell'utente (necessari per l'autenticazione) partendo dallo
 * username.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Carica l'utente dal database tramite lo username.
     *
     * @param username lo username dell'utente
     * @return un oggetto che implementa UserDetails (necessario per
     *         l'autenticazione)
     * @throws UsernameNotFoundException se l'utente non esiste
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}