package itsprodigi.matteocasini.steam_clone_backend.filter;


import itsprodigi.matteocasini.steam_clone_backend.service.security.CustomUserDetailsService; // Il nostro UserDetailsService
import itsprodigi.matteocasini.steam_clone_backend.service.security.JwtUtil; // Il nostro JwtUtil
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro di autenticazione JWT personalizzato.
 * Intercetta ogni richiesta HTTP per estrarre, validare e autenticare i token JWT.
 * Estende OncePerRequestFilter per garantire che il filtro venga eseguito una sola volta per richiesta.
 */
@Component // Indica che questa è una classe componente gestita da Spring.
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Autowired // Costruttore per l'iniezione delle dipendenze.
    public JwtAuthFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Implementazione del metodo doFilterInternal che viene eseguito per ogni richiesta.
     * Estrae il token JWT dall'header, lo valida e imposta l'autenticazione nel SecurityContext.
     * @param request La richiesta HTTP.
     * @param response La risposta HTTP.
     * @param filterChain La catena di filtri.
     * @throws ServletException Se si verifica un errore di servlet.
     * @throws IOException Se si verifica un errore di I/O.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Estrae l'header di autorizzazione.
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // 2. Controlla se l'header è presente e inizia con "Bearer ".
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Se non c'è un token JWT valido, passa la richiesta al prossimo filtro.
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Estrae il token JWT (rimuovendo "Bearer ").
        jwt = authHeader.substring(7);

        // 4. Estrae lo username dal token.
        username = jwtUtil.extractUsername(jwt);

        // 5. Se lo username è presente e l'utente non è già autenticato nel SecurityContext.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Carica i dettagli dell'utente usando il nostro CustomUserDetailsService.
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 6. Valida il token JWT.
            if (jwtUtil.validateToken(jwt, userDetails)) {
                // Se il token è valido, crea un oggetto di autenticazione.
                // UsernamePasswordAuthenticationToken è usato per rappresentare l'utente autenticato.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // Le credenziali (password) non sono necessarie qui dopo l'autenticazione via token.
                        userDetails.getAuthorities() // Le autorità (ruoli) dell'utente.
                );
                // Imposta i dettagli della richiesta (indirizzo IP, session ID, ecc.) per l'autenticazione.
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 7. Imposta l'oggetto di autenticazione nel SecurityContext di Spring.
                // Questo indica a Spring Security che l'utente è autenticato per questa richiesta.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // 8. Passa la richiesta al prossimo filtro nella catena.
        filterChain.doFilter(request, response);
    }
}