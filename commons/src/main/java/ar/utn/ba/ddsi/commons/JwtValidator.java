package ar.utn.ba.ddsi.commons;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Componente reutilizable para validar tokens JWT y construir un objeto
 * de autenticación para Spring Security.
 * Se configura a través de la propiedad 'jwt.secret' en application.properties.
 */
//@Component
public class JwtValidator {

    @Value("${jwt.secret}")
    private String secretString;

    private Key key;

    private static final Logger logger = LoggerFactory.getLogger(JwtValidator.class);

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretString.getBytes());
    }

    // TODO: Manchuas qué va acá
    /**
     * Valida el token y, si es válido, construye y devuelve un objeto Authentication.
     *
     * @param token El token JWT extraído de la cabecera de la solicitud.
     * @return Un Optional con el objeto Authentication si el token es válido, o un Optional vacío si no lo es.
     */
//    public Optional<Authentication> getAuthentication(String token) {
//        return validateAndGetClaims(token).map(claims -> {
//            String email = claims.getSubject();
//            String rol = claims.get("rol", String.class);
//
//            // Spring Security espera que los roles tengan el prefijo "ROLE_"
//            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol));
//
//            // Creamos el objeto de autenticación que Spring Security utilizará
//            return
}