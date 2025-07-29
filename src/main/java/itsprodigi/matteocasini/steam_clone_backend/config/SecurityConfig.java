package itsprodigi.matteocasini.steam_clone_backend.config;

import itsprodigi.matteocasini.steam_clone_backend.filter.JwtAuthFilter; // Importa il nostro filtro JWT
import itsprodigi.matteocasini.steam_clone_backend.service.security.CustomUserDetailsService; // Importa il nostro UserDetailsService
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager; // Importa AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; // Importa DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // Importa AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // NUOVO: Import per abilitare la sicurezza a livello di metodo
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy; // Importa SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Importa UsernamePasswordAuthenticationFilter
import itsprodigi.matteocasini.steam_clone_backend.service.security.CustomAuthExceptionHandler;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity // NUOVO: Abilita la sicurezza a livello di metodo (es. @PreAuthorize)
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter; // Inietta il nostro filtro JWT
    private final CustomUserDetailsService userDetailsService; // Inietta il nostro UserDetailsService
    private final CustomAuthExceptionHandler authExceptionHandler;

    // Costruttore per l'iniezione delle dipendenze
    public SecurityConfig(JwtAuthFilter jwtAuthFilter,
                          CustomUserDetailsService userDetailsService,
                          CustomAuthExceptionHandler authExceptionHandler) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
        this.authExceptionHandler = authExceptionHandler;
    }

    /**
     * Definisce un Bean per il PasswordEncoder.
     * Utilizza BCryptPasswordEncoder, un algoritmo di hashing robusto per le password.
     *
     * @return Un'istanza di BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Definisce un Bean per l'AuthenticationManager.
     * Questo è il bean che il nostro AuthController utilizzerà per autenticare gli utenti.
     *
     * @param config L'AuthenticationConfiguration fornita da Spring.
     * @return Un'istanza di AuthenticationManager.
     * @throws Exception Se si verifica un errore durante la configurazione.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Definisce il DaoAuthenticationProvider.
     * Indica a Spring Security quale UserDetailsService usare per caricare gli utenti
     * e quale PasswordEncoder usare per verificare le password.
     *
     * @return Un'istanza di DaoAuthenticationProvider.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Il nostro CustomUserDetailsService
        authProvider.setPasswordEncoder(passwordEncoder()); // Il nostro PasswordEncoder
        return authProvider;
    }

    /**
     * Definisce la catena di filtri di sicurezza HTTP.
     * Questo è il punto centrale dove si configurano le regole di autorizzazione
     * per i diversi percorsi URL e si integra il filtro JWT.
     *
     * @param http L'oggetto HttpSecurity per configurare le regole di sicurezza.
     * @return La catena di filtri di sicurezza configurata.
     * @throws Exception Se si verifica un errore durante la configurazione.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disabilita la protezione CSRF (Cross-Site Request Forgery).
                // Essenziale per API REST stateless che usano token (es. JWT).
                .csrf(AbstractHttpConfigurer::disable)
                // Gestisce le eccezioni di autenticazione tramite il nostro CustomAuthExceptionHandler.
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authExceptionHandler)
                        .accessDeniedHandler(authExceptionHandler)
                )

                // Configura le regole di autorizzazione per le richieste HTTP.
                .authorizeHttpRequests(authorize -> authorize
                        // Endpoint accessibili senza autenticazione (es. /api/auth/register, /api/auth/login)
                        .requestMatchers("/api/auth/**").permitAll()

                        // MODIFICATO: Le richieste DELETE per gli utenti ora richiedono solo autenticazione.
                        // La logica specifica (admin O proprio utente) sarà gestita da @PreAuthorize nel servizio.
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").authenticated()

                        // Endpoint GET per recuperare TUTTI gli utenti, accessibile SOLO agli ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")

                        // Tutte le altre richieste che iniziano con /api/ (es. GET singolo utente, PUT utente, ecc.)
                        // richiedono solo autenticazione (qualsiasi utente loggato)
                        .requestMatchers("/api/**").authenticated()

                        // Nega l'accesso a qualsiasi altra rotta non esplicitamente permessa o autenticata
                        .anyRequest().denyAll()
                )
                // Configura la gestione delle sessioni per essere stateless.
                // Ciò significa che Spring Security non creerà né utilizzerà sessioni HTTP.
                // Questo è il comportamento desiderato quando si usano i JWT, in quanto ogni richiesta
                // è autosufficiente e contiene le proprie informazioni di autenticazione.
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Aggiunge il nostro AuthenticationProvider.
                .authenticationProvider(authenticationProvider())
                // Aggiunge il nostro filtro JWT prima del filtro di autenticazione standard di Spring Security.
                // Questo fa sì che il nostro filtro JWT venga eseguito per primo per validare il token.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // Costruisce e restituisce la catena di filtri di sicurezza.
        return http.build();
    }
}
