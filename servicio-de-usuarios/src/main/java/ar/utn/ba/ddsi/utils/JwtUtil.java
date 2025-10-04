package ar.utn.ba.ddsi.utils;

import ar.utn.ba.ddsi.models.entities.Usuario;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;

import java.security.Key;
import java.util.Date;

public class JwtUtil {
    @Getter
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.RS256);
    private static final long ACCESS_TOKEN_VALIDITY = 15 * 60 * 1000; // 15 min
    private static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000; // 7 días

    public static String generarAccessToken(Usuario user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("rol", user.getRol())
                .setIssuer("metamapa-usuarios-server")
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
                .signWith(key)
                .compact();
    }
    public static String generarRefreshToken(Usuario user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuer("metamapa-usuarios-server")
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .claim("type", "refresh")
                .signWith(key)
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

/*
    private static final PublicKey publicKey;
    static {
        // Genera par de claves RSA (2048 bits por defecto, seguro)
        keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();

        // En producción, reemplaza esto por carga desde archivo o config:
        // privateKey = loadPrivateKeyFromFile("private.pem");
        // publicKey = loadPublicKeyFromFile("public.pem");
    }
    // Getter para la clave pública (para uso interno o exposición)
    @Getter
    public static PublicKey getPublicKey() {
        return publicKey;
    }
    // Método para exportar la clave pública en formato PEM (base64) para compartir con otros servicios
    public static String getPublicKeyPem() {
        if (!(publicKey instanceof RSAPublicKey)) {
            throw new IllegalStateException("Clave pública no es RSA");
        }
        RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;

        // Formato PEM simple (sin encabezados completos; ajusta si necesitas)
        String pem = "-----BEGIN PUBLIC KEY-----\n" +
            Base64.getEncoder().encodeToString(
                rsaPublicKey.getEncoded()
            ) +
            "\n-----END PUBLIC KEY-----";
        return pem;
    }

    // Método para generar refresh token (firma con clave privada)
    public static String generarRefreshToken(Usuario user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuer("metamapa-usuarios-server")
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .claim("type", "refresh")
                .signWith(privateKey)  // Usa clave privada para firmar
                .compact();
    }
    // Método para validar token (verifica con clave pública)
    public static String validarToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(publicKey)  // Usa clave pública para verificar
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException e) {
            // Maneja errores de verificación (ej. token inválido o expirado)
            throw new JwtException("Token inválido: " + e.getMessage());
        }
    }
    // En producción, agrega métodos para cargar claves desde archivos (ejemplo con PEM)
    // private static PrivateKey loadPrivateKeyFromFile(String filePath) { ... }
    // private static PublicKey loadPublicKeyFromFile(String filePath) { ... }
    // Usa java.security.spec.PKCS8EncodedKeySpec y KeyFactory para parsear PEM.

 */
}
