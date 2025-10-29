package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.models.dto.AuthResponseDTO;
import ar.utn.ba.ddsi.models.dto.RefreshRequestDTO;
import ar.utn.ba.ddsi.models.dto.TokenResponseDTO;
import ar.utn.ba.ddsi.models.dto.UserRolesDTO;
import ar.utn.ba.ddsi.models.entities.Usuario;
import ar.utn.ba.ddsi.models.exceptions.NotFoundException;
import ar.utn.ba.ddsi.models.exceptions.UsuarioExistenteException;
import ar.utn.ba.ddsi.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import ar.utn.ba.ddsi.services.LoginService;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private final LoginService loginService;
    @Autowired
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<AuthResponseDTO> login(@RequestBody Map<String, String> credentials) {
        try {
            String email = credentials.get("email");
            String password = credentials.get("password");

            if (email == null || email.trim().isEmpty() ||
                    password == null || password.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            Usuario usuario = loginService.autenticarUsuario(email, password);

            String accessToken = loginService.generarAccessToken(usuario);
            String refreshToken = loginService.generarRefreshToken(usuario);

            AuthResponseDTO response = AuthResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
    
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDTO> refresh(@RequestBody RefreshRequestDTO request) {
        try {
            String email = jwtUtil.validarToken(request.getRefreshToken());

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtUtil.getKey())
                    .build()
                    .parseClaimsJws(request.getRefreshToken())
                    .getBody();

            if (!"refresh".equals(claims.get("type"))) {
                return ResponseEntity.badRequest().build();
            }

            Usuario usuario = loginService.getUsuario(email);

            String newAccessToken = loginService.generarAccessToken(usuario);
            TokenResponseDTO response = new TokenResponseDTO(newAccessToken, request.getRefreshToken());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody Map<String, String> credentials){
        try {
            String email = credentials.get("email");
            String password = credentials.get("password");
            String nombre = credentials.get("nombre");
            String apellido = credentials.get("apellido");

            if (email == null || email.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            Usuario usuario = loginService.registrarUsuario(email, password, nombre, apellido, null);

            String accessToken = loginService.generarAccessToken(usuario);
            String refreshToken = loginService.generarRefreshToken(usuario);

            AuthResponseDTO response = AuthResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

            return ResponseEntity.ok(response);
        } catch (UsuarioExistenteException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/roles")
    public ResponseEntity<UserRolesDTO> getRoles(Authentication authentication) {
        try {
            String username = authentication.getName();
            UserRolesDTO response = loginService.obtenerRolUsuario(username);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}