package itsprodigi.matteocasini.steam_clone_backend.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Classe di utilità per la gestione dei JSON Web Token (JWT).
 * Utilizzata per creare, validare e leggere i token JWT associati all'autenticazione degli utenti.
 */
@Component
public class JwtUtil {

    // Chiave segreta per firmare il token, in formato Base64, configurata in application.properties
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    // Tempo di scadenza del token in millisecondi (es. 1000 * 60 * 60 * 10 = 10 ore)
    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME;

    /**
     * Estrae lo username (subject) dal token JWT.
     * @param token il token JWT
     * @return lo username contenuto nel token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Estrae un claim generico da un token JWT usando una funzione di estrazione.
     * @param token il token JWT
     * @param claimsResolver funzione che riceve i Claims e restituisce il dato desiderato
     * @return il valore del claim richiesto
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Estrae tutti i claims (dati contenuti) da un token JWT.
     * @param token il token JWT
     * @return tutti i claims come oggetto Claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder() // Costruisce un parser JWT
                .setSigningKey(getSignKey()) // Imposta la chiave per la firma
                .build()
                .parseClaimsJws(token) // Parsea e verifica il token
                .getBody(); // Ritorna il corpo (claims)
    }

    /**
     * Genera un token JWT a partire dai dati dell'utente autenticato.
     * Include anche i ruoli dell'utente come claim personalizzato ("roles").
     * @param userDetails oggetto UserDetails dell'utente
     * @return il token JWT generato
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        
        // Inserisce i ruoli dell'utente nel token (es. ROLE_ADMIN, ROLE_USER)
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Crea effettivamente il token JWT con claims, username, date e firma.
     * @param claims mappa dei claims personalizzati da includere nel token
     * @param userName username dell'utente (subject del token)
     * @return il token JWT firmato
     */
    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .claims(claims) // Inserisce i claims personalizzati
                .subject(userName) // Imposta lo username come subject
                .issuedAt(new Date(System.currentTimeMillis())) // Data di emissione
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Scadenza del token
                .signWith(getSignKey(), SignatureAlgorithm.HS256) // Firma HMAC con algoritmo SHA-256
                .compact(); // Compatta il tutto in una stringa JWT
    }

    /**
     * Verifica se un token JWT è valido per un determinato utente.
     * @param token il token JWT
     * @param userDetails i dettagli dell'utente
     * @return true se il token è valido (username corrisponde e non è scaduto)
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Controlla se il token è scaduto.
     * @param token il token JWT
     * @return true se il token è scaduto
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Estrae la data di scadenza da un token JWT.
     * @param token il token JWT
     * @return la data di scadenza
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Converte la chiave segreta (in Base64) in una chiave HMAC utilizzabile per firmare/verificare i token.
     * @return la chiave segreta come oggetto Key
     */
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY); // Decodifica Base64
        return Keys.hmacShaKeyFor(keyBytes); // Crea la chiave HMAC
    }
}
// Questo metodo è utilizzato per ottenere la chiave segreta in un formato compatibile con JWT. Genera e valida i token JWT utilizzando questa chiave.