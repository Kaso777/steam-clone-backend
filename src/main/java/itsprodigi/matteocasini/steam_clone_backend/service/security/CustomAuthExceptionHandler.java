package itsprodigi.matteocasini.steam_clone_backend.service.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Gestisce gli errori di autenticazione e autorizzazione restituendo risposte
 * JSON personalizzate:
 * - 401 Unauthorized per token mancanti o non validi
 * - 403 Forbidden per accessi negati da utenti autenticati
 */
@Component
public class CustomAuthExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    // Risposta per 401 Unauthorized (utente non autenticato)
    @Override
    public void commence(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Autenticazione richiesta. Token assente o non valido.\"}");
    }

    // Risposta per 403 Forbidden (utente autenticato ma non autorizzato)
    @Override
    public void handle(HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Accesso negato. Permessi insufficienti.\"}");
    }
}