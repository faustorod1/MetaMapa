package ar.utn.ba.ddsi.commons;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Key;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class JwtValidator {
    private Key key;
    private static final Logger logger = LoggerFactory.getLogger(JwtValidator.class);

    public JwtValidator(String secretString) {
        this.key = Keys.hmacShaKeyFor(secretString.getBytes());
    }

    public Optional<Authentication> getAuthentication(String token) {
        return validateAndGetClaims(token).map(claims -> {
            String email = claims.getSubject();
            Long userId = claims.get("userId", Long.class);
            String rol = claims.get("rol", String.class);

            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol));

            return new UsernamePasswordAuthenticationToken(email, null, authorities);
        });
    }

    private Optional<Claims> validateAndGetClaims(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Optional.of(claims);
        } catch (ExpiredJwtException e) {
            logger.error("Token JWT expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Token JWT no soportado: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Token JWT malformado: {}", e.getMessage());
        } catch (SignatureException e) {
            logger.error("Firma del token JWT inválida: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Claims del token JWT vacíos: {}", e.getMessage());
        }
        return Optional.empty();
    }
}