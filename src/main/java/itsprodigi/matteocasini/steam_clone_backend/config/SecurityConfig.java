package itsprodigi.matteocasini.steam_clone_backend.config;

import org.springframework.context.annotation.Bean; // Importa l'annotazione @Bean
import org.springframework.context.annotation.Configuration; // Importa l'annotazione @Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // L'implementazione specifica che useremo
import org.springframework.security.crypto.password.PasswordEncoder; // L'interfaccia

/**
 * Classe di configurazione per la sicurezza dell'applicazione.
 * Qui definiamo i Bean relativi a Spring Security, come il PasswordEncoder.
 * Anche se l'integrazione completa di Spring Security è posticipata,
 * il PasswordEncoder è una best practice fondamentale per la gestione delle password.
 */
@Configuration // Indica a Spring che questa classe contiene definizioni di Bean e configurazioni.
public class SecurityConfig {

    /**
     * Definisce un Bean per il PasswordEncoder.
     * Questo metodo dice a Spring: "Quando qualcuno richiede un oggetto di tipo PasswordEncoder,
     * per favore, crea e fornisci un'istanza di BCryptPasswordEncoder."
     * BCryptPasswordEncoder è un algoritmo di hashing delle password robusto e raccomandato.
     * @return Un'istanza di BCryptPasswordEncoder.
     */
    @Bean // Questa annotazione indica che il metodo produce un Bean che sarà gestito dal contenitore IoC di Spring.
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // In futuro, quando implementeremo Spring Security completamente,
    // questa classe conterrà anche la configurazione per la catena dei filtri di sicurezza,
    // le regole di autorizzazione, l'autenticazione JWT, ecc.
}