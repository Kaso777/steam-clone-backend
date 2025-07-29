package itsprodigi.matteocasini.steam_clone_backend.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Classe di utilità per la gestione dei JSON Web Token (JWT).
 * Permette la generazione, validazione e decodifica dei token.
 */
@Component
public class JwtUtil {

    // Chiave segreta per la firma del token (in Base64), configurata in application.properties
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    // Durata del token in millisecondi (es. 10 ore = 1000 * 60 * 60 * 10)
    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME;

    /**
     * Estrae lo username (subject) dal token JWT.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Estrae un claim specifico dal token usando una funzione di risoluzione.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Estrae tutti i claims da un token JWT.
     */
    private Claims extractAllClaims(String token) {
        JwtParser parser = Jwts
                .parser()
                .verifyWith(getSignKey())
                .build();

        Jws<Claims> claimsJws = parser.parseSignedClaims(token);
        return claimsJws.getPayload();
    }

    /**
     * Genera un token JWT per un utente autenticato.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        // Inserisce i ruoli dell'utente nel token
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Crea un token JWT con claims personalizzati.
     */
    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignKey(), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Verifica se un token è valido per l'utente specificato.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Verifica se un token JWT è scaduto.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Estrae la data di scadenza del token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Restituisce la chiave di firma decodificata da Base64.
     */
    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
