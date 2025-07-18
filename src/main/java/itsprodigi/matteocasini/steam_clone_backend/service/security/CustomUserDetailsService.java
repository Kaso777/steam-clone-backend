package itsprodigi.matteocasini.steam_clone_backend.service.security;

import itsprodigi.matteocasini.steam_clone_backend.repository.UserRepository; // Importa il tuo UserRepository
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servizio personalizzato per caricare i dettagli dell'utente.
 * Implementa l'interfaccia UserDetailsService di Spring Security,
 * permettendo al framework di autenticazione di recuperare gli utenti dal database.
 */
@Service // Indica che questa è una classe di servizio gestita da Spring.
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository; // Il tuo repository per accedere ai dati utente.

    // Costruttore per l'iniezione della dipendenza di UserRepository.
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Carica i dettagli dell'utente dato il suo username.
     * Questo metodo viene chiamato da Spring Security durante il processo di autenticazione.
     *
     * @param username Il nome utente dell'utente da caricare.
     * @return Un oggetto UserDetails (la tua entità User) contenente i dettagli dell'utente.
     * @throws UsernameNotFoundException Se l'utente non viene trovato nel database.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Cerca l'utente nel database tramite il suo username.
        // Utilizziamo un orElseThrow per lanciare un'eccezione specifica se l'utente non esiste.
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}