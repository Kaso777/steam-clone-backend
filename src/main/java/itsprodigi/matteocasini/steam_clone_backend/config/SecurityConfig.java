package itsprodigi.matteocasini.steam_clone_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Classe di configurazione per la sicurezza dell'applicazione.
 * Qui definiamo i Bean relativi a Spring Security, come il PasswordEncoder
 * e le regole di autorizzazione per gli endpoint HTTP.
 *
 * ATTENZIONE: La configurazione attuale permette tutte le richieste su /api/** senza autenticazione
 * ed è intesa SOLO per la fase di SVILUPPO e TEST.
 * In un ambiente di PRODUZIONE, saranno necessarie regole di sicurezza molto più robuste.
 */
@Configuration // Indica a Spring che questa classe contiene definizioni di Bean e configurazioni.
@EnableWebSecurity // Abilita le funzionalità di sicurezza web di Spring Security.
public class SecurityConfig {

    /**
     * Definisce un Bean per il PasswordEncoder.
     * Utilizza BCryptPasswordEncoder, un algoritmo di hashing robusto per le password.
     * @return Un'istanza di BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Definisce la catena di filtri di sicurezza HTTP.
     * Questo è il punto centrale dove si configurano le regole di autorizzazione
     * per i diversi percorsi URL.
     *
     * @param http L'oggetto HttpSecurity per configurare le regole di sicurezza.
     * @return La catena di filtri di sicurezza configurata.
     * @throws Exception Se si verifica un errore durante la configurazione.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disabilita la protezione CSRF (Cross-Site Request Forgery).
            // È comune disabilitarla per API REST stateless che usano token (es. JWT).
            // In produzione, valuta attentamente i rischi o abilitala con token appropriati.
            .csrf(AbstractHttpConfigurer::disable)
            // Configura le regole di autorizzazione per le richieste HTTP.
            .authorizeHttpRequests(authorize -> authorize
                // Permette l'accesso a tutti gli endpoint che iniziano con /api/
                // Questa regola è PERICOLOSA IN PRODUZIONE, usala solo per lo sviluppo.
                .requestMatchers("/api/**").permitAll()
                // Tutte le altre richieste richiedono autenticazione.
                .anyRequest().authenticated()
            );
        // Costruisce e restituisce la catena di filtri di sicurezza.
        return http.build();
    }
}