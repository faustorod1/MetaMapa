package ar.utn.ba.ddsi.utils;

import ar.utn.ba.ddsi.models.entities.Usuario;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.Getter;

import java.security.Key;
import java.util.Date;


public class JwtUtil {
    private static final String SECRET_STRING = "JuanferCURSÁ_BDD1245aluhsdbasikhujdgauyishdbajksgdyuasdgauysdgalhjsdbyuatsgvfdasbdasd[]¨*][[]¨*¨[]¨*";
    @Getter
    private static final Key key = Keys.hmacShaKeyFor(SECRET_STRING.getBytes());
    private static final long ACCESS_TOKEN_VALIDITY = 15 * 60 * 1000; // 15 min
    private static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000; // 7 días

    public static String generarAccessToken(Usuario user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("rol", user.getRol())
                .setIssuer("metamapa-usuarios-server")
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    public static String generarRefreshToken(Usuario user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuer("metamapa-usuarios-server")
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .claim("type", "refresh")
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    public static String validarToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}